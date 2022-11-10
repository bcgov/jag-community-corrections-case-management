<template>
  <Form :form="formJSON" :submission="dataModel" @evt_editForm="handleUnlockForm"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
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
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormInfoDataEntry()
  },
  methods: {
    buildFormInfoDataEntry() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.formInfoTemplate);
      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;
    },
    handleUnlockForm() {
      this.$emit('unlockForm');
    }
  },
}
</script>

<style>
.custom_wrap {
  color: #FCBA19;
  text-align:center;
  margin: 10px;
  padding: 10px;
  background-color:#FDF4DF;
  font-size: 25px;
}
</style>