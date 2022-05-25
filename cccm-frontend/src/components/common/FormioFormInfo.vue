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
      
      // build type ddl
      let tmpJSON = JSON.parse(tmpJSONStr);
      let objIndex = tmpJSON.components[0].components.findIndex((obj => obj.type == 'columns'));
      tmpJSON.components[0].components[objIndex].columns[1].components[0].data = this.dataModel.crnacmp_types.data;
     
      this.formJSON = tmpJSON;
    }
  },
}
</script>