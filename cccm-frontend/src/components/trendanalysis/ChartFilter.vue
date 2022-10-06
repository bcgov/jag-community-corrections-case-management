<template>
  <div data-app class="row filters p-4">
    <v-tabs v-model="reportTab" fixed-tabs color="deep-purple accent-4" @change="chartTypeChangeHandler">
      <v-tab v-for="item in reportTypes" :key="item.tab">
        {{ item.tab }}
      </v-tab>
    </v-tabs>
    <div class="col-md-3 col-sm-1 divider-right">
      <div class="filter-label">Date Range</div>
      <div class="d-flex justify-content-evenly align-items-center pb-3">
        <input id="startDate" v-model="userStartDate" @change="changeStartDate" class="form-control ms-3 me-3 mr-2"
          type="date" />
        <small>to</small>
        <input id="endDate" v-model="userEndDate" @change="changeEndDate" class="form-control ms-3 me-3 ml-2"
          type="date" />
      </div>
    </div>
    <div class="col-md-3 col-sm-1 divider-right">
      <div class="filter-label">Supervision Periods</div>
      <div class="d-flex justify-content-center">
        <v-radio-group label="" v-model="period" row v-on:change="changePeriods">
          <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="All Supervision Periods"
            value="allPeriods"></v-radio>
          <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="Current Supervision Period"
            value="currentPeriod"></v-radio>
        </v-radio-group>

      </div>

    </div>
    <div class="col-md-3  col-sm-1 divider-right">
      <div class="filter-label">Factor View</div>

      <v-select v-model="selectedFactors" item-text="label" item-value="value" :items="factorOptions" chips
        @blur="updateFactors()" :menu-props="{ maxHeight: '400' }"  multiple
        hint="Select one or more factors" persistent-hint>
      </v-select>
      <!-- 
      <v-select v-model="filter.factors"       item-text="label"
          item-value="value" :items="factorOptions"  @close="changeFactors()" @remove="removeFactor"
        :menu-props="{ maxHeight: '400' }" label="Select" multiple hint="Select one or more factors" persistent-hint>
      </v-select> -->

      <!-- <VueMultiselect
          :multiple="true"
          v-model="filter.factors"
          :close-on-select="false"
          @close="changeFactors()"
          @remove="removeFactor"
          track-by="id"
          label="name"
          :taggable="false"
          placeholder="Select one or more factors"
          :options="factors"/> -->


    </div>
    <div class="col-md-3  col-sm-1">
      <div class="filter-label">Display View</div>
      <v-select v-model="selectedFilter" item-text="label" item-value="value" :items="filterOptions" 
        @change="updateFilter()" :menu-props="{ maxHeight: '400' }" 
        hint="Select a display filter" persistent-hint>
      </v-select>


    </div>
    <v-progress-linear v-if="loading" indeterminate height="30" color="primary">Loading data...</v-progress-linear>

  </div>
</template>

<script>

import { trendStore } from '@/stores/trendstore';
import { mapStores, mapState, mapWritableState } from "pinia/dist/pinia";
import { getClientForms, getFormFactors, getTrendChartTypes, getChartData } from "@/components/form.api";


