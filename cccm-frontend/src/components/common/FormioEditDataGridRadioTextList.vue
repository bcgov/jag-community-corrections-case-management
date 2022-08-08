<template>
    <Form :form="formJSON" v-on:editGridSaveRow="handleEditGridSaveRowEvent" :submission="initData" />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateEditDataGrid from '@/components/common/templateEditDataGridRadioTextList.json';

export default {
  name: 'FormioEditDataGridRadioText',
  props: {
    dataModel: {},
    initData: {},
    dataTemplate: {},
    dataTemplateP: {},
    radioValue: {},
    editgridLabel: "",
    uiType: "",
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
    //   console.log("EditDataGridRadioTextList Initdata: ", this.initData.data.key_editgrid_radiotext[0].hidden_key);
    // }
  },
  methods: {
    buildFormioTemplate() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);

      // set panel details
      let label = this.editgridLabel;
      if (this.dataTemplate.show_label === null || !this.dataTemplate.show_label) {
        label = "";
      } 
      
      tmpJSONStr = tmpJSONStr.replaceAll('${className_label}', this.dataTemplateP.className_label);
      tmpJSONStr = tmpJSONStr.replaceAll('${label_editgrid}', label);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_questionLabel}', this.dataTemplateP.key_questionLabel);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_editgrid}', this.dataTemplateP.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_comments}', this.dataTemplateP.key_comments);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_radioButton}', this.dataTemplateP.key_radioButton);
      
      let tmpJSON = JSON.parse(tmpJSONStr);

      if (this.dataTemplate.show_header === null || !this.dataTemplate.show_header) {
        tmpJSON.components[1].templates.header = "<div />";
      }
      
      if (this.dataTemplate.show_radioButtons) {
        //Set the radio data. 
        tmpJSON.components[1].components[0].columns[1].components[0].values = this.radioValue;
      } else {
        // remove the radio button component
        tmpJSON.components[1].components[0].columns[1].components = [];
      }

      // remove the comment component
      if (!this.dataTemplate.show_comments) {
        tmpJSON.components[1].components[0].columns[2].components = [];
      }      

      
      this.formJSON = tmpJSON;
    },
    handleEditGridSaveRowEvent(event) {
      //console.log("datagrid radio text list event data: ", event.row);
      this.$emit('dataOnChanged', this.uiType, event.row);
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