<template>
    <Form v-on:change="handleChangeEvent" :submission="initData" :form="formJSON" @evt_submitBtnClicked="handleSubmit"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import formTemplate from '@/components/common/templateRadio.json';

export default {
  name: 'FormioRadio',
  props: {
    dataModel: {},
    initData: {},
    // param passed from parent to indicate time to save data
    notifySavingData: {
      type: Number,
      default: 1,
    },
  },
  data() {
    return {
      templateJSON: formTemplate,
      formJSON : {},
    }
  },
  watch: {
    notifySavingData() {
      // Submit the form by simulating clicking the submit button
      let btn = document.getElementById(this.dataModel.key);
      if (btn != null) { 
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
      let tmpJSONStr = JSON.stringify(this.templateJSON);

      tmpJSONStr = tmpJSONStr.replaceAll('${label}', this.dataModel.questionLabel);
      tmpJSONStr = tmpJSONStr.replaceAll('${key}', this.dataModel.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_radioButton}', this.dataModel.key_radioButton);
      tmpJSONStr = tmpJSONStr.replaceAll('${defaultValue}', this.dataModel.initData.data.key_radioButton);
      tmpJSONStr = tmpJSONStr.replaceAll('${value_hiddenKey}', this.dataModel.key);

      
      //Find index of radio component.    
      let tmpJSON = JSON.parse(tmpJSONStr);
      tmpJSON.components[0].components[0].values = this.dataModel.values;

      this.formJSON = tmpJSON;
    },
    handleChangeEvent(event) {
      // emit an event, dataOnChanged, to the parent, so parent knows the changes
      if (event.changed && event.changed.component.key === this.dataModel.key_radioButton) {
        //console.log("Formio Radio changed: ", event);
        this.$emit('dataOnChanged', event.data, this.dataModel.key);
      }
    }
  }
}
</script>