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
      CONST_COMMENT_SUFIX: '_COMMENT',
      CONST_DATAGRID: 'datagrid',
      CONST_INTERVENTION_CHECKBOX_SUFIX: '_intervention_checkbox',
      CONST_INTERVENTION_KEY_SUFFIX: '_intervention_datagrid',
      saveDraft: false,
      latestKey: '',
      latestValue: '',
      latestData: {},
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
      console.log("this.autoSaveDataCandidate: ", this.autoSaveDataCandidate);
      if (!this.saving && Object.keys(this.autoSaveDataCandidate).length > 0) {
        this.autoSaveData = JSON.parse(JSON.stringify(this.autoSaveDataCandidate));
        console.log("autosave data: ", this.autoSaveData);
        console.log("this.autoSaveData: ", this.autoSaveData);
        //clear this.autoSaveDataCandidate, so we are not repeated saving it
        this.autoSaveDataCandidate = {};
        
        // Repeat the saving till it succeeds
       //while(!this.savingSuccess) {
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
            }
          } catch (err) {
            console.error("Saving failed %o", err);
          } 
        //}
      }
    },
    private_addToAutoSaveDataCandidate(isDataGrid, key, eventData) {
      console.log("candidate before: ", this.autoSaveDataCandidate);
      let questionSequence = key;

      // ignore if the key contains CONST_INTERVENTION_CHECKBOX_SUFIX
      if (key.indexOf(this.CONST_INTERVENTION_CHECKBOX_SUFIX) >= 0) {
        let questionKey = key.substr(0, 6);
        let interventionDatagridKey = questionKey + this.CONST_INTERVENTION_KEY_SUFFIX;
        // if the checkbox is unchecked, remove the associated intervention
        if (!eventData[key]) {
          delete this.autoSaveDataCandidate[interventionDatagridKey];
          // need to include the question in the payload to keep API happy
          this.autoSaveDataCandidate[questionKey + this.CONST_COMMENT_SUFIX] = eventData[questionKey + this.CONST_COMMENT_SUFIX];
          this.autoSaveDataCandidate[questionKey] = eventData[questionKey];
        }
        return;
      }
      if (isDataGrid) {
        this.autoSaveDataCandidate[key] = eventData[key];
        // need to include the question in the payload to keep API happy
        let questionKey = key.substr(0, 6);
        this.autoSaveDataCandidate[questionKey + this.CONST_COMMENT_SUFIX] = eventData[questionKey + this.CONST_COMMENT_SUFIX];
        this.autoSaveDataCandidate[questionKey] = eventData[questionKey];
      } else {
        //if key contain _COMMENT, get the question sequence
        if (key.indexOf(this.CONST_COMMENT_SUFIX) >= 0) {
          questionSequence = key.substr(0, 6);
          this.autoSaveDataCandidate[questionSequence] = eventData[questionSequence];
          this.autoSaveDataCandidate[key] = eventData[key];
        } else {
          this.autoSaveDataCandidate[key + this.CONST_COMMENT_SUFIX] = eventData[key + this.CONST_COMMENT_SUFIX];
          this.autoSaveDataCandidate[key] = eventData[key];
        }
      }
      console.log("candidate after: ", this.autoSaveDataCandidate);
    },
    handleChangeEvent(event) {
      // datagrid either 'add intervention' or 'delete' icon is clicked
      if (   event.changed 
          && ( event.changed.component.type === "datagrid")) {
        console.log("Datagrid changed: ", event.data, event.changed.component.key, event.changed.value);
        this.private_addToAutoSaveDataCandidate(true, event.changed.component.key, event.data);
      }
      if (   event.changed 
          && ( event.changed.component.type === "textfield"
            || event.changed.component.type === "textarea")) {
        //console.log("textfield or textarea changed: ", event, event.data, event.changed.component.key, event.changed.value);
        // don't trigger the autosave on every key stroke, keep the latest values for now.
        // Trigger the autosave when blur event occurs.
        let dataGridKey = this.private_isPartOfDatagrid(event.changed.instance);
        if (dataGridKey != null) {
          this.private_addToAutoSaveDataCandidate(true, dataGridKey, event.data);
        } else {
          this.private_addToAutoSaveDataCandidate(false, event.changed.component.key, event.data);
        }
          this.triggerAutoSave = true;
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
          this.private_addToAutoSaveDataCandidate(true, dataGridKey, event.data);
        } else {
          this.private_addToAutoSaveDataCandidate(false, event.changed.component.key, event.data);
        }
      }
    },
    handleBlurEvent(event) {
      console.log("From blur event, this.autoSaveDataCandidate: ", this.autoSaveDataCandidate);
      this.autoSave();
      // if (this.triggerAutoSave) {
      //   this.triggerAutoSave = false;
      //   this.autoSave();
      // }
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
  }
}
</script>