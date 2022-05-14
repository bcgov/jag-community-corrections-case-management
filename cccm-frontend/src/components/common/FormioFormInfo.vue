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
      let tmpJSONStr = JSON.stringify(this.formInfoTemplate);

      tmpJSONStr = tmpJSONStr.replace('${formTitle}', this.dataModel.formTitle);

      //build first row content
      let firstRow = "<div><Strong>Created Date: </Strong>2021-03-12</div><div><Strong>Created By: </Strong>Doe, Jane</div>";
      tmpJSONStr = tmpJSONStr.replace('${firstRow}', firstRow);
      
      //build second row content
      let secondRow = "<div><Strong>Updated Date: </Strong>2021-04-13</div><div><Strong>Completed Date: </Strong>2021-07-13</div>";
      tmpJSONStr = tmpJSONStr.replace('${secondRow}', secondRow);
      //console.log(tmpJSONStr);

      // build type ddl
      let tmpJSON = JSON.parse(tmpJSONStr);
      let objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'columns'));
      tmpJSON.components[0].components[objIndex].columns[1].components[0].data = this.dataModel.crnacmp_types.data;
     
      this.formJSON = tmpJSON;
    }
  },
}
</script>