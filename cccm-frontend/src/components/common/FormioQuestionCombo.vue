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
      let tmpJSON = JSON.parse(JSON.stringify(this.questionComboTemplate));

      // set the label and key for the top component
      tmpJSON.components.label="tmpDataEntry";
      tmpJSON.components.key="tmpDataEntry";

      //Find index of radio component.    
      let objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'radio'));
      //console.log("Radio component index: ", objIndex);

      tmpJSON.components[0].components[objIndex].label = this.dataModel.questionLabel;
      tmpJSON.components[0].components[objIndex].values = this.dataModel.values;

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = tmpJSON;
    }
  },
}
</script>