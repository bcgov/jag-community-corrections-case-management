<template>
  <div>
    <FormioSidePanel v-if="header.type === 'panel'" :dataModel="header" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import FormioSidePanel from "@/components/common/FormioSidePanel.vue";

export default {
  name: 'CrnaCmpFormRightPanel',
  props: {
    dataModel: {}
  },
  components: {
    FormioSidePanel,
    FormioEditPanel,
  }
}
</script>