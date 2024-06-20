<template>
  <div data-app class="dashboard-po p-4">
    <div :key="key_centreDisplayName" class="row mb-2">
      <div class="col-sm-6">
        <h1 class="font-weight-bold">{{ centreDisplayName }}</h1>
      </div>
    </div>
    <v-card class="p-3">
      <div class="row pl-4">
        <div class="col-sm-9 d-flex">
          <section class="d-flex flex-column justify-content-end">
            <strong>Centre Filter</strong>
            <v-radio-group row v-model="centreFilter">
              <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="ReVOII Alerts" value="revoii"></v-radio>
              <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="ITRP Alerts" value="itrp"></v-radio>
              <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="All Individuals" value="all"></v-radio>
            </v-radio-group>
          </section>
        </div>
        <div class="col-sm-3">
          <DashboardDueDateLegend ref="dashboardDueDate"/>
        </div>
      </div>
      <div class="dashboard-v-card">
        <v-data-table
            :loading="loading"
            loading-text="Searching... Please wait"
            :key="key_results"
            :headers="headers"
            :items="clientList"
            :custom-filter="applyCentreFilter"
            :search="centreFilter"
            item-key="clientNum"
            no-results-text="No results found"
            class="elevation-1 text-center"
            hide-default-footer
            :page.sync="page"
            :items-per-page="itemsPerPage"
            @page-count="pageCount = $event"
        >
          <!--Customize the Name field, making it clickable-->
          <template v-slot:item.clientName="{ item }">
            <td class="text-left">
              <div class="px-3">
                <a :href="`${baseURL}${$ROUTER_NAME_CLIENTRECORD}/${item.clientNum}/tab-cp`">{{item.clientName}}</a>
              </div>
            </td>
          </template>
          <!--Customize the CMRPDueDate field -->
          <template v-slot:item.CMRPDueDate="{ item }">
            <div :class="$refs.dashboardDueDate?.getColor(item.CMRPDueDate)">{{item.CMRPDueDate}}</div>
          </template>
        </v-data-table>
      </div>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center px-3">
        <DatatablePagination :items-per-page.sync="itemsPerPage" :page.sync="page" :page-count="pageCount"/>
      </div>
    </v-card>
    <br/><br/>
  </div>
</template>

<script lang="ts">
import { useStore } from "@/stores/store";
import { mapStores } from "pinia";
import DashboardDueDateLegend from "@/components/dashboard/util/DashboardDueDateLegend.vue";
import DatatablePagination from "@/components/common/DatatablePagination.vue";
import { dashboardCentreSearch } from "@/components/form.api";

export default {
  name: "DashboardItrpView",
  components: { DashboardDueDateLegend, DatatablePagination },
  data() {
    return {
      // const designations
      key_results: 0,
      // datatable variables
      page: 1,
      pageCount: 1,
      itemsPerPage: this.$CONST_DATATABLE_ITEMS_PER_PAGE,
      totalClients: 0,
      loading: true,
      headers: [
        { text: 'Client Name', value: 'clientName' },
        { text: 'CS#', value: 'clientNum'},
        { text: 'Prob. Discharge/RTC Date', value: 'dischargeRtcDate' },
        { text: 'Next Court Date', value: 'nextCourtDate' },
        { text: 'Active Com. Order', value: 'orderStatus' },
        { text: 'Sup. Level', value: 'supervisionLevel' },
        { text: 'CRNA Comp. Dt.', value: 'CRNACompDate' },
        { text: 'C-CMRP Comp. Dt.', value: 'CMRPCompDate' },
        { text: 'C-CMRP Due Dt.', value: 'CMRPDueDate', cellClass: 'p-0 m-0' },
      ],
      centreFilter: "revoii",
      // calculated values
      baseURL: import.meta.env.BASE_URL,
      centreDisplayName: "",
      key_centreDisplayName: 0,
    }
  },
  created() {
    this.clientList = [];
  },
  mounted() {
    this.centreDisplayName = this.mainStore.locationDescription;
    this.initPage();
  },
  methods: {
    async initPage() {
      // form search from the backend
      const [error1, defaultLocation] = await this.mainStore.getUserDefaultLocation();
      if (error1) {
        console.error(error1);
      } else {
        if (this.mainStore.locationTypeCD != 'INST') {
          // redirect to client search page if location is not a custody centre
          await this.$router.replace(`${this.$ROUTER_NAME_CLIENTSEARCH}`)
        }
        else {
          // populate the clients data table
          await this.dashboardSearch(defaultLocation)
        }
      }
    },
    async dashboardSearch(POLocationId: number) {
      const [error, response] = await dashboardCentreSearch(POLocationId);
      if (error) {
        console.error(error);
      } else {
        this.clientList = response;
      }
      this.key_results++;
      this.loading = false;
    },
    applyCentreFilter(value: string, query: string, item: any) {
      // filter table based on alerts count
      if (query == "revoii") {
        return item.rvoCount > 0;
      } else if (query == "itrp") {
        return item.itrpCount > 0;
      } else {
        return true;
      }
    },
  },
  computed: {
    ...mapStores(useStore),
  }
}
</script>
