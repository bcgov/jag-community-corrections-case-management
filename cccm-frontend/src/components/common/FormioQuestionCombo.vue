<template>
  <div>
    <Form :form="formJSON" v-on:change="handleChangeEvent" :submission="initData"/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateQuestionCombo from '@/components/common/templateQuestionCombo.json';

export default {
  name: 'FormioQuestionCombo',
  props: {
    dataModel: {},
    initData: {},
  },
  data() {
    return {
      questionComboTemplate : templateQuestionCombo,
      formJSON : {},
      userInput: {}
    }
  },
  components: {
    Form,
  },
  mounted(){
    this.buildFormInfoDataEntry();
    //console.log("initData: ", this.initData);
  },
  methods: {
    buildFormInfoDataEntry() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.questionComboTemplate);
      
      // set the label and key for the top component
      tmpJSONStr = tmpJSONStr.replaceAll('${questionLabel}', this.dataModel.questionLabel);
      tmpJSONStr = tmpJSONStr.replaceAll('${defaultValue_radioButton}', this.dataModel.radioGroup.defaultValue);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_radioButton}', this.dataModel.radioGroup.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${value_hiddenKey}', this.dataModel.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_comments}', this.dataModel.comments.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${defaultValue_comments}', this.dataModel.comments.defaultValue);

      // Set intervention needed component
      tmpJSONStr = tmpJSONStr.replaceAll('${label}', this.dataModel.interventionNeeded.label);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_checkbox}', this.dataModel.interventionNeeded.key_checkbox);
      tmpJSONStr = tmpJSONStr.replaceAll('${defaultValue_checkbox}', this.dataModel.interventionNeeded.defaultValue);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_itvDataGrid}', this.dataModel.interventionNeeded.key_itvDataGrid);

      // Set intervention needed component
      tmpJSONStr = tmpJSONStr.replaceAll('${key_itv_type}', this.dataModel.interventionNeeded.key_itv_type);

      tmpJSONStr = tmpJSONStr.replaceAll('${key_itv_other}', this.dataModel.interventionNeeded.key_itv_other);
      tmpJSONStr = tmpJSONStr.replaceAll('${value_itv_other}', this.dataModel.interventionNeeded.value_itv_other);

      tmpJSONStr = tmpJSONStr.replaceAll('${key_itv_description}', this.dataModel.interventionNeeded.key_itv_description);
      tmpJSONStr = tmpJSONStr.replaceAll('${value_itv_description}', this.dataModel.interventionNeeded.value_itv_description);
    
      let tmpJSON = JSON.parse(tmpJSONStr);  

      //Set the radio data. 
      tmpJSON.components[0].components[0].values = this.dataModel.radioGroup.values;

      // Set intervention needed type DDL
      tmpJSON.components[0].components[4].components[0].components[0].columns[0].components[0].data.values = this.dataModel.interventionNeeded.itv_type_data.values;

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = tmpJSON;
    },
    handleChangeEvent(event) {
      // emit an event, dataOnChanged, to the parent, so parent knows the changes
      if (   event.changed 
          && ( event.changed.component.key === this.dataModel.comments.key  
            || event.changed.component.key === this.dataModel.interventionNeeded.key_checkbox 
            || event.changed.component.key === this.dataModel.interventionNeeded.key_itv_type
            || event.changed.component.key === this.dataModel.interventionNeeded.key_itv_other
            || event.changed.component.key === this.dataModel.interventionNeeded.key_itv_description)
          ) {
        this.$emit('dataOnChanged', event.data);
      }
    }
  }
}
</script>