<template>
    <Form
      :key="formKey"
      :form="formJSON" 
      :submission="dataModel" 
      :options="options"
      @customEvent="changeButtonLabel" />
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
      preVal: '',
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
      this.formKey++;
    },
    async changeButtonLabel(evt) {
      if (evt != null && evt.type === "evt_cancelButtonLabel") {
        this.dataModel.data.sourcesContacted.hideSCInput = true;
        this.dataModel.data.input_key_sourceContacted = this.preVal;
        this.formKey++;
      } else if (evt != null && (evt.type === "evt_addSource")) {
        this.dataModel.data.sourcesContacted.hideSCInput = false;
        this.dataModel.data.input_key_sourceContacted = this.preVal;
        this.formKey++;
      } else if (evt != null && evt.type === "evt_saveSource") {
        this.dataModel.data.sourcesContacted.hideSCInput = true;
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