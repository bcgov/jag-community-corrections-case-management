<template>
  <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import sampleFormInfoTemplate from '@/components/crna-cmp/sampleData/sampleFormInfoTemplate.json';

export default {
  name: 'FormioFormInfo',
  props: {
    dataModel: {}
  },
  data() {
    return {
      formInfoTemplate : sampleFormInfoTemplate,
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
      let tmpJSON = JSON.parse(JSON.stringify(this.formInfoTemplate));

      // set the label and key for the top component
      tmpJSON.components.label="tmpDataEntry";
      tmpJSON.components.key="tmpDataEntry";

      //Find index of form title component.    
      let objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'htmlelement'));
      
      // set form title
      tmpJSON.components[0].components[objIndex].content = this.dataModel.formTitle;
      
      //Find index of first row component.    
      objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'columns'));
      
      // set 1st row
      tmpJSON.components[0].components[objIndex].columns[0].components[0].columns[0].components[0].content = "<div><Strong>Created Date: </Strong>2021-03-12</div>\r\n\r\n<div><Strong>Created By: </Strong>Doe, Jane</div>";
      tmpJSON.components[0].components[objIndex].columns[0].components[0].columns[1].components[0].content = "<div><Strong>Updated Date: </Strong>2021-04-13</div>\r\n\r\n<div><Strong>Completed Date: </Strong>2021-07-13</div>";

      // set 2nd row
      tmpJSON.components[0].components[objIndex].columns[1].components[0].data = this.dataModel.crnacmp_types.data;
      
      console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = tmpJSON;
    }
  },
}
</script>