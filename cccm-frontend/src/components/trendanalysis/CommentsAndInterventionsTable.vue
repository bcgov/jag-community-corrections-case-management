<template>
  <div data-app class="row p-4">
    <div class="justify-content-center mb-2 col-10">
      <v-data-table item-key="comment.id" class="elevation-1" :headers="headers" :items="data" :items-per-page="10">
        <template v-slot:item.interventions="{item}">
          <table width="100%" height="100%">
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
    this.applyFilters();

    this.store.$subscribe((mutation, state) => {
      if (mutation.payload && mutation.payload.filteredData) {
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
        { text: 'Date', value: 'createdDate', width: '10em' },
        { text: 'Factor', value: 'factor', width: '20%' },
        { text: 'Rating', value: 'answerValue', width: '8em' },
        { text: 'Comment', value: 'value' },
        { text: 'Interventions', value: 'interventions' },


      ],
    }
  },
  methods: {
    async applyFilters() {
      this.data = [];
      if (this.store.filteredData) {
        this.store.filteredData.datasets.forEach(dataset => {
          if (!dataset.hidden) {
            let comments = dataset.comments;
            let interventions = dataset.interventions;

            comments.forEach(comment => {
              if(comment) {
                this.data.push(comment);
                if(interventions) {
                  comment.interventions = interventions.filter(intervention => intervention.relatedAnswerId === comment.id) || [];
                }
              }
              
              

            });
          }

        });
      }


    },

    getRowClass(comment) {
      // eslint-disable-next-line no-debugger
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