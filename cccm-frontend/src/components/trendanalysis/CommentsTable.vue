<template>
  <div class="client-comments col-10 justify-content-start">
    {{ this.pointDateSelected }}
    <div class="row">
      <table class="table table-hover table-bordered comments-table">
        <thead>
        <tr style="background-color: #0a58ca; color: white">
          <th>Date</th>
          <th>Factor</th>
          <th>Rating</th>
          <th>Comment</th>

        </tr>
        </thead>
        <tbody>
        <tr :class="getRowClass(comment)" v-for="comment in comments" v-bind:key="comment.id">
          <td class="date">{{ comment.date }}</td>
          <td>{{ comment.factor }}</td>
          <td>{{ comment.rating }}</td>
          <td style="text-align: justify">{{ comment.detail }}</td>
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
  name: "CommentsTable",
  // computed: mapState(['factors', 'startDate', 'endDate', 'period', 'chartFilters', 'clientId', 'filterStartDate', 'filterEndDate', 'commentCount', 'pointDateSelected']),

  mounted() {
    console.log("Showing comments...%o", this);
    let factors = this.factors.map(factor => {
      return factor.name;
    });
    axios
        .get('http://localhost:8888/client/comments/crna', {
          params: {
            factors: String(factors),
            period: this.period,
            clientId: this.clientId,
            startDate: this.startDate,
            endDate: this.endDate,
            count: this.commentCount,
            advancedFilter: this.advancedFilter
          },
        })
        .then((response) => {
          console.log("Got response %o", response);
          if (this.pointDateSelected) {

            response.data[0].date = this.pointDateSelected;
          }
          this.comments = response.data;

        });

  },
  data() {
    return {
      comments: []
    }
  },
  methods: {
    getRowClass(comment) {
      // eslint-disable-next-line no-debugger
      debugger;
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