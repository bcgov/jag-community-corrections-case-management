<template>
  <div data-app class="row filters p-2">
    <v-tabs v-model="reportTab" fixed-tabs color="deep-purple accent-4" @change="chartTypeChangeHandler">
      <v-tab v-for="item in reportTypes" :key="item.tab">
        {{ item.tab }}
      </v-tab>
    </v-tabs>
    <div class="col-md-3 col-sm-1 divider-right">
      <div class="filter-label">Date Range</div>
      <section class="row justify-content-between align-items-sm-center pr-2 pl-2">
        <div class="col-sm-5">
          <small>Start Date: </small><input id="startDate" v-model="userStartDate" :min="minStartDate" @change="changeStartDate"
            class="form-control ms-3 me-3 mr-2" type="datetime-local" /> 
        </div> 
        <div class="col-sm-5">
          <small>End Date: </small><input id="endDate" v-model="userEndDate" :max="maxEndDate" @change="changeEndDate"
            class="form-control ms-3 me-3 ml-2" type="datetime-local" />
        </div>
        <div class="col-sm-1">
          <small>&nbsp; </small><button v-on:click="resetDates" title="Reset Dates"><a class="fas fa-undo-alt" /></button>
        </div>
      </section>
      
    </div>
    <div class="col-md-3 col-sm-1 divider-right">
      <div class="filter-label">Supervision Periods</div>
      <div class="d-flex justify-content-center">
        <v-radio-group label="" v-model="period" row v-on:change="changePeriods">
          <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="All Periods" value="allPeriods">
          </v-radio>
          <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="Current Period"
            value="currentPeriod"></v-radio>
        </v-radio-group>

      </div>

    </div>
    <div class="col-md-3  col-sm-1 divider-right">
      <div class="filter-label">Factor View</div>
      <v-select v-model="selectedFactors" item-text="label" item-value="value" :items="factorOptions" chips
        @blur="updateFactors()" :menu-props="{ maxHeight: '400' }" multiple hint="Select one or more factors"
        persistent-hint>
        <template v-slot:selection="{ item, index }">
          <span v-if="index === 0">
            <b class="ml-6" style="margin-left:8px">{{ selectedFactors.length }}</b> factor(s) selected
          </span>
        </template>
      </v-select>

    </div>
    <div class="col-md-3  col-sm-1">
      <div class="filter-label">Display View</div>
      <v-select v-model="selectedFilter" item-text="label" item-value="value" :items="filterOptions"
        @change="updateFilter()" :menu-props="{ maxHeight: '400' }" hint="Select a display filter" persistent-hint>
      </v-select>


    </div>
    <v-overlay :value="loading">
      <v-progress-circular
        indeterminate :width="6"
        size="200"
      >
      <span class="mt-30">Loading chart data...</span>

    </v-progress-circular>

    </v-overlay>


  </div>
</template>

<script>

