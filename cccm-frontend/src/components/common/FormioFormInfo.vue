<template>
  <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateFormInfo from '@/components/common/templateFormInfo.json';

export default {
  name: 'FormioFormInfo',
  props: {
    dataModel: {}
  },
  data() {
    return {
      formInfoTemplate : templateFormInfo,
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
      tmpJSONStr = tmpJSONStr.replace('${createdDate}', this.dataModel.createdDate);
      tmpJSONStr = tmpJSONStr.replace('${createdBy}', this.dataModel.createdBy);
      
      //build second row content
      tmpJSONStr = tmpJSONStr.replace('${updatedDate}', this.dataModel.updatedDate);
      tmpJSONStr = tmpJSONStr.replace('${completedDate}', this.dataModel.completedDate);
      
      // build formStatus
      tmpJSONStr = tmpJSONStr.replace('${formStatus}', this.dataModel.formStatus);

      // build crnacmpType DDL
      let tmpJSON = JSON.parse(tmpJSONStr);
      if (this.dataModel != null && this.dataModel.crnacmp_types != null) {
        tmpJSON.components[0].components[1].columns[0].components[0].columns[2].components[0].data = this.dataModel.crnacmp_types.data;
      }
     
      this.formJSON = tmpJSON;
    }
  },
}
</script>

<style>
.custom_wrap {
  margin: 10px;
  display: flex;
  align-self: flex-end;
  font-size: 25px;
  display: block; 
}
</style>