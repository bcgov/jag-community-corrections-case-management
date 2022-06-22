<template>
    <Form v-on:change="handleChangeEvent" :submission="initData" :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateLabelTextArea from '@/components/common/templateLabelTextArea.json';

export default {
  name: 'FormioLabelTextarea',
  props: {
    dataModel: {},
    initData: {}
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

      tmpJSONStr = tmpJSONStr.replaceAll('${key}', this.dataModel.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${label}', this.dataModel.label);
      tmpJSONStr = tmpJSONStr.replaceAll('${label_textarea}', this.dataModel.label_textarea);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_textarea}', this.dataModel.key_textarea);
      tmpJSONStr = tmpJSONStr.replaceAll('${className_label}', this.dataModel.className_label);
      tmpJSONStr = tmpJSONStr.replaceAll('${maxLength_textarea}', this.dataModel.maxLength_textarea);

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = JSON.parse(tmpJSONStr);
    },
    handleChangeEvent(event) {
      // emit an event, dataOnChanged, to the parent, so parent knows the changes
      if (event.changed && event.changed.component.key === this.dataModel.key_textarea) {
        let key = event.changed.instance.parent.component.key;
        console.log("event: ", key);
        this.$emit('dataOnChanged', event.data, key);
      }
    }
  }
}
</script>