<template>
  <div data-app class="p-4">
    <div class="row mb-4">
      <div class="col-sm-6 mb-2">
        <h1 class="font-weight-bold">{{getUserName}}'s team</h1>
      </div>
    </div>
    <v-card>
      <div class="row justify-content-between">
        <div class="col-sm-2 m-3">
          <strong>Location</strong>
          <v-select
            :key="key_location"
            item-text="text"
            item-value="value"
            v-model="selectedLocation"
            :items="locationTypes"
            label=""
            v-on:change="applyLocationFilter"
            outlined
          >
          </v-select>
        </div>
        <div class="col-sm-2 mr-3 align-self-end pb-4">
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
        </div>
      </div>
      <div class="dashboard-v-card">
        <v-data-table
            :key="key_results"
            :headers="headers"
            :search="search"
            :items="filteredOfficerList"
            item-key="poID"
            :single-expand="singleExpand"
            :expanded.sync="expanded"
            no-results-text="No results found"
            show-expand
            class="elevation-1 text-center"
            hide-default-footer
            :page.sync="page"
            :items-per-page="itemsPerPage"
            @page-count="pageCount = $event"
        >
          <!--Added the Total row-->
          <template slot="body.append">
            <tr class="pink--text">
              <th></th>
              <th class="title text-left p-0">Total</th>
              <th class="title">{{ sumField('numActive') }}</th>
              <th class="title">{{ sumField('numAdminClosed') }}</th>
              <th class="title">{{ sumField('numBAL') }}</th>
              <th class="title">{{ sumField('numHigh') }}</th>
              <th class="title">{{ sumField('numMedium') }}</th>
              <th class="title">{{ sumField('numLow') }}</th>
              <th class="title">{{ sumField('numUnknown') }}</th>
              <th class="title">{{ sumField('numOverdue') }}</th>
              <th class="title">{{ sumField('numActiveReports') }}</th>
            </tr>
          </template>
          <!--Customize the poName field, making it clickable-->
          <template v-slot:item.poName="{ item }">
            <td class="text-left">
              <a :href="`${baseURL}dashboardpo/${item.poID}`">{{item.poName}}</a>
            </td>
          </template>
          <!--Customize the expanded item to show more-->
          <template v-slot:expanded-item="{ headers, item }">
            <td :colspan="1"></td>
            <td :colspan="1">
              <strong>PCM</strong>
              <br />
              {{ item.numPCM}}
            </td>
            <td :colspan="1">
              <strong>SCM</strong>
              <br />
              {{ item.numSCM}}
            </td>
            <td :colspan="1">
              <strong>SMO</strong>
              <br />
              {{ item.numSMO}}
            </td>
            <td :colspan="1">
              <strong>Closed/Incomplete Report</strong>
              <br />
              {{ item.numClosedReport}}
            </td>
            <td :colspan="1">
              <strong>Expiring 30 Days</strong>
              <br />
              {{ item.numExpiry30Days}}
            </td>
            <td :colspan="1">
              <strong>Not Required</strong>
              <br />
              {{ item.numNotRquired}}
            </td>
            <td :colspan="1">
              <strong>RNA's Due 7 days</strong>
              <br />
              {{ item.numDue7Days}}
            </td>
          </template>
          <!--Customize the numHigh field -->
          <template v-slot:item.numHigh="{ item }">
            <div class="
              w-100 h-100
              d-flex
              align-items-center
              justify-content-center
              dashboard-background-color-red">{{item.numHigh}}</div>
          </template>
          <!--Customize the numMedium field -->
          <template v-slot:item.numMedium="{ item }">
            <div class="
            w-100 h-100
            d-flex
            align-items-center
            justify-content-center
            dashboard-background-color-yellow">{{item.numMedium}}</div>
          </template>
          <!--Customize the numLow field -->
          <template v-slot:item.numLow="{ item }">
            <div class="
            w-100 h-100
            d-flex
            align-items-center
            justify-content-center
            dashboard-background-color-green">{{item.numLow}}</div>
          </template>
        </v-data-table>
      </div>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center p-3 pt-2">
        <v-row class="justify-content-between">
          <v-col cols="1" sm="1" class="pl-3 pr-2">
            <v-select
              solo
              :items="items"
              value=5
              dense
              item-color="primary"
              @input="itemsPerPage = parseInt($event, 10)"
            ></v-select>
          </v-col>
          <v-col cols="10" sm="10">
            <v-pagination v-model="page" :total-visible="7" :length="pageCount"></v-pagination>
          </v-col>
        </v-row>
      </div>
      <!--Add the total -->
      
    </v-card>
    <br/><br/>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { Component } from 'vue-property-decorator';
import {dashboardSupervisorSearch} from "@/components/form.api";
import {useStore} from "@/stores/store";
import {mapStores} from 'pinia';

