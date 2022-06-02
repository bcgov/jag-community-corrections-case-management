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
import FormioPanel from "@/components/common/FormioPanel.vue";

export default {
  name: 'CrnaCmpFormRightPanel',
  props: {
    dataModel: {}
  },
  components: {
    FormioPanel,
  }
}
</script>