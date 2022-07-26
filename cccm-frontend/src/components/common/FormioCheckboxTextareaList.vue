<template>
    <Form v-on:change="handleChangeEvent" :form="formJSON" :submission="initData" @evt_submitBtnClicked="handleSubmit"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateCheckboxTextarea from '@/components/common/templateCheckboxTextareaList.json';

export default {
  name: 'FormioCheckboxTextareaList',
  props: {
    dataModel: {},
    initData: {},
    // param passed from parent to indicate time to save data
    notifySavingData: {
      type: Number,
      default: 1,
    }
  },
  data() {
    return {
      templateCheckboxTextarea : templateCheckboxTextarea,
      formJSON : {},
    }
  },
  watch: {
    notifySavingData() {
      // Submit the form by simulating clicking the submit button
      for (let i = 0; i < this.dataModel.checkboxTextareaItems.length; i++) {
        let btn = document.getElementById(this.dataModel.checkboxTextareaItems[i].key_container);
        if (btn != null) { 
          //console.log("Simulate the btn click: ", btn);
          btn.click(); 
        }
      }
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormData()
  },
  methods: {
    handleSubmit(evt) {
      // emit an event, dataSubmitted, to the parent, so parent knows form data
      if (evt.data != null) {
        //console.log("child data submitted: ", evt.data.hidden_key, evt.data);
        this.$emit('dataSubmitted', evt.data);
      }
    },
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templateCheckboxTextarea);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_well}', this.dataModel.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${tooltip_title}', this.dataModel.tooltip_title);
      tmpJSONStr = tmpJSONStr.replaceAll('${content_html}', this.dataModel.label_description);
      tmpJSONStr = tmpJSONStr.replaceAll('${content_title}', this.dataModel.label_subSection);
      tmpJSONStr = tmpJSONStr.replaceAll('${className_label}', this.dataModel.className_label);
      tmpJSONStr = tmpJSONStr.replaceAll('${maxLength_textarea}', this.dataModel.maxLength_textarea);

      //console.log("tmpJSONStr: ", tmpJSONStr);
      let tmpJSON = JSON.parse(tmpJSONStr);
      let containerIndex = 1;

      let componentJSON = tmpJSON.components[0].components[0].components[containerIndex];
      let tmpComponentJSONStr = JSON.stringify(componentJSON);


      for (let i = 0; i < this.dataModel.checkboxTextareaItems.length; i++) {
        let componentJSONStr = tmpComponentJSONStr;
        componentJSONStr = componentJSONStr.replaceAll('${key_container}', this.dataModel.checkboxTextareaItems[i].key_container);
        componentJSONStr = componentJSONStr.replaceAll('${label_checkbox}', this.dataModel.checkboxTextareaItems[i].label_checkbox);
        componentJSONStr = componentJSONStr.replaceAll('${tooltip_checkbox}', this.dataModel.checkboxTextareaItems[i].tooltip_checkbox);
        componentJSONStr = componentJSONStr.replaceAll('${key_checkbox}', this.dataModel.key_checkbox);
        componentJSONStr = componentJSONStr.replaceAll('${label_textarea}', this.dataModel.label_textarea);
        componentJSONStr = componentJSONStr.replaceAll('${key_textarea}', this.dataModel.key_textarea);
        componentJSONStr = componentJSONStr.replaceAll('${value_hiddenKey}', this.dataModel.checkboxTextareaItems[i].key_container);

        tmpJSON.components[0].components[0].components[i+containerIndex] = JSON.parse(componentJSONStr);
      }
      //console.log("formJSONStr: ", JSON.stringify(tmpJSON));
      this.formJSON = tmpJSON;
    },
    handleChangeEvent(event) {
      if (event.changed && ( event.changed.component.key === this.dataModel.key_checkbox 
                          || event.changed.component.key === this.dataModel.key_textarea )) {
        let containerKey = event.changed.instance.parent.path;
        let parentKey = event.changed.instance.parent.parent.path;
        let questionLabel = event.changed.component.label;
        //if textarea is updated, need to get the checkbox lable
        if (event.changed.component.key === this.dataModel.key_textarea) {
          let components = event.changed.instance.parent.component.components;
          if (components != null) {
            for (let i = 0; i < components.length; i++) {
              if (components[i].key === this.dataModel.key_checkbox) {
                questionLabel = components[i].label;
                break;
              }
            }
          }
        }
        //console.log("formio checkbox textarea list event change: ", containerKey, event);
        this.$emit('dataOnChanged', event.data, parentKey, containerKey, questionLabel);
      }
    }
  }
}
</script>

<!-- <style>
.legend {
  font-size: 20px;
  font-weight: bold;
  text-decoration: underline;
  -webkit-text-decoration-color: rgb(255, 208, 0); /* Safari */  
  text-decoration-color: rgb(255, 208, 0);
}
</style> -->