<template>
  <FormioFormInfo :dataModel="dataModelFormInfo" />
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
