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
    options: {}
  },
  data() {
    return {
      KEY_SOURCESCONTACTED: 'input_key_sourceContacted',
      templatePanel : templatePanel,
      formJSON : {},
      formKey: 0
    }
  },
  components: {
    Form
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
        this.dataModel.data.hideSCInput = !evt.data.hideSCInput;
        this.formKey++;

        // Time to save
        if (this.dataModel.data.hideSCInput) {
          // Save button clicked, call API to save the data
          let newVal = evt.data[this.KEY_SOURCESCONTACTED];
          let sourcesContacted = {};
          sourcesContacted[this.KEY_SOURCESCONTACTED] = newVal;
          const [error, response] = await updateSourcesContacted(this.clientFormId, sourcesContacted);
          if (error) {
            console.error("Save source contacted error: ", error);
          } else {
            console.log("Save source contacted success");
            this.$emit('sourcesContactedUpdated', newVal);
          }
        }
      }
    }
  }
}
</script>