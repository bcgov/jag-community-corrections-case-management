<template>
    <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templatePanel from '@/components/common/templatePanel.json';

export default {
  name: 'FormioPanel',
  props: {
    dataModel: {}
  },
  data() {
    return {
      templatePanel : templatePanel,
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
      let tmpJSONStr = JSON.stringify(this.templatePanel);

      // set client details
      tmpJSONStr = tmpJSONStr.replace('${panelTitle}', this.dataModel.panelTitle);
      tmpJSONStr = tmpJSONStr.replace('${panelKey}', this.dataModel.panelKey);
      tmpJSONStr = tmpJSONStr.replace('${contentKey}', this.dataModel.contentKey);
      
      // build content
      let content = "";

      // text
      if (this.dataModel.contentType == 'text') {
        content = this.dataModel.data;
      }

      // unordered list
      if (this.dataModel.contentType == 'ul') {
        content = "<ul>";
        for (const legendVal of this.dataModel.data){
          content += "<li>" + legendVal + "</li>";
        }
        content += "</ul>";
      }

      // ordered list (type A)
      if (this.dataModel.contentType == 'ol_typea') {
        content = "<ol type='A'>";
        for (const legendVal of this.dataModel.data){
          content += "<li>" + legendVal + "</li>";
        }
        content += "</ol>";
      }

      // table
      if (this.dataModel.contentType == 'table') {
        content = "<table>";
        for (const rowArray of this.dataModel.data){
          content += "<tr>";
          for (const col of rowArray){
            content += "<th>" + col + "</th>";
          }
          content += "</tr>";
        }
        content += "</table>";
      }

      let tmpJSON = JSON.parse(tmpJSONStr);
      tmpJSON.components[0].components[0].content = content;

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = tmpJSON;
    }
  }
}
</script>