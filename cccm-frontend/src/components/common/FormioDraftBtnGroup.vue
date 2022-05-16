<template>
    <Form :form="btnGroup"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import sampleFormBtnGroup from '@/components/common/templateDraftBtnGroup.json';

export default {
  name: 'CrnaCmpFormBtnGroup',
  data() {
    return {
      btnGroup : sampleFormBtnGroup,
    }
  },
  components: {
    Form
  }
}
</script>