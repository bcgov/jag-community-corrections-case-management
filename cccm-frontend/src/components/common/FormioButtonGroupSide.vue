<template>
    <Form :form="formJSON" 
      @evt_saveAndClose="handleSaveAndClose" 
      @evt_print="handlePrint"
      :submission="dataModel" />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
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
      this.formJSON = JSON.parse(tmpJSONStr);
    },
    handleSaveAndClose(evt) {
      // emit an event, saveCloseClicked with setting false to flag "continue to next section", to the parent, so parent knows it's time to save data
      if (evt != null && evt.type === 'evt_saveAndClose' ) {
        this.$emit('saveCloseClicked', false);
      } 
    },
    handlePrint(evt) {
      // emit an event, printFormClicked, to the parent, so parent knows it's time to cancel form
      if (evt != null && evt.type === 'evt_print' ) {
        this.$emit('printFormClicked');
      } 
    }
  }
}
</script>