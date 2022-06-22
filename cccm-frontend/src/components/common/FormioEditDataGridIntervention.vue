<template>
    <Form :form="formJSON" v-on:editGridSaveRow="handleEditGridSaveRowEvent" :submission="initData" />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateEditDataGrid from '@/components/common/templateEditDataGridIntervention.json';

export default {
  name: 'FormioEditDataGridIntervention',
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
    //console.log("initdata: ", this.initData);
  },
  methods: {
    buildFormioTemplate() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);

      // set panel details
      tmpJSONStr = tmpJSONStr.replaceAll('${className_label}', this.dataTemplate.className_label);
      tmpJSONStr = tmpJSONStr.replaceAll('${label_editgrid}', this.dataTemplate.label);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_questionLabel}', this.dataTemplate.key_questionLabel);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_editgrid}', this.dataTemplate.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_comments}', this.dataTemplate.key_comments);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_itv_type}', this.dataTemplate.key_itv_type);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_itv_description}', this.dataTemplate.key_itv_description);
      
      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;
    },
    handleEditGridSaveRowEvent(event) {
      this.$emit('dataOnChanged', event.row);
    }
  }
}
</script>