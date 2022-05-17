<template>
    <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateLabelTextArea from '@/components/common/templateLabelTextArea.json';

export default {
  name: 'FormioLabelTextarea',
  props: {
    dataModel: {}
  },
  data() {
    return {
      templateLabelTextArea : templateLabelTextArea,
      formJSON : {},
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormData()
  },
  methods: {
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templateLabelTextArea);

      tmpJSONStr = tmpJSONStr.replace('${label}', this.dataModel.label);
      tmpJSONStr = tmpJSONStr.replace('${label_textarea}', this.dataModel.label_textarea);

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = JSON.parse(tmpJSONStr);
    }
  }
}
</script>