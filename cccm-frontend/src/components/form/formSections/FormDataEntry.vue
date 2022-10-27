<template>
  <div>
    <Form :form="dataModel" 
      :submission="initData" 
      v-on:change="handleChangeEvent" 
      v-on:blur="handleBlurEvent"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import { objectToString } from '@vue/shared';
import {updateForm} from "@/components/form.api";
import {useStore} from "@/stores/store";
import {mapStores} from 'pinia';

export default {
  name: 'FormDataEntry',
  props: {
    dataModel: {},
    initData: {},
    csNumber: '',
    formId: '',
  },
  components: {
    Form
  },
  data() {
    return {
      CONST_ID_SUFFIX: '_ID',
      CONST_COMMENT_SUFFIX: '_COMMENT',
      CONST_DATAGRID: 'datagrid',
      CONST_INTERVENTION_CHECKBOX_SUFFIX: '_intervention_checkbox',
      CONST_INTERVENTION_KEY_SUFFIX: '_intervention_datagrid',
      autoSaveData: {},
      autoSaveDataCandidate: {},
      saving: false,
      savingSuccess: false,
    }
  },
  mounted() {
    // reset the indicator
    this.savingSuccess = false;
    this.saving = false;
  },
  methods: {
    async autoSave() {
      console.log("autosave");
      //only start saving if previous saving is done
      //console.log("this.autoSaveDataCandidate: ", this.autoSaveDataCandidate);
      if (!this.saving && Object.keys(this.autoSaveDataCandidate).length > 0) {
        this.autoSaveData = JSON.parse(JSON.stringify(this.autoSaveDataCandidate));
        console.log("autosave data: ", this.autoSaveData);
        //console.log("this.autoSaveData: ", this.autoSaveData);
        //clear this.autoSaveDataCandidate, so we are not repeatedly saving it
        this.autoSaveDataCandidate = {};
        
        // Repeat the saving till it succeeds
       while(!this.savingSuccess) {
          try {
            this.saving = true;
            this.savingSuccess = true;
            this.saving = false;
            const [error, response] = await updateForm(this.csNumber, this.formId, this.autoSaveData);
            if (error) {
              console.error(error);
            } else {
              this.savingSuccess = true;
              this.saving = false;

              // Cache the response to the autosave store
              this.autosaveStore.addArray(response);
            }
          } catch (err) {
            console.error("Saving form data failed %o", err);
          } 
        }
      }
    },
    private_addToAutoSaveDataCandidate(isDataGrid, key, eventData, delayAutoSave) {
      //console.log("candidate before: ", this.autoSaveDataCandidate);
      // The key contains CONST_INTERVENTION_CHECKBOX_SUFFIX
      if (key.indexOf(this.CONST_INTERVENTION_CHECKBOX_SUFFIX) >= 0) {
        let questionKey = key.substr(0, 6);
        let interventionDatagridKey = questionKey + this.CONST_INTERVENTION_KEY_SUFFIX;
        // If the checkbox is unchecked, remove the associated intervention from autoSaveDataCandidate.
        // This triggers a full intervention deletion in the backend.
        if (!eventData[key]) {
          delete this.autoSaveDataCandidate[interventionDatagridKey];

          // Get questionID
          let key_questionID = questionKey + this.CONST_ID_SUFFIX;
          let questionID = this.privateGetQuestionID(key_questionID);
          if (questionID) {
            this.autoSaveDataCandidate[key_questionID] = questionID;
            if (!delayAutoSave) {
              this.autoSave();
            }
          } else {
            // We shouldn't get here, means the question hasn't been answered and user answered interventions.
            // do nothing
          }
        } else {
          // The checkbox is checked, add intervention data to autoSaveDataCandidate, and trigger autoSave
          this.autoSaveDataCandidate[interventionDatagridKey] = eventData[interventionDatagridKey];
          // Get questionID
          let key_questionID = questionKey + this.CONST_ID_SUFFIX;
          let questionID = this.privateGetQuestionID(key_questionID);
          if (questionID) {
            this.autoSaveDataCandidate[key_questionID] = questionID;
            if (!delayAutoSave) {
              this.autoSave();
            }
          }
        }
        return;
      }

      // User made an update to the dataGrid (interventions)
      if (isDataGrid) {
        this.autoSaveDataCandidate[key] = eventData[key];
        // Get questionID
        let questionKey = key.substr(0, 6);
        let key_questionID = questionKey + this.CONST_ID_SUFFIX;
        let questionID = this.privateGetQuestionID(key_questionID);
        if (questionID) {
          this.autoSaveDataCandidate[key_questionID] = questionID;
          if (!delayAutoSave) {
              this.autoSave();
            }
        } else {
          // We shouldn't get here, means the question hasn't been answered and user answered interventions.
          // do nothing 
        }
      } else {
        // User made an update to the question comments
        if (key.indexOf(this.CONST_COMMENT_SUFFIX) >= 0) {
          let questionKey = key.substr(0, 6);
          // Add question answer and comment to autoSaveDataCandidate
          this.autoSaveDataCandidate[questionKey] = eventData[questionKey];
          this.autoSaveDataCandidate[key] = eventData[key];

          let key_questionID = questionKey + this.CONST_ID_SUFFIX;
          let questionID = this.privateGetQuestionID(key_questionID);

          if (questionID) {
            this.autoSaveDataCandidate[key_questionID] = questionID;
          }
        } else {
          // User made an update to the question answer (e.g., select a different radio button)
          // Add question answer and comment to autoSaveDataCandidate
          this.autoSaveDataCandidate[key + this.CONST_COMMENT_SUFFIX] = eventData[key + this.CONST_COMMENT_SUFFIX];
          this.autoSaveDataCandidate[key] = eventData[key];

          let questionKey = key;
          let key_questionID = questionKey + this.CONST_ID_SUFFIX;
          let questionID = this.privateGetQuestionID(key_questionID);

          if (questionID) {
            this.autoSaveDataCandidate[key_questionID] = questionID;
          }
        }
      }
      console.log("candidate after: ", this.autoSaveDataCandidate);
    },
    privateGetQuestionID(questionKey): any {
      let questionID = questionKey + this.CONST_ID_SUFFIX;
      if (eventData[questionID]) {
        return eventData[questionID];
      }
      return this.autosaveStore.getValue(questionKey);
    },
    handleChangeEvent(event) {
      console.log("event: ", event);
      // datagrid either 'add intervention' or 'delete' icon is clicked
      if (   event.changed 
          && ( event.changed.component.type === "datagrid")) {
        //console.log("Datagrid changed: ", event.data, event.changed.component.key, event.changed.value);
        this.private_addToAutoSaveDataCandidate(true, event.changed.component.key, event.data, false);
      }
      if (   event.changed 
          && ( event.changed.component.type === "textfield"
            || event.changed.component.type === "textarea")) {
        console.log("textarea changed: ", event.data);
        //console.log("textfield or textarea changed: ", event, event.data, event.changed.component.key, event.changed.value);
        // don't trigger the autosave on every key stroke, keep the latest values for now.
        // Trigger the autosave when blur event occurs.
        let dataGridKey = this.private_isPartOfDatagrid(event.changed.instance);
        if (dataGridKey != null) {
          this.private_addToAutoSaveDataCandidate(true, dataGridKey, event.data, true);
        } else {
          this.private_addToAutoSaveDataCandidate(false, event.changed.component.key, event.data, true);
        }
      }

      // Trigger autosave
      if (   event.changed 
          && ( event.changed.component.type === "radio"
            || event.changed.component.type === "checkbox"
            || event.changed.component.type === "select")
          ) {
        //console.log("radio, checkbox or select changed: ", event.data, event.changed.component.key, event.changed.value);
        
        // if the radio button or checkbox or select is part of an datagrid, map the datagrid data to this.autoSaveDataCandidate
        let dataGridKey = this.private_isPartOfDatagrid(event.changed.instance);
        if (dataGridKey != null) {
          //console.log("it's part of a datagrid, instance: ,theKey: , newValue: ", event.data, event.changed.component.key, dataGridKey, event.changed.value);
          this.private_addToAutoSaveDataCandidate(true, dataGridKey, event.data, true);
        } else {
          this.private_addToAutoSaveDataCandidate(false, event.changed.component.key, event.data, true);
        }
      }
    },
    handleBlurEvent(event) {
      console.log("From blur event, this.autoSaveDataCandidate: ", this.autoSaveDataCandidate);
      this.autoSave();
    },
    private_isPartOfDatagrid(theInstance) {
      //console.log("check partof dataGrid: ", theInstance);
      let datagridKey = null;
      if (theInstance != null 
          && theInstance.parent != null 
          && theInstance.parent.parent != null 
          && theInstance.parent.parent.type === this.CONST_DATAGRID) {
        datagridKey = theInstance.parent.parent.key;
      } else if ( theInstance.parent.parent.parent != null 
          && theInstance.parent.parent.parent.type === this.CONST_DATAGRID) {
        datagridKey = theInstance.parent.parent.parent.key;
      }
      return datagridKey;
    }
  },
  computed: {
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore)
  },
}
</script>