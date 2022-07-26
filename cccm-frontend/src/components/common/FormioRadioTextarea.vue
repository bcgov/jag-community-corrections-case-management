<template>
    <Form v-on:change="handleChangeEvent" :form="formJSON" :submission="initData" @evt_submitBtnClicked="handleSubmit"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import formTemplate from '@/components/common/templateRadioTextarea.json';

export default {
  name: 'FormioRadioTextarea',
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
      let tmpJSONStr = JSON.stringify(this.templateJSON);

      // set radio btn component 
      tmpJSONStr = tmpJSONStr.replaceAll('${label_questionLabel}', this.dataModel.questionLabel);
      tmpJSONStr = tmpJSONStr.replaceAll('${key}', this.dataModel.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_radioButton}', this.dataModel.key_radioButton);
      tmpJSONStr = tmpJSONStr.replaceAll('${defaultValue}', this.dataModel.initData.data.key_radioButton);
      tmpJSONStr = tmpJSONStr.replaceAll('${value_hiddenKey}', this.dataModel.key);
      
      // set textarea component
      tmpJSONStr = tmpJSONStr.replaceAll('${key_textarea}', this.dataModel.key_comments);
      tmpJSONStr = tmpJSONStr.replaceAll('${defaultValue_textarea}', this.dataModel.initData.data.key_comments);

      //Find index of radio component.    
      let tmpJSON = JSON.parse(tmpJSONStr);
      tmpJSON.components[0].components[0].values = this.dataModel.values;

      this.formJSON = tmpJSON;
    },
    handleChangeEvent(event) {
      // emit an event, dataOnChanged, to the parent, so parent knows the changes
      if ( event.changed 
        && (  event.changed.component.key === this.dataModel.key_radioButton
          ||  event.changed.component.key === this.dataModel.key_comments)
        ) {
        //console.log("Formio radioTextarea: ", event);
        this.$emit('dataOnChanged', event.data);
      }
    }
  }
}
</script>