<template>
    <Form :key="formKey"
      :form="formJSON" 
      :submission="dataModel" 
      :options="options"
      @evt_changeButtonLabel="changeButtonLabel"/>
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
    clientFormId: 0,
    options: {},
    timeForValidate: {
      type: Number,
      default: 1,
    }
  },
  data() {
    return {
      KEY_SOURCESCONTACTED: 'input_key_sourceContacted',
      templatePanel : templatePanel,
      formJSON : {},
      formKey: 0,
      simulateBtnClick: false
    }
  },
  components: {
    Form
  },
  watch: {
    timeForValidate() {
      this.simulateBtnClick = true;
      // get button instance
      let btnName= "data[add_source]";
      let theBtn = document.getElementsByName(btnName);
      // Submit the form by simulating clicking the submit button
      if (theBtn != null && theBtn[0] != null) {
        theBtn[0].click(); 
      }
    }
  },
  mounted(){
    this.buildFormData();
  },
  methods: {
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);
      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;
    },
    async changeButtonLabel(evt) {
      if (evt != null && evt.type === "evt_changeButtonLabel" ) {
        if (this.simulateBtnClick) {
          this.simulateBtnClick = false;
          this.$emit('dataCollectedForValidate', evt.data[this.KEY_SOURCESCONTACTED]);
        } else {
          this.dataModel.data.hideSCInput = !evt.data.hideSCInput;
          this.formKey++;

          // Time to save
          if (!this.dataModel.data.hideSCInput) {
            // Save button clicked, call API to save the data
            let sourcesContacted = {};
            sourcesContacted[this.KEY_SOURCESCONTACTED] = evt.data[this.KEY_SOURCESCONTACTED];
            const [error, response] = await updateSourcesContacted(this.clientFormId, sourcesContacted);
            if (error) {
              console.error("Save source contacted error: ", error);
            } else {
              console.log("Save source contacted success", response);
            }
          }
        }
      }
    }
  }
}
</script>