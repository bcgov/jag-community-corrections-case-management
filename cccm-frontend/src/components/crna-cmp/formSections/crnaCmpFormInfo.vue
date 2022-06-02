<template>
  <div>
    <div v-for="(header, indexp) in dataModelFormInfo.rightPanel" :key="indexp">
      <FormioFormInfo v-if="indexp === 0" :dataModel="dataModelFormInfo"/>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { ref, reactive } from '@vue/composition-api';
import FormioFormInfo from "@/components/common/FormioFormInfo.vue";

export default ({
  name: 'CrnaCmpFormInfo',
  props: {
    dataModelFormInfo: {}
  },
  components: {
    FormioFormInfo,
  }
})
</script>
