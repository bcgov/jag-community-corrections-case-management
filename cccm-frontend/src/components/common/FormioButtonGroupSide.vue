<template>
    <Form
      v-if="isFormReady"
      :key="formKey"
      :form="formJSON" 
      @customEvent="handleCustomEvent"
      :submission="dataModel" />
</template>

<script lang="ts">
import { Form } from '@formio/vue';
import templateButtons from '@/components/common/templateButtonGroupSide.json';

export default {
  name: 'FormioSideButton',
  props: {
    dataModel: {},
  },
  data() {
    return {
      templatePanel : templateButtons,
      formJSON : {},
      formKey: 0,
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormData();
  },
  computed: {
    isFormReady() {
      return !!(this.formJSON && this.formJSON.components && this.formJSON.components.length);
    }
  },
  methods: {
    buildFormData() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.templatePanel);
      this.formJSON = JSON.parse(tmpJSONStr);
      this.formKey++;
    },
    handleCustomEvent(evt) {
      // Form.io emits customEvent for all button actions with type "event"
      if (evt != null && evt.type === 'evt_saveAndClose') {
        // emit an event, saveCloseClicked with setting false to flag "continue to next section", to the parent, so parent knows it's time to save data
        this.$emit('saveCloseClicked', false);
      } else if (evt != null && evt.type === 'evt_print') {
        // emit an event, printFormClicked, to the parent, so parent knows it's time to cancel form
        this.$emit('printFormClicked');
      }
    }
  }
}
</script>