<template>
    <Form :form="btnGroup"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import formBtnGroup from '@/components/common/templateSaveBtnGroup.json';

export default {
  name: 'CrnaCmpFormSaveBtnGroup',
  data() {
    return {
      btnGroup : formBtnGroup,
    }
  },
  components: {
    Form
  }
}
</script>