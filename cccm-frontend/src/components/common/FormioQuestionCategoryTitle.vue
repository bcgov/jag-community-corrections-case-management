<template>
  <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateForm from '@/components/common/templateQuestionCategoryTitle.json';

export default {
  name: 'FormioQuestionCategoryTitle',
  props: {
    dataModel: {}
  },
  data() {
    return {
      formInfoTemplate : templateForm,
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

      tmpJSONStr = tmpJSONStr.replace('${sectionTitle}', this.dataModel.sectionTitle);
      tmpJSONStr = tmpJSONStr.replace('${sectonSubTitle}', this.dataModel.sectionSubTitle);
      tmpJSONStr = tmpJSONStr.replace('${sectionKey}', this.dataModel.sectionKey);
      tmpJSONStr = tmpJSONStr.replace('${subSectionKey}', this.dataModel.subSectionKey);

      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;
    }
  },
}
</script>