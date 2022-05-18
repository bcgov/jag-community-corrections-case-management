<template>
  <div>
    <div v-for="(header, indexp) in dataModel.rightPanel" 
        :key="indexp">
      <FormioPanel v-if="header.type === 'panel'" :dataModel="header" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import FormioPanel from "@/components/common/FormioPanel.vue";

export default {
  name: 'CrnaCmpFormSideCards',
  props: {
    dataModel: {}
  },
  components: {
    Form,
    FormioPanel,
  }
}
</script>