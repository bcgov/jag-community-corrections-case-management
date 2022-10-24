<template>
    <Form :form="formJSON" :submission="dataModel" @evt_changeButtonLabel="changeButtonLabel"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import { updateSourcesContacted } from "@/components/form.api";
import templatePanel from '@/components/common/templateSidePanel.json';

export default {
  name: 'FormioPanel',
  props: {
    dataModel: {},
    clientFormId: 0
  },
  data() {
    return {
      KEY_SOURCESCONTACTED: 'input_key_sourceContacted',
      templatePanel : templatePanel,
      formJSON : {}
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormData();
    setTimeout(() => {
      this.hideInputTextBox();
    }, 1000);
  },
  methods: {
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);

      // set client details
      tmpJSONStr = tmpJSONStr.replaceAll('${name}', this.dataModel.clientName);
      tmpJSONStr = tmpJSONStr.replaceAll('${csNumber}', this.dataModel.clientNumber);
      tmpJSONStr = tmpJSONStr.replaceAll('${gender}', this.dataModel.gender);
      tmpJSONStr = tmpJSONStr.replaceAll('${dob}', this.dataModel.birthDate);
      tmpJSONStr = tmpJSONStr.replaceAll('${location}', this.dataModel.locationInformation == null ? '' : this.dataModel.locationInformation.outLocation);
      tmpJSONStr = tmpJSONStr.replaceAll('${orderExpDate}', this.dataModel.orderInformation == null ? '' : this.dataModel.orderInformation.expiryDate);
      
      //console.log("FormInfoDataEntry: ", tmpJSON);
      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;
    },
    hideInputTextBox() {
      // get textbox instance
      let tbName= "data[" + this.KEY_SOURCESCONTACTED + "]";
      let textBox = document.getElementsByName(tbName);
      
      //hide textbox
      if (textBox != null &&  textBox[0] != null) {
        textBox[0].setAttribute('style', 'display:none');
      }
    },
    async changeButtonLabel(evt) {
      if (evt != null && evt.type === "evt_changeButtonLabel" ) {
        // get button instance
        let btnName= "data[add_source]";
        let theBtn = document.getElementsByName(btnName);

        // get textbox instance
        let tbName= "data[" + this.KEY_SOURCESCONTACTED + "]";
        let textBox = document.getElementsByName(tbName);
        
        // get html instance
        let className = '[class*="key_sourceContacted"]';
        let theHtmlParentDiv = document.querySelector(className);

        if (theBtn != null && theBtn[0] != null) {
          if (theBtn[0].innerText === "Add Source") {
            // Update the label to 'Save'
            theBtn[0].innerText = "Save";

            // show textbox
            if (textBox != null && textBox[0] != null) {
              textBox[0].setAttribute('style', 'display:block');
            }
            
            // hide html
            if (theHtmlParentDiv != null) {
              theHtmlParentDiv.setAttribute('style', 'display:none');
            }
          } else {
            // Save button clicked, call API to save the data
            let sourcesContacted = {};
            sourcesContacted[this.KEY_SOURCESCONTACTED] = evt.data[this.KEY_SOURCESCONTACTED];
            //console.log("sourcesContacted data: ", sourcesContacted);
            const [error, response] = await updateSourcesContacted(this.clientFormId, sourcesContacted);
            if (error) {
              console.error("Save source contacted error: ", error);
            } else {
              console.log("Save source contacted success", response);
            }
            
            theBtn[0].innerText = "Add Source"
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