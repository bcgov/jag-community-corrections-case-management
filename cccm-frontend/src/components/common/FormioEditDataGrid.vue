<template>
    <Form :form="formJSON" :submission="initData" />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateEditDataGrid from '@/components/common/templateEditDataGrid.json';

export default {
  name: 'FormioPanel',
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
      tmpJSONStr = tmpJSONStr.replaceAll('${label_editgrid}', this.dataTemplate.label);
      
      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;
    }
  }
}
</script>