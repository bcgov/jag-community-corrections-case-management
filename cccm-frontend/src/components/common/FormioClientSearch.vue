<template>
  <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import template from '@/components/common/templateClientSearch.json';

export default {
  name: 'FormioClientSearch',
  data() {
    return {
      formInfoTemplate : template,
      formJSON : {},
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildForm()
  },
  methods: {
    buildForm() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.formInfoTemplate);

      // build crnacmpType DDL
      let tmpJSON = JSON.parse(tmpJSONStr);
           
      this.formJSON = tmpJSON;
    }
  },
}
</script>