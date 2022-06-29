<template>
    <Form v-on:change="handleChangeEvent" :submission="initData" :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import formTemplate from '@/components/common/templateRadio.json';

export default {
  name: 'FormioRadio',
  props: {
    dataModel: {},
    initData: {},
  },
  data() {
    return {
      templateJSON: formTemplate,
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
      let tmpJSONStr = JSON.stringify(this.templateJSON);

      tmpJSONStr = tmpJSONStr.replace('${label}', this.dataModel.questionLabel);
      tmpJSONStr = tmpJSONStr.replace('${key}', this.dataModel.key);
      tmpJSONStr = tmpJSONStr.replace('${key_radioButton}', this.dataModel.key_radioButton);
      
      //Find index of radio component.    
      let tmpJSON = JSON.parse(tmpJSONStr);
      tmpJSON.components[0].components[0].values = this.dataModel.values;

      this.formJSON = tmpJSON;
    },
    handleChangeEvent(event) {
      // emit an event, dataOnChanged, to the parent, so parent knows the changes
      if (event.changed && event.changed.component.key === this.dataModel.key_radioButton) {
        //console.log("Radio changed: ", event);
        this.$emit('dataOnChanged', event.data, this.dataModel.key);
      }
    }
  }
}
</script>