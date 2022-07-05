<template>
  <Form :form="formJSON" v-on:clientSearchEvent="handleClientSearch"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import {clientSearch} from "@/components/form.api";
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
    },
    async handleClientSearch(payload) {
      console.log("handleClientSearch", payload);
      const [error, response] = await clientSearch(payload);
      if (error) {
        console.error(error);
      } else {
        console.log("clientSearch result: ", response);
      }
    }
  },
}
</script>