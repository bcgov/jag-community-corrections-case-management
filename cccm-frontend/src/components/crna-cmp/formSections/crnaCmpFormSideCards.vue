<template>
  <div>
    <div v-for="(header, indexp) in dataModel.rightPanel" 
        :key="indexp">
      <FormioPanel v-if="header.type === 'panel'" :dataModel="header" />
      <FormioButton v-if="header.type === 'button'" :dataModel="header" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import FormioPanel from "@/components/common/FormioPanel.vue";
import FormioButton from "@/components/common/FormioButton.vue";

export default {
  name: 'CrnaCmpFormSideCards',
  props: {
    dataModel: {}
  },
  components: {
    Form,
    FormioPanel,
    FormioButton,
  }
}
</script>