export default {
  name: 'OfficerList',
  data() {
    return {
      key_results: 0,
      key_location: 0,
      selectedLocation: {text: "", value: ""},
      locationTypes: [{text: "Victoria Probation Office", value: "victoria"}, {text: "Vancouver", value: "vancouver"}, {text: "Nanaimo", value: "nanaimo"}],
      // datatable variables
      items: ['1', '2', '5', '10', '15'],
      page: 1,
      pageCount: 1,
      itemsPerPage: 5,
      totalClients: 0,
      loading: true,
      headers: [
        { text: 'PO Name', value: 'poName', sortable: true },
        { text: 'Active/Admin', value: 'numActive' },
        { text: 'Admin Closed', value: 'numAdminClosed' },
        { text: 'BAL', value: 'numBAL' },
        { text: 'High', value: 'numHigh', cellClass: 'p-0 m-0 text-center' },
        { text: 'Medium', value: 'numMedium', cellClass: 'p-0 m-0 text-center' },
        { text: 'Low', value: 'numLow', cellClass: 'p-0 m-0 text-center' },
        { text: 'Unknown', value: 'numUnknown' },
        { text: 'Overdue RNA\'s', value: 'numOverdue' },
        { text: 'Active Reports', value: 'numActiveReports' },
      ],
      expanded: [],
      singleExpand: false,
      filteredOfficerList: [],
      officerList: [],
      search: '',
      baseURL: import.meta.env.BASE_URL,
    }
  },
  mounted(){
    //form search from the backend
    //console.log("SDashboard this.locationCD: ", this.mainStore.locationCD, this.mainStore.locationDescription);
    this.getPOList();
  },
  methods: {
    async getPOList() {
      this.selectedLocation.value = this.mainStore.locationCD;
      this.selectedLocation.text = this.mainStore.locationDescription;
      this.key_results++;
      this.key_location++;

      this.dashboardSupervisorSearch(this.$route.params.supervisorID);
    },
    sumField(key) {
      // sum data in give key (property)
      return this.filteredOfficerList.reduce((total, obj) => total + obj[key], 0);
    },
    applyLocationFilter(locationType) {
      this.filteredOfficerList = this.officerList.filter(el => {
        return el.locations.includes(locationType.toLowerCase());
      });
      this.key_results++;
    },
    async dashboardSupervisorSearch(supervisorID) {
      const [error, response] = await dashboardSupervisorSearch(supervisorID);
      this.loading = false;
      if (error) {
        console.error(error);
      } else {
        console.log("Supervisor dashboard search: ", response);
        //this.officerList = response.data;
        this.officerList =   
          [
            {
              "poID": "1233440",
              "poName": "Arsenault, Sonja",
              "numActive": 44,
              "numAdminClosed": 8, 
              "numBAL": 890,
              "numHigh": 4,
              "numMedium": 30,
              "numLow": 10,
              "numUnknown": 0,
              "numOverdue": 3,
              "numActiveReports": 13,
              "numPCM": 203,
              "numSCM": 0,
              "numSMO": 7,
              "numClosedReport": 2,
              "numExpiry30Days": 4,
              "numNotRquired": 1,
              "numDue7Days": 3,
              "locations": ["victoria", "vancouver"]
            },
            {
              "poID": "1233441",
              "poName": "Kovlachek, Maria",
              "numActive": 56,
              "numAdminClosed": 0, 
              "numBAL": 700,
              "numHigh": 5,
              "numMedium": 30,
              "numLow": 9,
              "numUnknown": 0,
              "numOverdue": 3,
              "numActiveReports": 8,
              "numPCM": 203,
              "numSCM": 0,
              "numSMO": 7,
              "numClosedReport": 2,
              "numExpiry30Days": 4,
              "numNotRquired": 1,
              "numDue7Days": 3,
              "locations": ["victoria"]
            },
            {
              "poID": "1233442",
              "poName": "Shiau, Ann",
              "numActive": 21,
              "numAdminClosed": 2, 
              "numBAL": 210,
              "numHigh": 1,
              "numMedium": 20,
              "numLow": 4,
              "numUnknown": 5,
              "numOverdue": 1,
              "numActiveReports": 14,
              "numPCM": 203,
              "numSCM": 0,
              "numSMO": 7,
              "numClosedReport": 2,
              "numExpiry30Days": 4,
              "numNotRquired": 1,
              "numDue7Days": 3,
              "locations": ["victoria", "vancouver", "nanaimo"]
            },
            {
              "poID": "1233443",
              "poName": "Tyler, Steven",
              "numActive": 39,
              "numAdminClosed": 2, 
              "numBAL": 340,
              "numHigh": 2,
              "numMedium": 10,
              "numLow": 7,
              "numUnknown": 1,
              "numOverdue": 2,
              "numActiveReports": 30,
              "numPCM": 203,
              "numSCM": 0,
              "numSMO": 7,
              "numClosedReport": 2,
              "numExpiry30Days": 4,
              "numNotRquired": 1,
              "numDue7Days": 3,
              "locations": ["vancouver"]
            }
          ];
        // apply location filter
        this.applyLocationFilter(this.selectedLocation.value);
      }
    }
  },
  computed: {
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore),
    getUserName() {
        return Vue.$keycloak.tokenParsed.family_name + ", " + Vue.$keycloak.tokenParsed.given_name;
    }
  }
}
</script>
