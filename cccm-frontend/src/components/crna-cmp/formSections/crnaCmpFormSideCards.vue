<template>
    <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateSideCards from './templateSideCards.json';

export default {
  name: 'CrnaCmpFormSideCards',
  props: {
    dataModel: {}
  },
  data() {
    return {
      formInfoTemplate : templateSideCards,
      formJSON : {},
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormioData()
  },
  methods: {
    buildFormioData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.formInfoTemplate);

      // set client details
      tmpJSONStr = tmpJSONStr.replace('${firstName}', this.dataModel.clientDetails.firstName);
      tmpJSONStr = tmpJSONStr.replace('${lastName}', this.dataModel.clientDetails.lastName);
      tmpJSONStr = tmpJSONStr.replace('${csNumber}', this.dataModel.clientDetails.csNumber);
      tmpJSONStr = tmpJSONStr.replace('${gender}', this.dataModel.clientDetails.gender);
      tmpJSONStr = tmpJSONStr.replace('${dob}', this.dataModel.clientDetails.dob);
      tmpJSONStr = tmpJSONStr.replace('${location}', this.dataModel.clientDetails.firstName);
      tmpJSONStr = tmpJSONStr.replace('${finalOrderExpDate}', this.dataModel.clientDetails.finalOrderExpDate);

      // set legends  
      let legends = "<ol type='A'>";
      for (const legendVal of this.dataModel.legend.data){
          legends += "<li>" + legendVal + "</li>";
      }
      legends += "</ol>";
      tmpJSONStr = tmpJSONStr.replace('${legends}', legends);

      // set source contacted
      tmpJSONStr = tmpJSONStr.replace('${sourceContacted}', this.dataModel.sourceContacted.data);
      
      // build type ddl
      let tmpJSON = JSON.parse(tmpJSONStr);
      
      this.formJSON = tmpJSON;
    }
  },
}
</script>