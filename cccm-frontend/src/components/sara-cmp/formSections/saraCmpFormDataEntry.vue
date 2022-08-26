<template>
  <div>
    <Form :form="dataModel"/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';

export default {
  name: 'saraCmpForm',
  props: {
    dataModel: {}
  },
  components: {
    Form
  },
}
</script>