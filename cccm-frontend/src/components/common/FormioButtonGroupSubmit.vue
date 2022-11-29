<template>
    <Form :form="formJSON" 
      @evt_save="handleSave" 
      @evt_cancel="handleCancelForm" 
      :options="options" />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateButtons from '@/components/common/templateButtonGroupSubmit.json';

export default {
  name: 'FormioSubmitButton',
  props: {
    saveBtnLabel: '',
    options: {}
  },
  data() {
    return {
      CONST_KEY_SAVE_BTN: 'save_continue',
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
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);
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
      if (evt != null && evt.type === 'evt_save' ) {
        this.$emit('saveContinueClicked', true);
      } 
    },
    handleCancelForm(evt) {
      // emit an event, cancelFormClicked, to the parent, so parent knows it's time to cancel form
      if (evt != null && evt.type === 'evt_cancel' ) {
        this.$emit('cancelFormClicked');
      }
    }
  }
}
</script>