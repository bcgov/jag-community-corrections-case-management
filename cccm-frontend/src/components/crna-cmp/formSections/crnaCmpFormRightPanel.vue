<template>
  <div>
    <div v-for="(header, indexp) in dataModel.rightPanel" 
        :key="indexp">
      <FormioPanel v-if="header.type === 'panel'" :dataModel="header" />
      <FormioEditPanel v-if="header.type === 'editPanel'" :dataModel="header" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import FormioPanel from "@/components/common/FormioPanel.vue";
import FormioEditPanel from "@/components/common/FormioEditPanel.vue";

export default {
  name: 'CrnaCmpFormRightPanel',
  props: {
    dataModel: {}
  },
  components: {
    FormioPanel,
    FormioEditPanel,
  }
}
</script>