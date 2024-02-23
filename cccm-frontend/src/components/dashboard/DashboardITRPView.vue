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
            <v-radio-group v-model="centreFilter" row v-on:change="applyCentreFilter">
              <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="ReVOII Alerts" value="revoii"></v-radio>
              <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="ITRP Alerts" value="itrp"></v-radio>
              <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="All Individuals" value="all"></v-radio>
            </v-radio-group>
          </section>
        </div>
        <div class="col-sm-3">
          <DashboardDueDateLegend/>
        </div>
      </div>
    </v-card>
    <br/><br/>
  </div>

</template>

<script lang="ts">
import { useStore } from "@/stores/store";
import { mapStores } from "pinia";
import DashboardDueDateLegend from "@/components/dashboard/util/DashboardDueDateLegend.vue";

export default {
  name: "DashboardItrpView",
  components: { DashboardDueDateLegend },
  data() {
    return {
      // datatable variables
      centreFilter: "revoii",
      // calculated values
      centreDisplayName: "",
      key_centreDisplayName: 0,
    }
  },
  created() {
  },
  mounted() {
    this.centreDisplayName = this.mainStore.locationDescription;
  },
  methods: {
    applyCentreFilter() {
      console.log("Filter", this.centreFilter);
    },
  },
  computed: {
    ...mapStores(useStore),
  }
}
</script>
