<template>
  <div id="trend-analysis-view">
    <h1>Trend Analysis</h1>
    <ChartFilter/>

    <DataView @changeView="changeView" :selected-view="this.viewType"/>

    <TrendChart class="mb-4" v-if="this.viewType === 'graph'"/>
    <InterventionsTable class="mb-4" v-if="this.viewType === 'interventions'"/>
    <CommentsTable class="mb-4" v-if="this.viewType === 'comments'"/>
  </div>

</template>

<script lang="ts">

import CommentsTable from "./CommentsTable.vue";
import ChartFilter from "./ChartFilter.vue";
import DataView from "./DataView.vue";
import TrendChart from "./TrendChart.vue";
import InterventionsTable from "./InterventionsTable.vue";
import axios from "axios";
import {trendStore} from "@/stores/trendstore";
import {formSearch} from "@/components/form.api";

export default {
  name: "TrendAnalysisView",
  data() {
    return {
      currentPath: window.location.hash,
      formType: null,
      formDescription: null,
      formFields: [],
      formNames: [],
      factors: [],
      options: [],
      form: null,
      formTypes: [],
      startDate: null,
      endDate: null,
      showComments: true,
      viewType: 'graph',
      reportTypes: [],
      filter: {
        clientId: 0,
        formName: null,
        factors: [],
        start: null,
        end: null
      }
    }
  },
  setup() {
    const store = trendStore()
    return {
      store,
    }
  },
  mounted() {
    // dummy list of report types
    formSearch()


  }, components: {
    InterventionsTable,
    CommentsTable,
    TrendChart,
    DataView,
    ChartFilter,
  },
  methods: {
    getSampleForm(formName) {
      console.log("Getting form %s", formName);
      axios
          .get('http://localhost:8888/client/form/' + formName)
          .then((response) => {
            this.form = response.data;
            this.getFormFields();
          });

    },
    changeFactors(factors) {
      console.log("Updating factors %o %o", factors);
      this.filter.factors = factors;
    },
    changeView(view) {
      console.log("Updating view %o", view);
      this.viewType = view;
    },
  }
}
</script>

<style scoped>
.filters {
  background-color: #EEEEEE;
  min-height: 80px;
}

.divider-right {
  border-right: 1px #AAAAAA solid;
}

.filter-label {
  text-align: left;
  font-weight: bold;
  margin-top: 8px;
  margin-bottom: 8px;
}

.multiselect__tag {
  background-color: darkslateblue !important;
}
</style>