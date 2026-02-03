<template>
  <Form
    v-if="isFormReady"
    :key="formKey"
    :form="formJSON"
    :submission="dataModel"
    @customEvent="handleUnlockForm"
  />
</template>

<script lang="ts">
import { Form } from '@formio/vue';
import templateFormInfo from '@/components/common/templateFormInfo.json';

export default {
  name: 'FormioFormInfo',
  props: {
    dataModel: {},
  },
  data() {
    return {
      formInfoTemplate : templateFormInfo,
      formJSON : {},
      formKey: 0,
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormInfoDataEntry();
  },
  computed: {
    isFormReady() {
      return !!(this.formJSON && this.formJSON.components && this.formJSON.components.length);
    }
  },
  methods: {
    buildFormInfoDataEntry() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.formInfoTemplate);
      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;
      this.formKey++;
    },
    handleUnlockForm(evt) {
      if (evt != null && evt.type === 'evt_editForm' ) {
        this.$emit('unlockForm');
      }
    }
  },
}
</script>

<style>
.custom_wrap {
  text-align:center;
  margin: 10px;
  padding: 10px;
  font-size: 25px;
}

.color_yellow {
  background-color:#FFE5BD;
}

.color_green {
  background-color:#c6e8c5;
}
</style>