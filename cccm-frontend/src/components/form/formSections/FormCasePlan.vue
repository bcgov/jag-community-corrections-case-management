<template>
    <!--Case plan section-->
    <div>
        <v-progress-linear v-if="loading" indeterminate height="30" color="primary">Loading case plan</v-progress-linear>
        <Form v-if="!loading" 
          :key="keyCaseplan" 
          :form="dataModel" 
          :submission="localInitData" 
          v-on:change="handleChangeEvent" 
          v-on:blur="handleBlurEvent" 
          :options="options"/>
    </div>
</template>

<script lang="ts">
import { Form } from '@formio/vue';
import { getCasePlanIntervention, loadFormData } from "@/components/form.api";
import { useAutosaveStore } from "@/stores/autoSaveStore";
import { mapStores } from 'pinia';

export default {
  name: 'FormCasePlan',
  props: {
    dataModel: {},
    initData: {},
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
    initData: {
      immediate: true,
      deep: true,
      handler(val) {
        if (val != null) {
          this.localInitData = JSON.parse(JSON.stringify(val));
        }
      }
    },
    timeForValidate() {
      // get event.data, emit dataCollectedForValidate event to parent
      let fullData = {};
      this.$emit('dataCollectedForValidate', fullData);
    }
  },
  data() {
    return {
      keyCaseplan: 0,
      loading: false,
      autoSaveData: {},
      autoSaveDataCandidate: {},
      saving: false,
      localInitData: { data: {} },
    }
  },
  mounted() {
    // push csNumber and formId to autoSaveStore
    this.autosaveStore.setClientNumber(this.csNumber);
    this.autosaveStore.setFormId(this.clientFormId);

    // fetch interventions 
    this.getCasePlanInterventionAPI();
    //console.log("caseplan options: ", this.options);
  },
  methods: {
    async getCasePlanInterventionAPI() {
        this.loading = true;
        const [error, clientFormData] = await loadFormData(this.csNumber, this.clientFormId);
        //console.log("client form data fetched from caseplan tab: ", clientFormData)
        if (error) {
          console.error(error);
        } else {
          this.localInitData = clientFormData;

          // get the intervention data
          const [error, interventionData] = await getCasePlanIntervention(this.csNumber, this.clientFormId, true);
          // console.log("getCasePlanIntervention: {}", interventionData);
          if (error) {
              console.error(error);
          } else {
              this.loading = false;
              // Deal with special case where intervention is attached to a section comment, rather than a section question.
              interventionData.forEach(dataset => {
                if (dataset.comment == null || dataset.comment == '') {
                  dataset.comment = dataset.value;
                }
              });
              
              // add Intervention data to the initData; refresh the page to show it
              if (this.localInitData && this.localInitData.data) {
                this.localInitData.data.interventions = interventionData;
              }
              //console.log("interventionData; ", this.localInitData.data.interventions);
              this.keyCaseplan++;
          }
        }
        
    },
    handleChangeEvent(event) {
      this.autosaveStore.handleChangeEvent(event, true);
    },
    handleBlurEvent(event) {
      //console.log("From blur event, this.autoSaveDataCandidate: ", this.autoSaveDataCandidate);
      this.autosaveStore.autoSave();
    }
  },
  computed: {
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useAutosaveStore)
  },
}
</script>