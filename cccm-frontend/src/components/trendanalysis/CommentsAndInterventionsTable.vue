<template>
  <div data-app class="row p-4">
    <div class="justify-content-center mb-2 col-10">
      <v-data-table v-if="showIntervention" item-key="comment.id" class="elevation-1" :headers="headers" :items="data" :items-per-page="15">
        <template v-slot:item.interventions="{item}">
          <table class="nestedIntervention">
            <tbody>
              <tr v-for="intervention in item.interventions">
                <td style="width:10%">{{intervention.type == 'OTHR' ? intervention.typeOverride : intervention.type}}</td>
                <td style="width:90% word-wrap:break-word">{{intervention.comment}}</td>
              </tr>
            </tbody>
          </table>
        </template>
      </v-data-table>
      <v-data-table v-else item-key="comment.id" class="elevation-1" :headers="header_noIntervention" :items="data" :items-per-page="15">
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
    showIntervention() {
      let theForm = this.$FORM_INFO.filter( item => item.formType === 'CRNA');
      if (theForm != null && theForm[0] != null) {
          return theForm[0].cmp;
      }
      return false;
    },
    ...mapState(trendStore, ['factors', 'startDate', 'endDate', 'clientNumber', 'chartType', 'formType'])
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
        { text: 'Date', value: 'createdDate', width: '10%' },
        { text: 'Factor', value: 'factor', width: '15%' },
        { text: 'Rating', value: 'answerValue', width: '5%' },
        { text: 'Comment', value: 'value', width: '30%' },
        { text: 'Interventions', value: 'interventions', width: '40%' },
      ],
      header_noIntervention: [
        { text: 'Date', value: 'createdDate', width: '10%' },
        { text: 'Factor', value: 'factor', width: '20%' },
        { text: 'Rating', value: 'answerValue', width: '10%' },
        { text: 'Comment', value: 'value', width: '60%' },
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
.nestedIntervention {
  width: 100%;
  height: 100%;
}
.nestedIntervention tr:nth-child(even)  {
   background-color:#F2F2F2;
}

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