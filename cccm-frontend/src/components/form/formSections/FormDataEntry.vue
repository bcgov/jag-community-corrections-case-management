<template>
  <div>
    <Form :key="formKey"
      :form="dataModel" 
      :submission="initData" 
      :options="options"
      v-on:change="handleChangeEvent" 
      v-on:blur="handleBlurEvent"
      @key_hidden_submit_btn="handleHiddenBtnClick"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import { objectToString } from '@vue/shared';
import { useStore } from "@/stores/autoSaveStore";
import { mapStores } from 'pinia';

export default {
  name: 'FormDataEntry',
  props: {
    dataModel: {},
    initData: {},
    csNumber: '',
    formId: '',
    options: {},
    formType: '',
    sendData: 0
  },
  watch: {
    sendData() {
      this.simulateBtnClick();
    }
  },
  components: {
    Form
  },
  data() {
    return {
      formKey: 0
    }
  },
  mounted() {
    // push csNumber and formId to autoSaveStore
    this.autosaveStore.setClientNumber(this.csNumber);
    this.autosaveStore.setFormId(this.formId);
  },
  methods: {
    simulateBtnClick() {
      let elementName = "data[key_hidden_submit_btn]";
      let theBtn = document.getElementsByName(elementName);
      if (theBtn != null && theBtn.length == 1) {
        theBtn[0].click();
      }
    },
    handleHiddenBtnClick(evt) {
      this.$emit('formDataCollected', evt.data);
    },
    handleChangeEvent(event) {
      this.autosaveStore.handleChangeEvent(event, false);
    },
    handleBlurEvent(event) {
      this.autosaveStore.autoSave();
    }
  },
  computed: {
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore)
  },
}
</script>