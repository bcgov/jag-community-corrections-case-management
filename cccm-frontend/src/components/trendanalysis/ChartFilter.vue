<template>
  <div data-app class="row filters p-4">
    <v-tabs v-model="reportTab" fixed-tabs color="deep-purple accent-4" @change="chartTypeChangeHandler">
      <v-tab v-for="item in reportTypes" :key="item.tab">
        {{ item.tab }}
      </v-tab>
    </v-tabs>
    <div class="col-md-3 col-sm-1 divider-right">
      <div class="filter-label">Date Range</div>
      <div class="d-flex justify-content-evenly pb-3">
        <input id="startDate" v-model="filter.startDate" @change="changeStartDate" class="form-control ms-3 me-3"
          type="date" />
        to
        <input id="endDate" v-model="filter.endDate" @change="changeEndDate" class="form-control ms-3 me-3"
          type="date" />
      </div>
    </div>
    <div class="col-md-3  col-sm-1 divider-right">
      <div class="filter-label">Supervision Periods</div>
      <div class="d-flex justify-content-center">
        <div class="form-check">
          <input class="form-check-input" type="radio" name="periodRadio" id="allPeriodsRadio" value="allPeriods"
            v-model="filter.periods" @change="changePeriods">
          <label class="form-check-label" for="flexRadioDefault1">
            All periods
          </label>
        </div>
        <div class="form-check ml-3">
          <input class="form-check-input" type="radio" name="periodRadio" id="currentPeriodRadio" value="currentPeriod"
            v-model="filter.periods" @change="changePeriods">
          <label class="form-check-label" for="flexRadioDefault2">
            Current period
          </label>
        </div>
      </div>

    </div>
    <div class="col-md-3  col-sm-1 divider-right">
      <div class="filter-label">Factor View</div>

      <v-select v-model="filter.factors" item-text="label" item-value="value" :items="factorOptions"
        @blur="changeFactors()" :menu-props="{ maxHeight: '400' }" label="Select" multiple
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



    </div>
  </div>
</template>

<script>

import { trendStore } from '@/stores/trendstore';
import { mapStores, mapState, mapWritableState } from "pinia/dist/pinia";
import { getClientForms, getFormFactors, getTrendChartTypes } from "@/components/form.api";


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
    ...mapState(trendStore, ['factors', 'minStartDate', 'maxEndDate', 'advancedFilterOptions', 'startDate'])
  },
  data() {
    return {
      expanded: null,
      factorOptions: [],
      reportTypes: [
      ],
      reportTab: 0,
      filter: {
        factors: [],
        periods: 'allPeriods',
        minStartDate: null,
        maxEndDate: null,
        startDate: null,
        endDate: null,
        advancedFilter: null
      }
    }
  },
  props: {},
  mounted() {
    this.getChartTypes().then(() => {

      this.getFormFactors();
    });

  },
  updated() {

    console.log("Chart filter updated");
    if (!this.filter.startDate) {
      this.filter.startDate = this.minStartDate;
    }
    if (!this.filter.endDate) {
      this.filter.endDate = this.maxEndDate;
    }
  },
  watch: {
    advancedFilterOptions: {
      handler: function (options) {
        console.log("Filter options updated!! %o", options);
        let optionSelections = this.advancedFilterOptions.map(option => {
          return { name: option, label: option };
        })
        this.filter.options = optionSelections;

      },
    },
    startDate: {
      handler: function (newValue, oldValue) {
        console.log("Start date updated!! %s %s", newValue, oldValue);
        this.filter.startDate = this.startDate;
        this.changeStartDate();

      },
    },
    endDate: {
      handler: function (newValue, oldValue) {
        console.log("End date updated!! %s %s", newValue, oldValue);
        this.filter.endDate = this.endDate;
        this.changeEndDate();
      },
    },
    factors: {
      handler: function (newValue, oldValue) {
        console.log("Factors updated from chart %o %o", newValue, oldValue);

      }
    }
  },
  methods: {
    chartTypeChangeHandler() {

      this.getFormFactors();

    },
    getMinStart() {
      return this.minStartDate;
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

          this.reportTypes.push({ tab: type.description, content: type.type });
        });
      }
    },
    async getFormFactors() {
      let chartType = this.reportTypes[this.reportTab].content;
      this.store.$patch({ chartType: chartType })
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
    changeFactors() {
      // eslint-disable-next-line no-debugger
      console.log("Filter updating factors %o", this.filter.factors);
      //  this.trendstore.commit('updateFactors', this.filter.factors);
      this.store.$patch({ factors: this.filter.factors })

    },
    changePeriods() {
      console.log("Filter updating periods %o", this.filter.periods);
      // this.$store.commit('updatePeriod', this.filter.periods);
      this.filter.startDate = undefined;
      this.filter.endDate = undefined;
    },
    changeStartDate() {
      console.log("Filter start date %o %o", this.filter.startDate);
      // this.$store.commit('userUpdateStartDate', this.filter.startDate);
      this.trendstore.commit('userUpdateStartDate', this.filter.startDate);

    },
    changeEndDate() {
      console.log("Filter end date %o", this.filter.endDate);
      // this.$store.commit('userUpdateEndDate', this.filter.endDate);
      this.trendstore.commit('userUpdateEndDate', this.filter.endDate);

    }
  },
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

