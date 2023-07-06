<template>
    <!--Dynamic section-->
    <div>
        <v-progress-linear v-if="loading" indeterminate height="30" color="primary">Loading data</v-progress-linear>
        <Form v-if="!loading" 
          :key="keyDSection" 
          :form="dataModel" 
          :submission="formInitData" 
          v-on:change="handleChangeEvent" 
          v-on:blur="handleBlurEvent" 
          :options="options"/>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import { loadFormData } from "@/components/form.api";
import { useStore } from "@/stores/autoSaveStore";
import { mapStores } from 'pinia';

export default {
  name: 'FormDataRefreshSection',
  props: {
    dataModel: {},
    clientFormId: 0,
    csNumber: '',
    options: {},
    timeForValidate: {
      type: Number,
      default: 1,
    }
  },
  components: {
    Form
  },
  watch: {
    timeForValidate() {
      // get event.data, emit dataCollectedForValidate event to parent
      let fullData = {};
      this.$emit('dataCollectedForValidate', fullData);
    }
  },
  data() {
    return {
      keyDSection: 0,
      loading: false,
      formInitData: {}
    }
  },
  mounted() {
    // push csNumber and formId to autoSaveStore
    this.autosaveStore.setClientNumber(this.csNumber);
    this.autosaveStore.setFormId(this.clientFormId);

    // fetch form data
    this.loadFormData();
  },
  methods: {
    async loadFormData() {
      this.loading = true;
      const [error, clientFormData] = await loadFormData(this.csNumber, this.clientFormId);
      if (error) {
        console.error(error);
      } else {
        this.loading = false;
        this.formInitData = clientFormData;
        this.keyDSection++;
      }
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