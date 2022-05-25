<template>
    <Form :form="formJSON"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateButton from '@/components/common/templateButton.json';

export default {
  name: 'FormioPanel',
  props: {
    dataModel: {}
  },
  data() {
    return {
      templateButton : templateButton,
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
      let tmpJSON = JSON.parse(JSON.stringify(this.templateButton));

      let btnComponent = tmpJSON.components[0].components[0].columns[0];
      let btnComponentCopy = JSON.parse(JSON.stringify(btnComponent));

      // build button
      let index = 0;
      for (const btn of this.dataModel.button){
        let btnComponentCopy = JSON.parse(JSON.stringify(btnComponent));
        btnComponentCopy.components[0].action = btn.action;
        btnComponentCopy.components[0].key = btn.key;
        btnComponentCopy.components[0].label = btn.label;
        btnComponentCopy.components[0].theme = btn.theme;
        tmpJSON.components[0].components[0].columns[index] = btnComponentCopy;
        index++;
      }

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = tmpJSON;
    }
  }
}
</script>