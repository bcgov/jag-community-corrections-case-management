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
import { updateForm } from "@/components/form.api";
import { useStore } from "@/stores/autoSaveStore";
import { mapStores } from 'pinia';

export default {
  name: 'FormDataEntry',
  props: {
    dataModel: {},
    initData: {},
    csNumber: '',
    formId: '',
    timeForValidate: {
      type: Number,
      default: 1,
    },
  },
  components: {
    Form
  },
  watch: {
    timeForValidate() {
      // get event.data, emit dataCollectedForValidate event to parent
      let fullData = {};
      this.$emit('dataCollectedForValidate', fullData);
    }
  },
  data() {
    return {
      CONST_MAX_RETRY: 5,
      CONST_ID_SUFFIX: '_ID',
      CONST_COMMENT_SUFFIX: '_COMMENT',
      CONST_DATAGRID: 'datagrid',
      CONST_INTERVENTION_CHECKBOX_SUFFIX: '_intervention_checkbox',
      CONST_INTERVENTION_KEY_SUFFIX: '_intervention_datagrid',
      CONST_INTERVENTION_DESC_SUFFIX : '_intervention_desc',
      CONST_INTERVENTION_TYPE_SUFFIX : '_intervention_type',
      CONST_INTERVENTION_ID_SUFFIX :'_intervention_id',
      autoSaveData: {},
      autoSaveDataCandidate: {},
      saving: false,
    }
  },
  mounted() {
    // reset the indicator
    this.saving = false;
  },
  methods: {
    async autoSave() {
      //only start saving if previous saving is done
      if (!this.saving && Object.keys(this.autoSaveDataCandidate).length > 0) {
        // deep copy of autoSaveDataCandidate, and assign it to autoSaveData 
        this.autoSaveData = JSON.parse(JSON.stringify(this.autoSaveDataCandidate));
        //console.log("autosave: ", this.autoSaveData);

        //clear this.autoSaveDataCandidate, so we are not repeatedly saving it
        this.autoSaveDataCandidate = {};
        
        // Repeat the saving till it succeeds
        let savingCnt = 0;
        while(savingCnt < this.CONST_MAX_RETRY) {
          try {
            savingCnt++;
            this.saving = true;
            
            const [error, response] = await updateForm(this.csNumber, this.formId, this.autoSaveData);
            if (error) {
              console.error(error);
            } else {
              this.saving = false;
              // Cache the response to the autosave store
              this.autosaveStore.addArray(response);
              break;
            }
          } catch (err) {
            console.error("Saving form data failed %o", err);
          } 
        }
        // Saving failed after CONST_MAX_RETRY where this.saving flag didn't set to true, do the following:
        // 1. Add payload back to the autoSaveDataCandidate, so we don't miss out data saving.
        // 2. Set the this.saving flag to false so autosave can continue
        if (this.saving) {
          this.private_mergePayload(this.autoSaveData);
          this.saving = false;
          console.error("Auto save failed after " + this.CONST_MAX_RETRY + "times");
        }
      }
    },
    private_mergePayload(source) {
      for (let i = 0; i < Object.keys(source); i++) {
        let key = Object.keys(source)[i];
        if (this.autoSaveDataCandidate[key] == null) {
          this.autoSaveDataCandidate[key] = source[key];
        }
      }
    },
    private_buildBlankInterventionArray(questionKey) {
      let emptyItvArray = [];
      let emptyItv = {};
      emptyItv[questionKey + this.CONST_INTERVENTION_DESC_SUFFIX] = "";
      emptyItv[questionKey + this.CONST_INTERVENTION_TYPE_SUFFIX] = "";
      emptyItv[questionKey + this.CONST_INTERVENTION_ID_SUFFIX] = "";
      emptyItvArray[0] = emptyItv;
      return emptyItvArray;
    },
    private_addToAutoSaveDataCandidate(isDataGrid, key, eventData, autoSaveNow) {
      // The key contains CONST_INTERVENTION_CHECKBOX_SUFFIX
      if (key.indexOf(this.CONST_INTERVENTION_CHECKBOX_SUFFIX) >= 0) {
        let questionKey = key.substr(0, 6);
        let interventionDatagridKey = questionKey + this.CONST_INTERVENTION_KEY_SUFFIX;
        // If the checkbox is unchecked, remove the associated intervention from autoSaveDataCandidate.
        // This triggers a full intervention deletion in the backend.
        if (!eventData[key]) {
          // insert an empty intervention json object which will trigger the all deletion
          this.autoSaveDataCandidate[interventionDatagridKey] = this.private_buildBlankInterventionArray(questionKey);

          // Get clientFormAnswerID
          let key_clientFormAnswerID = questionKey + this.CONST_ID_SUFFIX;
          let clientFormAnswerID = this.privateGetClientFormAnswerID(questionKey, eventData);
          if (clientFormAnswerID) {
            //console.log("found clientFormAnswerID: ", clientFormAnswerID);
            this.autoSaveDataCandidate[key_clientFormAnswerID] = clientFormAnswerID;
            this.autoSave();
          } else {
            //console.log("not found clientFormAnswerID: ", clientFormAnswerID);
            // We shouldn't get here, means the question hasn't been answered and user answered interventions.
            // Add question and comment to the autoSaveDataCandidate, this is a heavy save
            this.autoSaveDataCandidate[questionKey] = eventData[questionKey];
            this.autoSaveDataCandidate[questionKey + this.CONST_COMMENT_SUFFIX] = eventData[questionKey + this.CONST_COMMENT_SUFFIX];
            this.autoSave();
          }
        } 
        return;
      }

      // User made an update to the dataGrid (interventions)
      if (isDataGrid) {
        this.autoSaveDataCandidate[key] = eventData[key];
        // Get clientFormAnswerID
        let questionKey = key.substr(0, 6);
        let key_clientFormAnswerID = questionKey + this.CONST_ID_SUFFIX;
        let clientFormAnswerID = this.privateGetClientFormAnswerID(questionKey, eventData);
        //console.log("key_clientFormAnswerID: , clientFormAnswerID: ", key_clientFormAnswerID, clientFormAnswerID);
        if (clientFormAnswerID) {
          this.autoSaveDataCandidate[key_clientFormAnswerID] = clientFormAnswerID;
          if (autoSaveNow) {
              this.autoSave();
            }
        } else {
          // We shouldn't get here, means the question hasn't been answered and user answered interventions.
          // Add question and comment to the autoSaveDataCandidate, this is a heavy save
          this.autoSaveDataCandidate[questionKey] = eventData[questionKey];
          this.autoSaveDataCandidate[questionKey + this.CONST_COMMENT_SUFFIX] = eventData[questionKey + this.CONST_COMMENT_SUFFIX];
          if (autoSaveNow) {
            this.autoSave();
          }
        }
      } else {
        // User made an update to the question comments
        if (key.indexOf(this.CONST_COMMENT_SUFFIX) >= 0) {
          let questionKey = key.substr(0, 6);
          // Add question answer and comment to autoSaveDataCandidate
          this.autoSaveDataCandidate[questionKey] = eventData[questionKey];
          this.autoSaveDataCandidate[key] = eventData[key];

          let key_clientFormAnswerID = questionKey + this.CONST_ID_SUFFIX;
          let clientFormAnswerID = this.privateGetClientFormAnswerID(questionKey, eventData);

          if (clientFormAnswerID) {
            this.autoSaveDataCandidate[key_clientFormAnswerID] = clientFormAnswerID;
          }
        } else {
          // User made an update to the question answer (e.g., select a different radio button)
          // Add question answer and comment to autoSaveDataCandidate
          this.autoSaveDataCandidate[key + this.CONST_COMMENT_SUFFIX] = eventData[key + this.CONST_COMMENT_SUFFIX];
          this.autoSaveDataCandidate[key] = eventData[key];

          let questionKey = key;
          let key_clientFormAnswerID = questionKey + this.CONST_ID_SUFFIX;
          let clientFormAnswerID = this.privateGetClientFormAnswerID(questionKey, eventData);

          if (clientFormAnswerID) {
            this.autoSaveDataCandidate[key_clientFormAnswerID] = clientFormAnswerID;
          }
        }
      }
    },
    privateGetClientFormAnswerID(questionKey, eventData) {
      let clientFormAnswerID = questionKey + this.CONST_ID_SUFFIX;
      if (eventData[clientFormAnswerID]) {
        return eventData[clientFormAnswerID];
      }
      return this.autosaveStore.getValue(questionKey);
    },
    handleChangeEvent(event) {
      // datagrid either 'add intervention' or 'delete' icon is clicked
      if (   event.changed 
          && ( event.changed.component.type === "datagrid")) {
        // only record data to autoSaveDataCandidate if 'delete' icon is clicked
        if (event.changed.flags.isReordered) {
          this.private_addToAutoSaveDataCandidate(true, event.changed.component.key, event.data, true);
        }
      }
      if (   event.changed 
          && ( event.changed.component.type === "textfield"
            || event.changed.component.type === "textarea")) {
        //console.log("textfield or textarea changed: ", event, event.data, event.changed.component.key, event.changed.value);
        // don't trigger the autosave on every key stroke, keep the latest values for now.
        // Trigger the autosave when blur event occurs.
        let dataGridKey = this.private_isPartOfDatagrid(event.changed.instance);
        if (dataGridKey != null) {
          this.private_addToAutoSaveDataCandidate(true, dataGridKey, event.data, false);
        } else {
          this.private_addToAutoSaveDataCandidate(false, event.changed.component.key, event.data, false);
        }
      }

      // Trigger autosave
      if (   event.changed 
          && ( event.changed.component.type === "radio"
            || event.changed.component.type === "checkbox"
            || event.changed.component.type === "select")
          ) {
        // if the radio button or checkbox or select is part of an datagrid, map the datagrid data to this.autoSaveDataCandidate
        let dataGridKey = this.private_isPartOfDatagrid(event.changed.instance);
        if (dataGridKey != null) {
          //console.log("it's part of a datagrid, instance: ,theKey: , newValue: ", event.data, event.changed.component.key, dataGridKey, event.changed.value);
          this.private_addToAutoSaveDataCandidate(true, dataGridKey, event.data, true);
        } else {
          this.private_addToAutoSaveDataCandidate(false, event.changed.component.key, event.data, false);
        }
      }
    },
    handleBlurEvent(event) {
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