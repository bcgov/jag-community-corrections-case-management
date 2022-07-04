<template>
    <Form :form="formJSON" v-on:editGridSaveRow="handleEditGridSaveRowEvent" :submission="initData" />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateEditDataGrid from '@/components/common/templateEditDataGridRadioText.json';

export default {
  name: 'FormioEditDataGridRadioText',
  props: {
    dataModel: {},
    initData: {},
    dataTemplate: {}
  },
  data() {
    return {
      templatePanel: templateEditDataGrid,
      formJSON: {}
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormioTemplate();
    // if (this.initData != null) {
    //   console.log("EditDataGridRadioText Initdata: ", this.initData.data.key_editgrid_radiotext[0].hidden_key);
    // }
  },
  methods: {
    buildFormioTemplate() {
      //console.log("initData: ", this.initData);
      //console.log("dataTemplate: ", this.dataTemplate);
       // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);

      tmpJSONStr = tmpJSONStr.replaceAll('${className_label}', this.dataTemplate.className_label);
      tmpJSONStr = tmpJSONStr.replaceAll('${label_editgrid}', this.dataTemplate.label);
      tmpJSONStr = tmpJSONStr.replaceAll('${label_questionLabel}', this.dataTemplate.label_questionLabel);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_questionLabel}', this.dataTemplate.key_questionLabel);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_editgrid}', this.dataTemplate.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_comments}', this.dataTemplate.key_comments);
      tmpJSONStr = tmpJSONStr.replaceAll('${label_textarea}', this.dataTemplate.label_textarea);
      
      let tmpJSON = JSON.parse(tmpJSONStr);

      this.formJSON = tmpJSON;
    },
    handleEditGridSaveRowEvent(event) {
      //console.log("datagrid radio text event data: ", event.row);
      this.$emit('dataOnChanged', event.row);
    }
  }
}
</script>

<style scoped>
.form-group {
  margin-bottom: 0px;
}

.editgrid-listgroup {
  margin-bottom: 0px;
}
</style>