<template>
  <div>
    <Form :key="pageKey" :form="dataModel" :submission="initData" 
      v-on:editGridSaveRow="handleEditGridSaveRowEvent"
      v-on:change="handleChangeEvent" 
      v-on:blur="handleBlurEvent" 
      @evt_save="handleSave" 
      @evt_cancel="handleCancelForm"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import { objectToString } from '@vue/shared';
import {updateForm} from "@/components/form.api";

export default {
  name: 'saraCmpForm',
  props: {
    dataModel: {},
    initData: {},
    dataMap: {},
    saveBtnLabel: '',
    notifySaveDraft: 0,
  },
  components: {
    Form
  },
  data() {
    return {
      CONST_KEY_SAVE_BTN: 'button_save',
      pageKey: 0,
      saveDraft: false,
    }
  },
  watch: {
    saveBtnLabel() {
      this.private_updateSaveBtnLabel();
    },
    notifySaveDraft() {
      let btn = this.private_getSaveBtn();
      if (btn != null) { 
        //console.log("Simulate the btn click: ", btn);
        this.saveDraft = true;
        btn.click(); 
      }
    }
  },
  methods: {
    async updateForm(formData) {
      let formId= this.$route.params.formID;
      const [error, response] = await updateForm(formData);
      if (error) {
        console.error("Form update failed: ", error);
      }
    },
    private_getSaveBtn() {
      let theBtn = null;
      let className = '[class*="' + this.CONST_KEY_SAVE_BTN + '"]';
      let thePanel = document.querySelector(className);
      if (thePanel != null) {
        let typeName = '[type=button]';
        theBtn = thePanel.querySelector(typeName);
      }
      return theBtn;
    },
    private_updateSaveBtnLabel() {
      let theBtn = this.private_getSaveBtn();
      if (theBtn != null && theBtn.childNodes != null && theBtn.childNodes[0] != null) {
        theBtn.childNodes[0].nodeValue = this.saveBtnLabel;
      }
    },
    handleEditGridSaveRowEvent(event) {
      if (event != null && event.row != null) {
        console.log("datagrid radio text list event data: ", this.initData.data, 
        event.component.key, event.row,
        event.row.radio, event.row.comments);

        let keys = Object.keys(event.row);
        //console.log("keys: ", keys);

        // look for the data key 
        if (keys != null && keys.length > 0) {
          for (let i = 0; i < keys.length; i++) {
            let dataKey = this.dataMap[event.component.key + "." + keys[i]];
            //console.log("dataKey index: ", event.component.key + "." + keys[i]);
            // found the data key, update the initData
            if (dataKey != null) {
              //console.log("dataKey: ", dataKey);
              this.initData.data[dataKey] = event.row[keys[i]];
            }
          }
        }
        
        // Refresh the view
        this.pageKey++;
      }
      
    },
    handleChangeEvent(event) {
      if (   event.changed 
          && ( event.changed.component.type === "textfield"
            || event.changed.component.type === "textarea")) {
        console.log("handleChangeEvent: ", event, event.changed.component.key);      
        this.triggerAutoSave = true;
      }

      // Trigger auto save
      if (   event.changed 
          && ( event.changed.component.type === "radio"
            || event.changed.component.type === "checkbox"
            || event.changed.component.type === "select")
          ) {
            console.log("AutoSave triggered: ", event, event.changed.component.key);
            
      }
    },
    handleBlurEvent(event) {
      if (this.triggerAutoSave) {
        console.log("AutoSave blur triggered: ", event);
        this.triggerAutoSave = false;
        
      }
    },
    handleSave(evt) {
      //console.log("save evt: ", evt);
      if (evt != null && evt.data != null) {
        if (this.saveDraft) {
          this.updateForm(evt.data);
          this.saveDraft = false;
        } else {
          // emit an event, saveContinueClicked with setting true to flag "continue to next section", to the parent, so parent knows it's time to save data
          this.$emit('saveContinueClicked', evt.data);
        }
      } 
    },
    handleCancelForm(evt) {
      //console.log("cancel evt: ", evt);
      // emit an event, cancelFormClicked, to the parent, so parent knows it's time to cancel form
      if (evt != null) {
        this.$emit('cancelFormClicked');
      }
    }
  }
}
</script>