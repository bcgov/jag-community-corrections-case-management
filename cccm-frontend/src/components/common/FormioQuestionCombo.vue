<template>
  <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
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
    Form
  },
  mounted(){
    this.buildFormInfoDataEntry()
  },
  methods: {
    buildFormInfoDataEntry() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.questionComboTemplate);
      
      // set the label and key for the top component
      tmpJSONStr = tmpJSONStr.replace('${questionLabel}', this.dataModel.questionLabel);
      tmpJSONStr = tmpJSONStr.replace('${radiodefaultValue}', this.dataModel.radiodefaultValue);
      tmpJSONStr = tmpJSONStr.replace('${radiokey}', this.dataModel.radiokey);

      // Set intervention needed component
      tmpJSONStr = tmpJSONStr.replace('${key_itv_needed}', this.dataModel.interventionNeeded.key_yn);
      tmpJSONStr = tmpJSONStr.replace('${defaultValue_ivNeeded}', this.dataModel.interventionNeeded.defaultValue);
      tmpJSONStr = tmpJSONStr.replace('${key_itv_details}', "itv_" + this.dataModel.interventionNeeded.key_yn);
      tmpJSONStr = tmpJSONStr.replace('${key_itv_type}', this.dataModel.interventionNeeded.key_itv_type);

      tmpJSONStr = tmpJSONStr.replace('${key_itv_other}', this.dataModel.interventionNeeded.key_itv_other);
      tmpJSONStr = tmpJSONStr.replace('${value_itv_other}', this.dataModel.interventionNeeded.value_itv_other);

      tmpJSONStr = tmpJSONStr.replace('${key_itv_description}', this.dataModel.interventionNeeded.key_itv_description);
      tmpJSONStr = tmpJSONStr.replace('${value_itv_description}', this.dataModel.interventionNeeded.value_itv_description);
      
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
      
      // Set intervention needed type DDL
      objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'well'));
      tmpJSON.components[0].components[objIndex].components[0].columns[0].components[0].data.values = this.dataModel.interventionNeeded.itv_type_data.values;
      
      // build button
      if (this.dataModel.interventionNeeded.button != null){
        let btnComponent = JSON.parse("{}");
        btnComponent.type = "button";
        btnComponent.action = this.dataModel.interventionNeeded.button.action;
        btnComponent.key = this.dataModel.interventionNeeded.button.key;
        btnComponent.label = this.dataModel.interventionNeeded.button.label;
        btnComponent.theme = this.dataModel.interventionNeeded.button.theme;
        tmpJSON.components[0].components[objIndex].components[2] = btnComponent;
      }

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = tmpJSON;
    }
  },
}
</script>