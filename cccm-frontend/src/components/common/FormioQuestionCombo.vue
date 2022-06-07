<template>
  <div>
    <Form :form="formJSON"/>
    <FormioIntervention :dataModel="dataModel.interventionNeeded"/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import FormioIntervention from "@/components/common/FormioIntervention.vue";
import templateQuestionCombo from '@/components/common/templateQuestionCombo.json';

export default {
  name: 'FormioQuestionCombo',
  props: {
    dataModel: {}
  },
  data() {
    return {
      questionComboTemplate : templateQuestionCombo,
      formJSON : {},
    }
  },
  components: {
    Form,
    FormioIntervention,
  },
  mounted(){
    this.buildFormInfoDataEntry();
  },
  methods: {
    buildFormInfoDataEntry() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.questionComboTemplate);
      
      // set the label and key for the top component
      tmpJSONStr = tmpJSONStr.replace('${questionLabel}', this.dataModel.questionLabel);
      tmpJSONStr = tmpJSONStr.replace('${radiodefaultValue}', this.dataModel.radiodefaultValue);
      tmpJSONStr = tmpJSONStr.replace('${radiokey}', this.dataModel.radiokey);

      //Set the radio component. 
      let tmpJSON = JSON.parse(tmpJSONStr);   
      let objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'radio'));
      
      tmpJSON.components[0].components[objIndex].label = this.dataModel.questionLabel;
      tmpJSON.components[0].components[objIndex].values = this.dataModel.values;
      tmpJSON.components[0].components[objIndex].key = this.dataModel.key;
      tmpJSON.components[0].components[objIndex].defaultValue = this.dataModel.defaultValue;

      // Set comments component
      objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'textarea'));
      tmpJSON.components[0].components[objIndex].key = this.dataModel.comments.key;
      
      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = tmpJSON;
    }
  }
}
</script>