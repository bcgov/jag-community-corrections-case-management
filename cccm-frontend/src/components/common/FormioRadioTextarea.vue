<template>
    <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import formTemplate from '@/components/common/templateRadioTextarea.json';

export default {
  name: 'FormioRadioTextarea',
  props: {
    dataModel: {}
  },
  data() {
    return {
      templateJSON: formTemplate,
      formJSON : {},
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormData()
  },
  methods: {
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templateJSON);

      // set radio btn component 
      tmpJSONStr = tmpJSONStr.replace('${label}', this.dataModel.questionLabel);
      tmpJSONStr = tmpJSONStr.replace('${key}', this.dataModel.key);
      tmpJSONStr = tmpJSONStr.replace('${defaultValue}', this.dataModel.defaultValue);

      // set textarea component
      tmpJSONStr = tmpJSONStr.replace('${label_key}', this.dataModel.comments.key);

      //Find index of radio component.    
      let tmpJSON = JSON.parse(tmpJSONStr);
      let objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'radio'));
      
      tmpJSON.components[0].components[objIndex].values = this.dataModel.values;

      this.formJSON = tmpJSON;
    }
  }
}
</script>