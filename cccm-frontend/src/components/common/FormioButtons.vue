<template>
    <Form :form="formJSON" 
      @evt_save="handleSave" 
      @evt_saveAndClose="handleSaveAndClose" 
      @evt_cancel="handleCancelForm" 
      @evt_print="handlePrint"
      :options="options" />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateButtons from '@/components/common/templateButtons.json';

export default {
  name: 'FormioButton',
  props: {
    buttonType: '',
    saveBtnLabel: '',
    options: {}
  },
  data() {
    return {
      CONST_KEY_SAVE_BTN: 'save_contine',
      templatePanel : templateButtons,
      formJSON : {},
    }
  },
  watch: {
    saveBtnLabel() {
      this.private_updateSaveBtnLabel();
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormData();
  },
  methods: {
    buildFormData() {
      if (this.buttonType == "formButton") {
        // set value for sidePanelSaveBtnGroupData
        this.dataModel = [
          {
              "action": "event",
              "event": "evt_save",
              "key": "save_contine",
              "label": "Save and Continue",
              "toggledLabel": "Submit",
              "theme": "primary"
          },
          {
              "action": "event",
              "event": "evt_cancel",
              "key": "add_cancel",
              "label": "Delete Form",
              "theme": "danger"
          }
        ];
      };

      if (this.buttonType == "sideButton") {
        // set value for sidePanelSaveBtnGroupData
        this.dataModel = [
          {
              "action": "event",
              "event": "evt_saveAndClose",
              "key": "add_saveDraft",
              "label": "Close",
              "theme": "primary"
          },
          {
              "action": "event",
              "event": "evt_print",
              "key": "add_print",
              "label": "Print Form",
              "theme": "primary"
          }
        ];
      }
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);

      if (this.dataModel != null && this.dataModel.length > 0) {
        tmpJSONStr = tmpJSONStr.replaceAll('${label_btn_primary}', this.dataModel[0].label);
        tmpJSONStr = tmpJSONStr.replaceAll('${key_btn_primary}', this.dataModel[0].key);
        tmpJSONStr = tmpJSONStr.replaceAll('${evt_btn_primary}', this.dataModel[0].event);
        tmpJSONStr = tmpJSONStr.replaceAll('${theme_primary}', this.dataModel[0].theme);

        tmpJSONStr = tmpJSONStr.replaceAll('${label_btn_other}', this.dataModel[1].label);
        tmpJSONStr = tmpJSONStr.replaceAll('${key_btn_other}', this.dataModel[1].key);
        tmpJSONStr = tmpJSONStr.replaceAll('${evt_btn_other}', this.dataModel[1].event);
        tmpJSONStr = tmpJSONStr.replaceAll('${theme_other}', this.dataModel[1].theme);
      }

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = JSON.parse(tmpJSONStr);
    },
    private_updateSaveBtnLabel() {
      let theBtn = this.private_getSaveBtn();
      if (theBtn != null && theBtn.childNodes != null && theBtn.childNodes[0] != null) {
        theBtn.childNodes[0].nodeValue = this.saveBtnLabel;
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
      //console.log("save btn: ", theBtn);
      return theBtn;
    },
    handleSave(evt) {
      // emit an event, saveContinueClicked with setting true to flag "continue to next section", to the parent, so parent knows it's time to save data
      if (evt != null && evt.type === this.dataModel[0].event ) {
        this.$emit('saveContinueClicked', true);
      } 
    },
    handleSaveAndClose(evt) {
      // emit an event, saveCloseClicked with setting false to flag "continue to next section", to the parent, so parent knows it's time to save data
      if (evt != null && evt.type === this.dataModel[0].event ) {
        this.$emit('saveCloseClicked', false);
      } 
    },
    handleCancelForm(evt) {
      // emit an event, cancelFormClicked, to the parent, so parent knows it's time to cancel form
      if (evt != null && evt.type === this.dataModel[1].event ) {
        this.$emit('cancelFormClicked');
      }
    },
    handlePrint(evt) {
      // emit an event, printFormClicked, to the parent, so parent knows it's time to cancel form
      if (evt != null && evt.type === this.dataModel[1].event ) {
        this.$emit('printFormClicked');
      } 
    }
  }
}
</script>