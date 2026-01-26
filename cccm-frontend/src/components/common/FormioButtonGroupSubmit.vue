<template>
    <Form
      v-if="isFormReady"
      :key="formKey"
      :form="formJSON"
      @evt_save="handleSave" 
      @evt_cancel="handleCancelForm" 
      :submission="dataModel" />
</template>

<script lang="ts">
import { Form } from '@formio/vue';
import templateButtons from '@/components/common/templateButtonGroupSubmit.json';

export default {
  name: 'FormioSubmitButton',
  props: {
    saveBtnLabel: '',
    dataModel: {},
    submitDone: 0,
  },
  data() {
    return {
      CONST_KEY_SAVE_BTN: 'save_continue',
      templatePanel : templateButtons,
      formJSON : {},
      formKey: 0,
    }
  },
  watch: {
    saveBtnLabel() {
      this.private_updateSaveBtnLabel();
    },
    submitDone() {
      let btn = this.private_getSaveBtn();
      if (btn != null) {
        // Saving completes, enable the button; and reset the button label
        btn.disabled = false;
        if (btn != null && btn.childNodes != null && btn.childNodes[0] != null) {
          btn.childNodes[0].nodeValue = this.saveBtnLabel;
        }
      }
    },
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormData();
  },
  computed: {
    isFormReady() {
      return !!(this.formJSON && this.formJSON.components && this.formJSON.components.length);
    }
  },
  methods: {
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);
      this.formJSON = JSON.parse(tmpJSONStr);
      this.formKey++;
    },
    private_updateSaveBtnLabel() {
      let btn = this.private_getSaveBtn();
      if (btn != null && btn.childNodes != null && btn.childNodes[0] != null) {
        btn.childNodes[0].nodeValue = this.saveBtnLabel;
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
    handleSave(evt) {
      // emit an event, saveContinueClicked with setting true to flag "continue to next section", to the parent, so parent knows it's time to save data
      if (evt != null && evt.type === 'evt_save' ) {
        if (this.saveBtnLabel == this.$BUTTON_TEXT_SUBMIT) {
          let btn = this.private_getSaveBtn();
          if (btn != null) {
            this.$emit('saveContinueClicked', true);
            // Prevent double clicking by disable the btn, also change the btn label to 'Saving ...'
            // The btn will be enabled after form submission completes (either failed or succeeded)
            btn.disabled = true;
            if (btn != null && btn.childNodes != null && btn.childNodes[0] != null) {
              btn.childNodes[0].nodeValue = 'Saving ...';
            }
          }
        } else {
          this.$emit('saveContinueClicked', true);
        }
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