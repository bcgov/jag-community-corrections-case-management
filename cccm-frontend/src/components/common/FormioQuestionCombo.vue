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
    setTimeout(() => {
      this.showHideInterventionPanels();
    }, 1000);
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
    },
    showHideInterventionPanels() {
      // if the checkBox is check, show details
      let checkBoxID = this.dataModel.interventionNeeded.key_yn;
      let theCB = document.getElementById(checkBoxID);
      if (theCB != null) {
        let itvPanelID = "panel_" + this.dataModel.interventionNeeded.key_yn;
        let itvPanel = document.getElementById(itvPanelID);
        if (itvPanel != null) {
          if (theCB.checked) {
            itvPanel.setAttribute('style', 'display:block');
          } else {
            itvPanel.setAttribute('style', 'display:none');
          }
        }
        
      } else {
        console.log("checkbox not found");
      }

      // Check the intevention type ddl:
      // case 1: no value selected, diable description textarea
      // case 2: value selected, enable descrition textarea
      // case 2.1: value = other, show textinput
      // case 2.2: otherwise, don't show
      let itvTypeDDLName = "data[" + this.dataModel.interventionNeeded.key_itv_type + "]";
      let itvTypeDDL = document.getElementsByName(itvTypeDDLName);
      console.log("ddl: ", itvTypeDDL);
      if (itvTypeDDL != null && itvTypeDDL[0] != null) {
        itvTypeDDL.values
      }
    }
  }
}
</script>