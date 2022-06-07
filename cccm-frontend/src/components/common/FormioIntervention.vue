<template>
    <Form :form="formJSON"/>
    <!-- <div>
      <FormioCheckbox :dataModel="dataModel"/>
      <div :id="`panel_${dataModel.key_yn}`">
        <Form :form="formJSON"/>
        <FormioButton :dataModel="dataModel"/>
      </div>
    </div> -->
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import FormioButton from "@/components/common/FormioButton.vue";
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
    FormioButton,
  },
  mounted(){
    this.buildFormData();
  },
  methods: {
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templateIntervention);
      
      // Set intervention needed component
      tmpJSONStr = tmpJSONStr.replace('${label}', this.dataModel.label);
      tmpJSONStr = tmpJSONStr.replaceAll('${key}', this.dataModel.key_yn);
      //tmpJSONStr = tmpJSONStr.replace('${id_key}', this.dataModel.key_yn);
      tmpJSONStr = tmpJSONStr.replace('${defaultValue}', this.dataModel.defaultValue);

      // Set intervention needed component
      tmpJSONStr = tmpJSONStr.replace('${key_itv_details}', this.dataModel.key_yn);
      tmpJSONStr = tmpJSONStr.replace('${key_itv_type}', this.dataModel.key_itv_type);

      tmpJSONStr = tmpJSONStr.replace('${key_itv_other}', this.dataModel.key_itv_other);
      tmpJSONStr = tmpJSONStr.replace('${value_itv_other}', this.dataModel.value_itv_other);

      tmpJSONStr = tmpJSONStr.replace('${key_itv_description}', this.dataModel.key_itv_description);
      tmpJSONStr = tmpJSONStr.replace('${value_itv_description}', this.dataModel.value_itv_description);

      // Set intervention needed type DDL
      let tmpJSON = JSON.parse(tmpJSONStr);  
      //let objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'well'));
      tmpJSON.components[1].components[0].columns[0].components[0].data.values = this.dataModel.itv_type_data.values;

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = tmpJSON;
      
    }
  },
  computed: {
    getCheckBoxValue() {
      return this.dataModel.defaultValue;
    },
  }
}
</script>