<template>
    <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';

export default {
  name: 'CrnaCmpFormInfo',
  props: {
    formJSON: {}
  },
  components: {
    Form
  }
}
</script>