export default {
  name: "ChartFilter",
  components: {},
  setup() {
    const store = trendStore()
    return {
      store,
    }
  },
  computed: {
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    // ...mapWritableState(trendStore, ['data','factors', 'minStartDate', 'maxEndDate', 'advancedFilterOptions', 'startDate', 'endDate']),
    ...mapStores(trendStore, ['factors', 'minStartDate', 'maxEndDate', 'advancedFilterOptions', 'startDate', 'endDate']),

  },
  data() {
    return {
      expanded: null,
      loading: false,
      userStartDate: null,
      maxEndDate: null,
      minStartDate: null,
      filterOptions: [],
      period: 'allPeriods',
      chartType: null,
      selectedFactors: [],
      userEndDate: null,
      selectedFilter: null,
      factorOptions: [],
      reportTypes: [
      ],
      reportTab: 0,

    }
  },
  props: {},
  mounted() {
    this.getChartTypes().then(() => {
      let chartType = this.reportTypes[this.reportTab].content;
      this.store.$patch({ chartType: chartType });

      this.getFormFactors();
      this.getFilterOptions();
    });

    this.store.$subscribe(async (mutation, state) => {

      // chart type or period changed
      if (mutation.payload) {
        console.log("Filters updated from chart data %o %o", mutation, state);

        // chart change or period change reloads back-end data
        if ( mutation.payload) {
          if ( mutation.payload.chartType) {
            console.log("Changing chart type");
            this.userStartDate = null;
            this.userEndDate = null;
            await this.loadData();

          }
           else if ( mutation.payload.period && mutation.payload.period !== this.period ) {
            console.log("Changing period");
            await this.loadData();

          }
        }
      


        if (mutation.payload.data || mutation.payload.factors ) {

          console.log("Updating counters for factors %o", mutation.payload.factors);
          let commentCount = 0;
          let interventionCount = 0;
          let filteredDatasets = this.store.data.datasets.filter((dataset) => {
            return this.store.factors.includes(dataset.source);
          });
          filteredDatasets.forEach(dataset => {
            console.log("Checking dataset %o", dataset);
            commentCount += dataset.comments.length;
            interventionCount += dataset.interventions.length;
          });

          this.store.$patch({ commentCount: commentCount, interventionCount: interventionCount });

        }

      }


    });

  },
  updated() {


  },

  methods: {
    async loadData() {
      console.log("Getting data for %s", this.store.chartType);
      this.loading = true;
      let csNumber = this.$route.params.csNumber;
      const filter = {
        factors: [],
        chartType: this.store.chartType,
        currentPeriod: this.store.period === 'currentPeriod' ? true : false,
        clientNumber: csNumber,
        includeComments: true,
        includeInterventions: true
      }
      try {
        await getChartData(filter).then(([error, data]) => {
          if (error) {
            console.error(error);

          } else {
            console.log("Got data %o", data);
            let xSeries = data.dataLabels;

            // Get counters
            let commentCount = data.counters.comments ? data.counters.comments : 0;
            let interventionCount = data.counters.interventions ? data.counters.interventions : 0;

            if (xSeries.length !== 0) {

              this.maxEndDate = xSeries[xSeries.length - 1];
              this.minStartDate = xSeries[0];
              // patch the pinia store
              this.store.$patch({ data: data, minStartDate: xSeries[0], maxEndDate: xSeries[xSeries.length - 1], interventionCount: 0, commentCount: 0 });
              if (!this.userStartDate) {
                this.userStartDate = this.minStartDate;
              }

              if (!this.userEndDate) {
                this.userEndDate = this.maxEndDate;
              }
            } else {
              this.userStartDate = null;
              this.userEndDate = null;
              this.store.$patch({ data: data, minStartDate: null, maxEndDate: null, interventionCount: 0, commentCount: 0 });

            }
          }
        });
      } catch (error) {
        console.error(error);
      } finally {
        this.loading = false;
      }
    },
    chartTypeChangeHandler() {
      this.chartType = this.reportTypes[this.reportTab].content;
      this.filterOptions = this.reportTypes[this.reportTab].filters;
      this.selectedFactors = [];
      this.userStartDate = null;
      this.userEndDate = null;
      this.store.$patch({ chartType: this.chartType, factors: [] })
      this.getFormFactors();

    },
    clearSelections() {

    },
    getMinStart() {
      return this.minStartDate;
    },
    updateFilter() {
      console.log("Apply filter %o", this.selectedFilter);
    },
    changeStartDate() {
      console.log("Start date changed...");
    },
    changeEndDate() {
      console.log("End date changed...");
    },
    getMaxEnd() {
      return this.maxEndDate;
    },
    datesChanged() {

    },
    async getChartTypes() {
      const [error, data] = await getTrendChartTypes();
      if (error) {
        console.error(error);
      } else {
        console.log("Got chart types %o", data);
        data.forEach(type => {
          console.log("Add type %o", type);
          debugger;
          this.reportTypes.push({ tab: type.description, content: type.type, filters: type.filters });
        });
      }
    },
    async getFilterOptions() {
      this.filterOptions = this.reportTypes[this.reportTab].filters;
    },
    async getFormFactors() {
      let chartType = this.reportTypes[this.reportTab].content;
      const [error, data] = await getFormFactors(chartType);
      if (error) {
        console.error(error);
      } else {
        console.log("Got factors %o", data);
        this.factorOptions = data;
      }
    },


    removeAdvancedFilter() {
      this.filter.advancedFilter = null;
      // this.$store.commit('updateAdvancedFilter', this.filter.advancedFilter);
    },
    changeAdvancedFilter(value) {
      console.log("Changing advanced filter to %o %d", value);
      this.filter.advancedFilter = value;
      // this.$store.commit('updateAdvancedFilter', this.filter.advancedFilter);
    },
    removeFactor(factor) {
      console.log("Removed %o %s", factor);
      let removedIdx = this.filter.factors.findIndex(filter => filter.id === factor.id);
      this.filter.factors.splice(removedIdx, 1);
      console.log("Removed %o", removedIdx);
      // eslint-disable-next-line no-debugger

      // this.$store.commit('updateFactors', this.filter.factors);
    },
    updateFactors() {
      console.log("Filter update", this.selectedFactors);
      //  this.trendstore.commit('updateFactors', this.filter.factors);
      this.store.$patch({ factors: this.selectedFactors })

    },
    changePeriods() {
      console.log("Filter updating periods %o", this.period);
      let chartType = this.reportTypes[this.reportTab].content;
      this.filterOptions = this.reportTypes[this.reportTab].filters;
      this.userStartDate = null;
      this.userEndDate = null;
      this.store.$patch({ chartType: chartType, period: this.period })
      this.getFormFactors();

    },
    changeStartDate() {
      console.log("Filter start date %o %o", this.filter.startDate);
      // this.$store.commit('userUpdateStartDate', this.filter.startDate);
      // this.trendstore.commit('userUpdateStartDate', this.filter.startDate);

    },
    changeEndDate() {
      console.log("Filter end date %o", this.filter.endDate);
      // this.$store.commit('userUpdateEndDate', this.filter.endDate);
      // this.trendstore.commit('userUpdateEndDate', this.filter.endDate);

    }
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

