<template>
    <!--evt_changeButtonLabe needs to match this.dataModel.button.event-->
    <Form :form="formJSON" @evt_changeButtonLabel="changeButtonLabel"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateEditPanel from '@/components/common/templateEditPanel.json';

export default {
  name: 'FormioPanel',
  props: {
    dataModel: {}
  },
  data() {
    return {
      templatePanel : templateEditPanel,
      formJSON : {},
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormData();
    setTimeout(() => {
      this.hideInputTextBox();
    }, 500);
  },
  methods: {
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);

      // set panel details
      tmpJSONStr = tmpJSONStr.replaceAll('${panelTitle}', this.dataModel.panelTitle);
      tmpJSONStr = tmpJSONStr.replaceAll('${panelKey}', this.dataModel.panelKey);
      tmpJSONStr = tmpJSONStr.replaceAll('${contentKey}', this.dataModel.contentKey);
      tmpJSONStr = tmpJSONStr.replaceAll('${content}', this.dataModel.dataString);
      tmpJSONStr = tmpJSONStr.replaceAll('${contentInputKey}', "input_" + this.dataModel.contentKey);
      // set button details
      tmpJSONStr = tmpJSONStr.replaceAll('${label_btn_add_source}', this.dataModel.button.label);
      tmpJSONStr = tmpJSONStr.replaceAll('${key_btn_add_source}', this.dataModel.button.key);
      tmpJSONStr = tmpJSONStr.replaceAll('${evt_btn_add_source}', this.dataModel.button.event);
      tmpJSONStr = tmpJSONStr.replaceAll('${action_btn_add_source}', this.dataModel.button.action);

      //console.log("FormInfoDataEntry: ", tmpJSON);
      this.formJSON = JSON.parse(tmpJSONStr);
    },
    hideInputTextBox() {
      // get textbox instance
        let tbName= 'data[' + "input_" + this.dataModel.contentKey + "]";
        let textBox = document.getElementsByName(tbName);
        
        // hide textbox
        if (textBox != null &&  textBox[0] != null) {
          textBox[0].setAttribute('style', 'display:none');
        }
    },
    changeButtonLabel(evt) {
      if (evt != null && evt.type === this.dataModel.button.event ) {
        // get button instance
        let btnName= "data[" + this.dataModel.button.key + "]";
        let theBtn = document.getElementsByName(btnName);

        // get textbox instance
        let tbName= 'data[' + "input_" + this.dataModel.contentKey + "]";
        let textBox = document.getElementsByName(tbName);
        
        // get html instance
        let className = '[class*="' + this.dataModel.contentKey + '"]';
        let theHtmlParentDiv = document.querySelector(className);

        if (theBtn != null && theBtn[0] != null) {
          if (theBtn[0].innerText === this.dataModel.button.label) {
            // Update the label to 'Save'
            theBtn[0].innerText = this.dataModel.button.toggledLabel;

            // show textbox
            if (textBox != null && textBox[0] != null) {
              textBox[0].setAttribute('style', 'display:block');
            }
            
            // hide html
            if (theHtmlParentDiv != null) {
              theHtmlParentDiv.setAttribute('style', 'display:none');
            }

          } else {
            theBtn[0].innerText = this.dataModel.button.label;
            // hide textbox
            if (textBox != null &&  textBox[0] != null) {
              textBox[0].setAttribute('style', 'display:none');
            }
            
            // show html
            if (theHtmlParentDiv != null) {
              theHtmlParentDiv.setAttribute('style', 'display:block');
            }
          }
        }
      }
    }
  }
}
</script>