<template>
  <div data-app>
    <div class="justify-content-center mb-2 col-12">
      <v-data-table v-if="showIntervention" item-key="comment.id" class="elevation-1" :headers="headers" :items="data"
        hide-default-footer :page.sync="page" :items-per-page="itemsPerPage" @page-count="pageCount = $event">
        <template v-slot:item.interventions="{item}">
          <table class="nestedIntervention">
            <tbody>
              <tr v-for="intervention in item.interventions">
                <td style="width:20%">{{intervention.type == 'OTHR' ? intervention.typeOverride : intervention.typeDesc}}</td>
                <td style="width:80% word-wrap:break-word">{{intervention.comment}}</td>
              </tr>
            </tbody>
          </table>
        </template>
      </v-data-table>
      <v-data-table v-else item-key="comment.id" class="elevation-1" :headers="header_noIntervention" :items="data"
        hide-default-footer :page.sync="page" :items-per-page="itemsPerPage" @page-count="pageCount = $event">
      </v-data-table>
      <!--Customize the footer-->
      <div class="text-center px-3">
        <DatatablePagination :items-per-page.sync="itemsPerPage" :page.sync="page" :page-count="pageCount" />
      </div>
    </div>
  </div>
</template>
  
<script>
import { mapState, mapStores } from "pinia";
import { trendStore } from '@/stores/trendstore';

import axios from "axios";
import DatatablePagination from "@/components/common/DatatablePagination.vue";

export default {
  name: "CommentsAndInterventionsTable",
  components: { DatatablePagination },
  computed: {
    showIntervention() {
      let theForm = this.$FORM_INFO.filter( item => item.formType === this.store.formType);
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
        { title: 'Date', key: 'createdDate', width: '10%' },
        { title: 'Factor', key: 'factor', width: '15%' },
        { title: 'Rating', key: 'answerValue', width: '5%' },
        { title: 'Comment', key: 'value', width: '20%' },
        { title: 'Interventions', key: 'interventions', width: '50%' },
      ],
      header_noIntervention: [
        { title: 'Date', key: 'createdDate', width: '10%' },
        { title: 'Factor', key: 'factor', width: '20%' },
        { title: 'Rating', key: 'answerValue', width: '10%' },
        { title: 'Comment', key: 'value', width: '60%' },
      ],
      itemsPerPage: this.$CONST_DATATABLE_ITEMS_PER_PAGE,
      page: 1,
      pageCount: 1,
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
   background-color:#ddd8d8;
}

.nestedIntervention td  {
   padding-left: 10px;
   padding-right: 10px;
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