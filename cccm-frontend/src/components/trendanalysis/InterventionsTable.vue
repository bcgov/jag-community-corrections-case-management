<template>
  <div class="client-interventions col-8 justify-content-start">
    <div class="row">
      <table class="table table-hover table-bordered interventions-table">
        <thead>
        <tr style="background-color: #0a58ca; color: white">
          <th>Date</th>
          <th>Factor</th>
          <th>Rating</th>
          <th>Comment</th>

        </tr>
        </thead>
        <tbody>
        <tr v-for="intervention in interventions" v-bind:key="intervention.id">
          <td class="date">{{ intervention.date }}</td>
          <td>{{ intervention.factor }}</td>
          <td>{{ intervention.intervention }}</td>
          <td style="text-align: justify">{{ intervention.detail }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
// import {mapState} from "vuex";
import axios from "axios";

export default {
  name: "InterventionsTable",

  // computed: mapState(['factors', 'startDate', 'endDate', 'period', 'chartFilters', 'clientId', 'interventionCount', 'pointDateSelected']),
  mounted() {
    console.log("Showing interventions...%o", this);
    let factors = this.factors.map(factor => {
      return factor.name;
    });
    axios
        .get('http://localhost:8888/client/interventions/crna', {
          params: {
            factors: String(factors),
            period: this.period,
            clientId: this.clientId,
            startDate: this.startDate,
            endDate: this.endDate,
            count: this.interventionCount,
            advancedFilter: this.advancedFilter
          },
        })
        .then((response) => {
          console.log("Got response %o", response);
          this.interventions = response.data;
        });

  },
  data() {
    return {
      interventions: []
    }
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