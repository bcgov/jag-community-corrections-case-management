<template>
  <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import sampleQuestionComboTemplate from '../sampleData/sampleQuestionComboTemplate.json';
import sampleFormDataEntry from '../sampleData/sampleDataEntry.json';

export default {
  name: 'CrnaCmpFormQuestionCombo',
  props: {
    questionDataModel: {}
  },
  data() {
    return {
      questionComboTemplate : sampleQuestionComboTemplate,
      formJSON : {},
      sampleFormDataEntry : sampleFormDataEntry
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormInfoNDataEntry()
  },
  methods: {
    async buildFormInfoNDataEntry() {
      this.formJSON = this.questionComboTemplate;
    }
  }
}
</script>