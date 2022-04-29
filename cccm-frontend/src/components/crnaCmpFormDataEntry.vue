<template>
    <formio :form= formJSON />
</template>

<script>
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';

export default {
  name: 'CrnaCmpFormDataEntry',
  props: {
    formJSON: {}
  },
  components: {
    formio : Form
  }
}
</script>