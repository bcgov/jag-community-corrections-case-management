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
          <tr  v-for="comment in comments" v-bind:key="comment.id">
            <td class="date">{{ comment.createdDate }}</td>
            <td>{{ comment.factor }}</td>
            <td>{{ comment.question }}</td>
            <td style="text-align: justify">{{ comment.value }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { mapState, mapStores } from "pinia/dist/pinia";
import { trendStore } from '../../stores/trendstore';
import { getClientFormComments } from "@/components/form.api";

import axios from "axios";

export default {
  name: "CommentsTable",
  computed: {
    ...mapState(trendStore, ['factors', 'startDate', 'endDate']),
  },
  mounted() {
    console.log("Showing comments...%o", this);
    this.getComments();
  },
  data() {
    return {
      comments: []
    }
  },
  methods: {
    async getComments() {      
      let csNumber = this.$route.params.csNumber;
      let filter = {
        factors: this.factors,
            csNumber: csNumber,
            startDate: this.startDate,
            endDate: this.endDate
      };
      const [error,data] = await getClientFormComments(csNumber, filter);
      if (error) {
        console.error(error);
      } else {
        console.log("Got comments %o", data);
        this.comments = data;
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