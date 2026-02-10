<template>
  <div data-app class="row p-4">
    <div class="justify-content-center mb-2 col-10">
      <v-data-table item-key="comment.id" class="elevation-1" :headers="headers" :items="comments" :items-per-page="10">
      </v-data-table>
    </div>
  </div>
</template>

<script>
import { mapState, mapStores } from "pinia";
import { trendStore } from '../../stores/trendstore';

import axios from "axios";

export default {
  name: "CommentsTable",
  computed: {
    ...mapState(trendStore, ['factors', 'startDate', 'endDate', 'clientNumber', 'chartType'])
  },
  mounted() {
    console.log("Showing comments for %o", this.store.factors);
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
      comments: [],
      headers: [
        { text: 'Date', value: 'createdDate' },
        { text: 'Factor', value: 'factor' },
        { text: 'Rating', value: 'answerValue' },
        { text: 'Comment', value: 'value' }
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
        this.comments.push(...dataset.comments);
      });

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