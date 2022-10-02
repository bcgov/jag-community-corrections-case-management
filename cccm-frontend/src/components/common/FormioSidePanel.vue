<template>
    <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templatePanel from '@/components/common/templateSidePanel.json';

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
      tmpJSONStr = tmpJSONStr.replaceAll('${name}', this.dataModel.clientName);
      tmpJSONStr = tmpJSONStr.replaceAll('${csNumber}', this.dataModel.clientNumber);
      tmpJSONStr = tmpJSONStr.replaceAll('${gender}', this.dataModel.gender);
      tmpJSONStr = tmpJSONStr.replaceAll('${dob}', this.dataModel.birthDate);
      tmpJSONStr = tmpJSONStr.replaceAll('${location}', this.dataModel.locationInformation == null ? '' : this.dataModel.locationInformation.outLocation);
      tmpJSONStr = tmpJSONStr.replaceAll('${orderExpDate}', this.dataModel.orderInformation == null ? '' : this.dataModel.orderInformation.expiryDate);
      
      //console.log("FormInfoDataEntry: ", tmpJSON);
      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;
    }
  }
}
</script>