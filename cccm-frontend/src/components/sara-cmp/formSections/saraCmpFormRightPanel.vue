<template>
  <div>
    <Form :form="dataModel" @evt_saveDraft="handleSaveDraft" @evt_print="handlePrint"/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';

export default {
  name: 'saraCmpForm',
  props: {
    dataModel: {}
  },
  components: {
    Form
  },
  methods: {
    handleSaveDraft(evt) {
      // emit an event, saveContinueClicked with setting false to flag "continue to next section", to the parent, so parent knows it's time to save data
      if (evt != null && evt.type === 'evt_saveDraft') {
        this.$emit('saveContinueClicked', false);
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