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
      tmpJSONStr = tmpJSONStr.replace('${createdDate}', this.dataModel.createdDate);
      tmpJSONStr = tmpJSONStr.replace('${createdBy}', this.dataModel.createdBy);
      tmpJSONStr = tmpJSONStr.replace('${updatedDate}', this.dataModel.updatedDate);
      tmpJSONStr = tmpJSONStr.replace('${completedDate}', this.dataModel.completedDate);
      tmpJSONStr = tmpJSONStr.replace('${formStatus}', this.dataModel.formStatus);

      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;
    }
  },
}
</script>

<style>
.custom_wrap {
  color: #FCBA19;
  text-align:center;
  margin: 10px;
  padding: 10px;
  background-color:#FDF4DF;
  font-size: 25px;
}
</style>