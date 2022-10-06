<template>
  <div data-app class="row p-4">
    <div class="justify-content-center mb-2 col-10">
      <v-data-table item-key="invervention.id" class="elevation-1" :headers="headers" :items="interventions"
        :items-per-page="10"></v-data-table>
    </div>
  </div>
</template>

<script>
// import {mapState} from "vuex";
import axios from "axios";
import { mapState, mapStores } from "pinia/dist/pinia";
import { trendStore } from '@/stores/trendstore';

export default {
  name: "InterventionsTable",

  computed: {
    ...mapState(trendStore, ['factors', 'startDate', 'endDate', 'clientNumber', 'chartType'])
  },
  setup() {
    const store = trendStore()
    return {
      store,
    }
  },
  mounted() {
    console.log("Showing interventions...%o", this);
    this.applyFilters();

    this.store.$subscribe((mutation, state) => {
      if (mutation.payload) {
        console.log("Trend store changed - getting interventions...%o %o", mutation, state);
        this.applyFilters();

      }

    });


  },
  data() {
    return {
      interventions: [],
      headers: [
        {
          text: 'ID (Debug)',
          align: 'start',
          sortable: false,
          value: 'id',
        },
        { text: 'Entry Date', value: 'createdDate' },
        { text: 'Factor', value: 'factor' },
        { text: 'Intervention Type', value: 'type' },
        { text: 'Description', value: 'comment' }
      ],
    }
  },
  methods: {
    async applyFilters() {
      let filteredDatasets = this.store.data.datasets.filter((dataset) => {
        return this.store.factors.includes(dataset.source);
      });

      this.comments = [];
      filteredDatasets.forEach(dataset => {
        this.interventions.push(...dataset.interventions);
      });

    },
  },
  watch: {
    pointDateSelected: {
      handler: function (newValue, oldValue) {
        console.log("Highlighting selected date %s %s", newValue, oldValue);
      }
    },
  },
}
</script>

<style scoped>
.client-interventions {
  margin-left: 24px;
}

.date {
  white-space: nowrap;
}
</style>