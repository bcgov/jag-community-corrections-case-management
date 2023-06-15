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
    console.log("data: ", this.dataModel);
    this.buildFormInfoDataEntry()
  },
  methods: {
    buildFormInfoDataEntry() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.formInfoTemplate);
      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;
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