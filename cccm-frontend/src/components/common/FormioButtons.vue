<template>
    <!--evt_changeButtonLabe needs to match this.dataModel.button.event-->
    <Form :form="formJSON" @evt_save="handleSave" @evt_cancel="handleCancelForm" @evt_print="handlePrint"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateButtons from '@/components/common/templateButtons.json';

export default {
  name: 'FormioButton',
  props: {
    dataModel: {}
  },
  data() {
    return {
      templatePanel : templateButtons,
      formJSON : {},
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

      tmpJSONStr = tmpJSONStr.replaceAll('${label_btn_save}', this.dataModel[0].label);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_btn_save}', this.dataModel[0].key);
      tmpJSONStr = tmpJSONStr.replaceAll('${evt_btn_save}', this.dataModel[0].event);
      tmpJSONStr = tmpJSONStr.replaceAll('${theme_save}', this.dataModel[0].theme);

      tmpJSONStr = tmpJSONStr.replaceAll('${label_btn_other}', this.dataModel[1].label);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_btn_other}', this.dataModel[1].key);
      tmpJSONStr = tmpJSONStr.replaceAll('${evt_btn_other}', this.dataModel[1].event);
      tmpJSONStr = tmpJSONStr.replaceAll('${theme_other}', this.dataModel[1].theme);

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = JSON.parse(tmpJSONStr);
    },
    handleSave(evt) {
      // emit an event, saveContinueClicked, to the parent, so parent knows it's time to save data
      this.$emit('saveContinueClicked');
    },
    handleCancelForm(evt) {
      // emit an event, cancelFormClicked, to the parent, so parent knows it's time to cancel form
      this.$emit('cancelFormClicked');
    },
    handlePrint(evt) {
      // emit an event, printFormClicked, to the parent, so parent knows it's time to cancel form
      this.$emit('printFormClicked');
    }
  }
}
</script>