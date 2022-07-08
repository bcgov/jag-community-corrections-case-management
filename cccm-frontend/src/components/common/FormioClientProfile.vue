<template>
  <Form :form="formJSON" :submission="initData"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateClientProfile from '@/components/common/templateClientProfile.json';

export default {
  name: "FormioClientprofile",
  props: {
    clientID: 0,
    initData: {},
  },
  data() {
    return {
      formJSON: {},
      template: templateClientProfile,
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildForm();
  },
  methods: {
    buildForm() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.template);

      tmpJSONStr = tmpJSONStr.replaceAll('${clientPhotoURL}', "https://www.w3schools.com/images/lamp.jpg");

      // build crnacmpType DDL
      let tmpJSON = JSON.parse(tmpJSONStr);
           
      this.formJSON = tmpJSON;
    }
  }
}
</script>