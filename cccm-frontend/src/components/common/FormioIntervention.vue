<template>
    <Form :form="formJSON" v-on:change="handleChangeEvent"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateIntervention from '@/components/common/templateIntervention.json';

export default {
  name: 'FormioPanel',
  props: {
    dataModel: {}
  },
  data() {
    return {
      templateIntervention : templateIntervention,
      formJSON : {},
    }
  },
  components: {
    Form,
  },
  mounted(){
    this.buildFormData();
  },
  methods: {
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templateIntervention);
      
      // Set intervention needed component
      tmpJSONStr = tmpJSONStr.replaceAll('${label}', this.dataModel.label);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_checkbox}', this.dataModel.key_yn);
      tmpJSONStr = tmpJSONStr.replaceAll('${defaultValue_checkbox}', this.dataModel.defaultValue);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_itvDataGrid}', "datagrid_" + this.dataModel.key_yn);

      // Set intervention needed component
      tmpJSONStr = tmpJSONStr.replaceAll('${key_itv_details}', this.dataModel.key_yn);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_itv_type}', this.dataModel.key_itv_type);

      tmpJSONStr = tmpJSONStr.replaceAll('${key_itv_other}', this.dataModel.key_itv_other);
      tmpJSONStr = tmpJSONStr.replaceAll('${value_itv_other}', this.dataModel.value_itv_other);

      tmpJSONStr = tmpJSONStr.replaceAll('${key_itv_description}', this.dataModel.key_itv_description);
      tmpJSONStr = tmpJSONStr.replaceAll('${value_itv_description}', this.dataModel.value_itv_description);

      // Set intervention needed type DDL
      let tmpJSON = JSON.parse(tmpJSONStr);  
      //let objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'well'));
      tmpJSON.components[1].components[0].components[0].columns[0].components[0].data.values = this.dataModel.itv_type_data.values;

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = tmpJSON;
    },
    handleChangeEvent(event) {
      //if (event.changed && event.changed.component.key === 'customerNumber' && event.changed.value) {
      if (event.changed) {
        console.log("event: ", event);
        console.log("data: ", JSON.stringify(event.data));
      }
    }
  }
}
</script>