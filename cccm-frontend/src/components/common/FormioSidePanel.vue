<template>
    <Form :key="formKey"
      :form="formJSON" 
      :submission="dataModel" 
      :options="options"
      @evt_changeButtonLabel="changeButtonLabel"
      @evt_cancelButtonLabel="changeButtonLabel"/>
</template>

<script lang="ts">
import { Form } from '@formio/vue';
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
      templatePanel : templatePanel,
      formJSON : {},
      formKey: 0,
      preVal: ''
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormData();
    if (this.dataModel != null && this.dataModel.data != null) {
      this.preVal = this.dataModel.data.input_key_sourceContacted;
    }
  },
  methods: {
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);
      this.formJSON = JSON.parse(tmpJSONStr);
    },
    async changeButtonLabel(evt) {
      if (evt != null && evt.type === "evt_cancelButtonLabel") {
        this.dataModel.data.sourcesContacted.hideSCInput = !evt.data.sourcesContacted.hideSCInput;
        this.dataModel.data.input_key_sourceContacted = this.preVal;
        this.formKey++;
      }
      if (evt != null && evt.type === "evt_changeButtonLabel" ) {
        this.dataModel.data.sourcesContacted.hideSCInput = !evt.data.sourcesContacted.hideSCInput;
        this.formKey++;

        // Time to save
        if (this.dataModel.data.sourcesContacted.hideSCInput) {
          let newVal = evt.data[this.$KEY_SOURCES_CONTACTED];
          this.preVal = newVal;
          let sourcesContacted = {};
          sourcesContacted[this.$KEY_SOURCES_CONTACTED] = newVal;
          const [error, response] = await updateSourcesContacted(this.clientFormId, sourcesContacted);
          if (error) {
            console.error("Save source contacted error: ", error);
          }
        }
      }
    }
  }
}
</script>