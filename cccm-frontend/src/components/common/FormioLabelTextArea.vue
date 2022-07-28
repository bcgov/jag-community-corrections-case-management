<template>
    <Form v-on:change="handleChangeEvent" v-on:blur="handleBlurEvent" :submission="initData" :form="formJSON" @evt_submitBtnClicked="handleSubmit"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateLabelTextArea from '@/components/common/templateLabelTextArea.json';

export default {
  name: 'FormioLabelTextarea',
  props: {
    dataModel: {},
    initData: {},
    uiType: "",
    // param passed from parent to indicate time to save data
    notifySavingData: {
      type: Number,
      default: 1,
    }
  },
  data() {
    return {
      templateLabelTextArea : templateLabelTextArea,
      formJSON : {},
      triggerAutoSave: false,
    }
  },
  watch: {
    notifySavingData() {
      // Submit the form by simulating clicking the submit button
      let btn = document.getElementById(this.dataModel.key);
      if (btn != null) { 
        //console.log("Simulate the btn click: ", btn);
        btn.click(); 
      }
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormData()
  },
  methods: {
    handleSubmit(evt) {
      // emit an event, dataSubmitted, to the parent, so parent knows form data
      if (evt.data != null) {
        //console.log("child data submitted: ", evt.data.hidden_key, evt.data);
        this.$emit('dataSubmitted', evt.data);
      }
      
    },
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templateLabelTextArea);

      tmpJSONStr = tmpJSONStr.replaceAll('${key}', this.dataModel.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${label}', this.dataModel.label);
      tmpJSONStr = tmpJSONStr.replaceAll('${label_textarea}', this.dataModel.label_textarea);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_textarea}', this.dataModel.key_textarea);
      tmpJSONStr = tmpJSONStr.replaceAll('${className_label}', this.dataModel.className_label);
      tmpJSONStr = tmpJSONStr.replaceAll('${maxLength_textarea}', this.dataModel.maxLength_textarea);
      tmpJSONStr = tmpJSONStr.replaceAll('${defaultValue}', this.dataModel.initData.data.key_textarea);
      tmpJSONStr = tmpJSONStr.replaceAll('${value_hiddenKey}', this.dataModel.key);

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = JSON.parse(tmpJSONStr);
    },
    handleChangeEvent(event) {
      // emit an event, dataOnChanged, to the parent, so parent knows the changes
      if (event.changed && event.changed.component.key === this.dataModel.key_textarea) {
        this.triggerAutoSave = true;
      }
    },
    handleBlurEvent(event) {
      if (this.triggerAutoSave) {
        //console.log("AutoSave blur triggered: ", event._data);
        this.triggerAutoSave = false;
        this.$emit('dataOnChanged', this.uiType, event._data, this.dataModel.key);
      }
    }
  }
}
</script>