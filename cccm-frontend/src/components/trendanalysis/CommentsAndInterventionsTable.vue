<template>
  <div data-app class="row p-4">
    <div class="justify-content-center mb-2 col-10">
      <v-data-table item-key="comment.id" class="elevation-1" :headers="headers" :items="data" :items-per-page="10">
        <template v-slot:item.interventions="{item}">
          <table width="100%" height="100%" >
            <tbody>
              <tr v-for="intervention in item.interventions">
              <td class="text-start">{{intervention.type}}</td>
              <td>{{intervention.comment}}</td>
              </tr>
            </tbody>
          </table>

        </template>


      </v-data-table>
    </div>
  </div>
</template>
  
<script>
import { mapState, mapStores } from "pinia";
import { trendStore } from '../../stores/trendstore';

import axios from "axios";

export default {
  name: "CommentsAndInterventionsTable",
  computed: {
    ...mapState(trendStore, ['factors', 'startDate', 'endDate', 'clientNumber', 'chartType'])
  },
  mounted() {
    console.log("Showing comments and interventions for %o", this.store.factors);
    this.applyFilters();

    this.store.$subscribe((mutation, state) => {
      if (mutation.payload) {
        console.log("Trend store changed - getting comments...%o %o", mutation, state);
        this.applyFilters();

      }

    });
  },
  setup() {
    const store = trendStore()
    return {
      store,
    }
  },
  data() {
    return {
      data: [],
      headers: [
        {
          text: 'ID (Debug)',
          align: 'start',
          sortable: false,
          value: 'id',
        },
        { text: 'Date', value: 'createdDate' },
        { text: 'Factor', value: 'factor' },
        { text: 'Rating', value: 'answerValue' },
        { text: 'Comment', value: 'value' },
        { text: 'Interventions', value: 'interventions' },


      ],
    }
  },
  methods: {
    async applyFilters() {
      if (this.store.data) {
        let filteredDatasets = this.store.data.datasets.filter((dataset) => {
          return this.store.factors.includes(dataset.source);
        });

        this.data = [];
        filteredDatasets.forEach(dataset => {
          let comments = dataset.comments;
          let interventions = dataset.interventions;


          console.log("Interventions %o", interventions);
          comments.forEach(comment => {
            
            comment.interventionTypes = [];
            comment.interventionComments = [];
            comment.interventions = interventions.filter(intervention => intervention.relatedAnswerId === comment.id) || [];
            this.data.push(comment);
            // see if we have interventions
            // comment.interventionTypes.push(...interventions.filter(intervention => intervention.relatedAnswerId === comment.id);

            let interventionTypes = interventions.filter(intervention => intervention.relatedAnswerId === comment.id).map(a => a.type);
            let interventionComments = interventions.filter(intervention => intervention.relatedAnswerId === comment.id).map(a => a.comment);
            comment.interventionTypes.push(...interventionTypes);
            comment.interventionComments.push(...interventionComments);

            console.log("Handling comment %o %o", interventionTypes, interventionComments);


          });
          console.log("Combined data %o ", this.data);

        });

      }
    },

    getRowClass(comment) {
      // eslint-disable-next-line no-debugger
      console.log("Checking row %o", comment);
      return (comment.date === this.pointDateSelected) ? "blue" : "";

    }
  }
}
</script>
  
<style scoped>
.client-comments {
  margin-left: 24px;
}

tr.blue {
  background-color: cadetblue;
  color: white;
}

tr.blue:hover {
  color: white;
}

.date {
  white-space: nowrap;
}
</style>