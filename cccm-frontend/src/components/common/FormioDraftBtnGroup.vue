<template>
    <Form :form="btnGroup"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import formBtnGroup from '@/components/common/templateDraftBtnGroup.json';

export default {
  name: 'FormioSaveDraftBtnGroup',
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