import { trendStore } from '@/stores/trendstore';
import { useStore } from "@/stores/store";
import { mapStores, mapState, mapWritableState } from "pinia";
import { getFormFactors, getTrendChartTypes, getChartData } from "@/components/form.api";


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
    ...mapStores(trendStore, ['factors', 'minStartDate', 'maxEndDate', 'advancedFilterOptions', 'startDate', 'endDate'], useStore),

  },
  data() {
    return {
      expanded: null,
      loading: false,
      userStartDate: null,
      filterOptions: [],
      period: 'allPeriods',
      maxEndDate: null,
      minStartDate: null,
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
        // chart change or period change reloads back-end data
        if (mutation.payload.chartType) {
          console.log("Changing chart type");
          this.userStartDate = null;
          this.userEndDate = null;
          this.selectedFilter = null;
          await this.loadData();
        }
        else if (mutation.payload.period && mutation.payload.period !== this.period) {
          console.log("Changing period");
          await this.loadData();
        }

        if (mutation.payload.factors || mutation.payload.period || mutation.payload.data || mutation.payload.startDate || mutation.payload.endDate || mutation.payload.advancedFilter) {
          this.applyDateFilters();
        }
      }
    });
  },
  methods: {
    resetDates() {
      this.userStartDate = this.store.minStartDate;
      this.userEndDate = this.store.maxEndDate;
      this.store.$patch({ startDate: this.userStartDate, endDate: this.userEndDate });
    },
    async loadData() {
      console.log("ChartFilter: Getting data for %s", this.store.chartType);

      this.loading = true;
      let csNumber = this.$route.params.csNumber;
      let locationCD = this.mainStore.locationCD;

      const filter = {
        factors: [],
        chartType: this.store.chartType,
        currentPeriod: this.store.period === 'currentPeriod' ? true : false,
        clientNumber: csNumber,
        includeComments: true,
        includeInterventions: true,
        locationId: locationCD
      }

      try {
        await getChartData(filter).then(([error, data]) => {
          if (error) {
            console.error(error);
          } else {
            //console.log("Got data %o", data);
            let xSeries = data.dataLabels;

            this.minStartDate = data.startDateRange; 
            this.maxEndDate = data.endDateRange;
 
            // Get counters
            let commentCount = data.counters.comments ? data.counters.comments : 0;
            let interventionCount = data.counters.interventions ? data.counters.interventions : 0;

            if (xSeries.length !== 0) {
              // patch the pinia store
              this.store.$patch({ data: data, minStartDate: this.minStartDate, maxEndDate: this.maxEndDate, interventionCount: interventionCount, commentCount: commentCount, startDate: this.minStartDate, endDate: this.maxEndDate });
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
      this.store.$patch({ chartType: this.chartType, factors: [], advancedFilter: null, filteredData: null });
      this.getFormFactors();
      this.getFilterOptions();
    },
    clearSelections() {

    },
    getMinStart() {
      return this.minStartDate;
    },
    updateFilter() {
      // get the filter from the store
      let filter = this.reportTypes[this.reportTab].filters.filter(option => option.value === this.selectedFilter)[0];
      console.log("updateFilter: ", filter);
      this.store.$patch({ advancedFilter: filter })
    },
    changeStartDate() {
      console.log("Start date changed...");
      this.store.$patch({ startDate: this.userStartDate });
    },
    changeEndDate() {
      console.log("End date changed...");
      this.store.$patch({ endDate: this.userEndDate });
    },
    getMaxEnd() {
      return this.maxEndDate;
    },
    applyDateFilters() {
      //console.log("applyDateFilters...");
      let startDate = (this.store.startDate) ? new Date(this.store.startDate) : null;
      let endDate = (this.store.endDate) ? new Date(this.store.endDate) : null;

      if (this.store.factors === null || this.store.factors.length === 0 || this.store.data === null) {
        return;
      }
      if (this.store.data && this.store.data.dataLabels) {
        let labels = [];
        let index = 0;
        let startIndex = 0;
        let endIndex = this.store.data.dataLabels.length - 1;
        this.store.data.dataLabels.forEach(label => {
          let seriesDate = new Date(label);
          if (startDate != null && seriesDate < startDate) {
            startIndex++;
          }
          if (endDate != null && endDate < seriesDate) {
            endIndex--;
          }
          index++;
        });
        
        let filteredDatasets = new Array();
        if(this.store.data.datasets) {
          if (this.store.factors === null || this.store.factors.length === 0) {
              filteredDatasets = this.store.data.datasets;
          } else {
            let storedDatasets = this.store.data.datasets;
            
            if(this.selectedFactors != null && this.selectedFactors.length > 0) {
              for(let i = 0; i < this.selectedFactors.length; i++) {
                for(let j = 0; j < storedDatasets.length; j++) {
                  if(this.selectedFactors[i] == storedDatasets[j].source) {
                    filteredDatasets.push(storedDatasets[j]);
                    break;
                  }
                }
              }

            } else {
              for(let i = 0; i < this.store.factors.length; i++) {
                for(let j = 0; j < storedDatasets.length; j++) {
                  if(this.store.factors[i].value == storedDatasets[j].source) {
                    filteredDatasets.push(storedDatasets[j]);
                    break;
                  }
                }
              }
            }
          }
        }

        filteredDatasets = this.applyAdvancedFilter(filteredDatasets);
        let datasets = [];
        filteredDatasets.forEach(ds => {
          let data = [];
          for (let i = startIndex; i <= endIndex; i++) {
            data.push(ds.data[i]);
          }
          let copiedDs = JSON.parse(JSON.stringify(ds));

          copiedDs.data = data;
          datasets.push(copiedDs);
        });
        for (let i = startIndex; i <= endIndex; i++) {
          labels.push(this.store.data.dataLabels[i]);
        }

        // this.applyFilters(labels, datasets);
        let filtered = {
          datasets: datasets,
          labels: labels
        }
        let commentCount = 0;
        let interventionCount = 0;
        datasets.forEach(dataset => {
          if ( !dataset.hidden) {
            dataset.comments = dataset.comments;
            dataset.interventions = dataset.interventions;
            if(dataset.comments) {
              commentCount += dataset.comments.length;
            }
            if(dataset.interventions) {
              interventionCount += dataset.interventions.length;
            }
          }
        });

        this.store.$patch({ filteredData: filtered, commentCount: commentCount, interventionCount: interventionCount });
      }

    },
    async getChartTypes() {
      const [error, data] = await getTrendChartTypes();
      if (error) {
        console.error(error);
      } else {
        data.forEach(type => {
          this.reportTypes.push({ tab: type.description, content: type.type, filters: type.filters });
        });
      }
    },
    async getFilterOptions() {
      this.filterOptions = this.reportTypes[this.reportTab].filters;
      this.selectedFilter = this.filterOptions == null ? null : this.filterOptions[0];
    },
    async getFormFactors() {
      let chartType = this.reportTypes[this.reportTab].content;
      const [error, data] = await getFormFactors(chartType);
      if (error) {
        console.error(error);
      } else {
        this.factorOptions = data;
        this.store.$patch({ factors: data });
        if(this.selectedFactors == null || this.selectedFactors.length == 0) {
          // by default, all should be selected
          this.selectedFactors = new Array();
          if(this.factorOptions != null && this.factorOptions.length > 0) {
            for(let i = 0; i < this.factorOptions.length; i++) {
              this.selectedFactors.push(this.factorOptions[i].value);
            }
          }
        }
        this.applyDateFilters();
      }
    },
    applyAdvancedFilter(datasets) {
      let filter = this.store.advancedFilter;
      console.log("applyAdvancedFilter filter: ", filter);
      if (filter) {
        datasets.forEach(ds => {
          ds.hidden = false;
          const lastTwo = ds.data.slice(-2);
          switch (filter.value) {
            case '': {
              ds.hidden = null;
              break;
            }
            case 'improved': {
              if (lastTwo[1] > lastTwo[0]) {
                ds.hidden = false;
              } else {
                ds.hidden = true;

              }
              break;
            }
            case 'worsened': {
              if (lastTwo[1] < lastTwo[0]) {
                ds.hidden = false;

              } else {
                ds.hidden = true;
              }
              break;
            }
            case 'remained-c-d': {
              if ((lastTwo[1] === 0 && lastTwo[0] === 0) || (lastTwo[1] === 1 && lastTwo[0] === 1)) {
                ds.hidden = null;
              } else {
                ds.hidden = true;
              }

              break;
            }
            case 'remained-a-b': {
              if ((lastTwo[1] === 2 && lastTwo[0] === 2) || (lastTwo[1] === 3 && lastTwo[0] === 3)) {
                ds.hidden = null;
              } else {
                ds.hidden = true;

              }

              break;
            }
            default: {
              console.log("Not handling %s", filter.condition);
            }

          }
        });
      }
      return datasets;
    },

    removeAdvancedFilter() {
      this.filter.advancedFilter = null;
    },
    changeAdvancedFilter(value) {
      this.filter.advancedFilter = value;
    },
    removeFactor(factor) {
      let removedIdx = this.filter.factors.findIndex(filter => filter.id === factor.id);
      this.filter.factors.splice(removedIdx, 1);
    },
    updateFactors() {
      this.store.$patch({ factors: this.selectedFactors })
    },
    changePeriods() {
      let chartType = this.reportTypes[this.reportTab].content;
      this.filterOptions = this.reportTypes[this.reportTab].filters;
      this.userStartDate = null;
      this.userEndDate = null;
      this.store.$patch({ chartType: chartType, period: this.period })
      this.getFormFactors();
    }
  }
}
</script>

<style scoped>
.filters {
  background-color: #EEEEEE;
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

