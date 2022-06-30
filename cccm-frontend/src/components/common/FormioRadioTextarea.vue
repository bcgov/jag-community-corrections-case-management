<template>
    <Form v-on:change="handleChangeEvent" :form="formJSON" :submission="initData"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import formTemplate from '@/components/common/templateRadioTextarea.json';

export default {
  name: 'FormioRadioTextarea',
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

      // set radio btn component 
      tmpJSONStr = tmpJSONStr.replace('${label_questionLabel}', this.dataModel.questionLabel);
      tmpJSONStr = tmpJSONStr.replace('${key}', this.dataModel.key);
      tmpJSONStr = tmpJSONStr.replace('${key_radioButton}', this.dataModel.key_radioButton);
      tmpJSONStr = tmpJSONStr.replace('${defaultValue}', this.dataModel.initData.data.key_radioButton);
      
      // set textarea component
      tmpJSONStr = tmpJSONStr.replace('${key_textarea}', this.dataModel.comments.key);
      tmpJSONStr = tmpJSONStr.replace('${defaultValue_textarea}', this.dataModel.initData.data.key_comments);

      //Find index of radio component.    
      let tmpJSON = JSON.parse(tmpJSONStr);
      tmpJSON.components[0].components[0].values = this.dataModel.values;

      this.formJSON = tmpJSON;
    },
    handleChangeEvent(event) {
      // emit an event, dataOnChanged, to the parent, so parent knows the changes
      if ( event.changed 
        && (  event.changed.component.key === this.dataModel.key_radioButton
          ||  event.changed.component.key === this.dataModel.comments.key)
        ) {
        console.log("Formio radioTextarea: ", event);
        this.$emit('dataOnChanged', event.data, this.dataModel.key);
      }
    }
  }
}
</script>