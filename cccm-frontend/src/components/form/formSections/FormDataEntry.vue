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
  watch: {
    initData() {
      console.log("initData updated: ", this.initData);
    }
  },
  data() {
    return {
      CONST_EDITGRID: 'editgrid',
      CONST_DATAGRID: 'datagrid',
      CONST_INTERVENTIONDESCRIPTION: 'interventionDescription',
      CONST_INTERVENTIONTYPE: 'interventionType',
      CONST_INTERVENTIONTYPEOTHER: 'interventionTypeOther',
      saveDraft: false,
      latestKey: '',
      latestValue: '',
      latestData: {},
      autoSaveData: {}
    }
  },
  mounted() {
    this.debounce((changeEvent) => this.autoSave(), 1000);
  },
  methods: {
    debounce(func, timeout = 300) {
      let timer;
      return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => {
          func.apply(this, args);
        }, timeout);
      };
    },
    async autoSave() {
      console.log("autosave: ", this.autoSaveData);
      try {
        const [error, response] = await updateForm(this.csNumber, this.formId, this.autoSaveData);
        if (error) {
          console.error(error);
          // this.errorOccurred = true;
          // this.errorText = "Failed to save form: " + err;
        } 
      } catch (err) {
        console.error("Saving failed %o", err);
      } finally {
        //this.saving = false;
      }
      //clear the autoSaveData
      this.autoSaveData = {};
    },
    handleChangeEvent(event) {
      // datagrid either 'add intervention' or 'delete' icon is clicked
      if (   event.changed 
          && ( event.changed.component.type === "datagrid")) {
        console.log("Datagrid changed: ", event, event.changed.component.key);
          
      }
      if (   event.changed 
          && ( event.changed.component.type === "textfield"
            || event.changed.component.type === "textarea")) {
        console.log("textfield or textarea changed: ", event.data, event.changed.component.key, event.changed.value);
        // don't trigger the autosave on every key stroke, keep the latest values for now.
        // Trigger the autosave when blur event occurs.
        this.latestKey = event.changed.component.key;
        this.latestValue = event.changed.value;  
        this.latestData = event.data;  
        this.triggerAutoSave = true;
      }

      // Trigger autosave
      if (   event.changed 
          && ( event.changed.component.type === "radio"
            || event.changed.component.type === "checkbox"
            || event.changed.component.type === "select")
          ) {
        console.log("radio, checkbox or select changed: ", event.data, event.changed.component.key, event.changed.value);

        // if the radio button or checkbox or select is NOT part of an editgrid, call dataMapping function
        let dataGridKey = this.private_isPartOfDatagrid(event.changed.instance);
        if (dataGridKey != null) {
          console.log("it's part of a datagrid, instance: ,theKey: , newValue: ", event.data, event.changed.component.key, dataGridKey, event.changed.value);
        }
      }
    },
    handleBlurEvent(event) {
      console.log("latestKey, latestVal, latestData, ", this.latestKey, this.latestValue, this.latestData);
      if (this.triggerAutoSave) {
        this.triggerAutoSave = false
      }
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