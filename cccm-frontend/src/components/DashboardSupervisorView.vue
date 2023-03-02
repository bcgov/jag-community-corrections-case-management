<template>
  <div data-app class="p-4 dashboard-supervisor">
    <div class="row mb-4">
      <div class="col-sm-6 mb-2">
        <h1 class="font-weight-bold">{{getUserName}}'s team</h1>
      </div>
    </div>
    <v-card>
      <div class="row justify-content-between">
        <div class="col-sm-5 m-3">
          <strong>Search Location</strong>
          <v-select
            :key="key_location"
            item-text="value"
            item-value="key"
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
      <div :key="keyExpandRow" class="dashboard-v-card">
        <v-data-table
            :loading="loading"   
              loading-text="Searching... Please wait"
            :key="key_results"
            :headers="headers"
            :search="search"
            :items="officerList"
            item-key="officer"
            :single-expand="singleExpand"
            :expanded.sync="expanded"
            @item-expanded="expandRow"
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
            <tr class="pink--text table-footer">
              <th></th>
              <th class="title text-left p-0">Total</th>
              <th class="title">{{ sumField('activeAdmin') }}</th>
              <th class="title">{{ sumField('adminClosed') }}</th>
              <th class="title">{{ sumField('bal') }}</th>
              <th class="title">{{ sumField('sen') }}</th>
              <th class="title">{{ sumField('high') }}</th>
              <th class="title">{{ sumField('medium') }}</th>
              <th class="title">{{ sumField('low') }}</th>
              <th class="title">{{ sumField('unknown') }}</th>
              <th class="title">{{ sumField('overdue') }}</th>
              <th class="title">{{ sumField('activeReports') }}</th>
            </tr>
          </template>
          <!--Customize the officer field, making it clickable-->
          <template v-slot:item.officer="{ item }">
            <td class="text-left">
              <a href="#" @click="onSelected(item.idirId, item.officer)">{{item.officer}}</a>
            </td>
          </template>
          <!--Customize the expanded item to show more-->
          <!-- <div :key="keyExpandRow"> -->
          <template v-slot:expanded-item="{ headers, item }">
            <td :colspan="2"></td>
            <td :colspan="1">
              <strong>PCM</strong>
              <br />
              {{ item.pcm ? item.pcm : '&nbsp;'}}
            </td>
            <td :colspan="1">
              <strong>SCM</strong>
              <br />
              {{ item.scm ? item.scm : '&nbsp;'}}
            </td>
            <td :colspan="1">
              <strong>SMO</strong>
              <br />
              {{ item.smo ? item.smo : 0}}
            </td>
            <td :colspan="2">
              <strong>Closed/Incomplete Report</strong>
              <br />
              {{ item.closedIncomplete ? item.closedIncomplete : 0 }}
            </td>
            <td :colspan="2">
              <strong>Expiring 30 Days</strong>
              <br />
              {{ item.expiringThirty ? item.expiringThirty : 0 }}
            </td>
            <td :colspan="1">
              <strong>Not Required</strong>
              <br />
              {{ item.notRequired ? item.notRequired : 0 }}
            </td>
            <td :colspan="1">
              <strong>RNA's Due 7 days</strong>
              <br />
              {{ item.dueSeven ? item.dueSeven : 0}}
            </td>
          </template>
        
          <!--Customize the high field -->
          <template v-slot:item.high="{ item }">
            <div class="
              w-100 h-100
              d-flex
              align-items-center
              justify-content-center
              dashboard-background-color-red">{{item.high}}</div>
          </template>
          <!--Customize the medium field -->
          <template v-slot:item.medium="{ item }">
            <div class="
            w-100 h-100
            d-flex
            align-items-center
            justify-content-center
            dashboard-background-color-yellow">{{item.medium}}</div>
          </template>
          <!--Customize the low field -->
          <template v-slot:item.low="{ item }">
            <div class="
            w-100 h-100
            d-flex
            align-items-center
            justify-content-center
            dashboard-backgro und-color-green">{{item.low}}</div>
          </template>
                    <!--Customize the Overdue rating field -->
          <template v-slot:item.overdue="{ item }">
            <div :class="`w-100 h-100 d-flex align-items-center justify-content-center ${getOverdueColor(item.overdue)}`">{{ item.overdue }}</div>
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
              v-model="itemsPerPage"
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
import { dashboardSupervisorSearch, dashboardPODetailsSearch } from "@/components/form.api";
import { useStore } from "@/stores/store";
import { mapStores } from 'pinia';

export default {
  name: 'OfficerList',
  data() {
    return {
      key_results: 0,
      key_location: 0,
      selectedLocation: {},
      // datatable variables
      items: this.$CONST_DATATABLE_PAGE_FILTERLSIT,
      page: 1,
      pageCount: 1,
      itemsPerPage: this.$CONST_DATATABLE_ITEMS_PER_PAGE,
      totalClients: 0,
      loading: true,
      headers: [
        { text: 'PO Name', value: 'officer', sortable: true },
        { text: 'Active/Admin', value: 'activeAdmin' },
        { text: 'Admin Closed', value: 'adminClosed' },
        { text: 'BAL', value: 'bal' },
        { text: 'SEN', value: 'sen' },
        { text: 'High', value: 'high', cellClass: 'p-0 m-0 text-center' },
        { text: 'Medium', value: 'medium', cellClass: 'p-0 m-0 text-center' },
        { text: 'Low', value: 'low', cellClass: 'p-0 m-0 text-center' },
        { text: 'Unknown', value: 'unknown' },
        { text: 'Overdue RNA\'s', value: 'overdue' },
        { text: 'Active Reports', value: 'activeReports' },
      ],
      expanded: [],
      singleExpand: false,
      search: '',
      baseURL: import.meta.env.BASE_URL,
      keyExpandRow: 0,
      pickedLocationCD : 0
    }
  },
  created() {
    this.officerList = [];
    this.locationTypes = [];
  },
  mounted(){
    //get PO list
    this.getSupervisorTeamMembers();
  },
  methods: {
    onSelected(idirId, poName) {
      let param = {};
      param.userId = idirId;
      param.userName = poName;
      param.locationId = this.pickedLocationCD;
      //console.log("Set PO param: ", param);
      let base64EncodeParam = btoa(JSON.stringify(param));

      //For code running using Node.js APIs, converting between base64-encoded strings and binary data 
      //should be performed using Buffer.from(str, 'base64') andbuf.toString('base64')
      this.$router.push({
          name: this.$ROUTER_NAME_DASHBOARDPO,
          params: {
            param: base64EncodeParam
          }
        });
    },
    expandRow ({ item, value }) {
      // call searchPhotoAPI only when the photo hasn't loaded.
      if (this.officerList != null && this.officerList[item.idirId] != null 
        && this.officerList[item.idirId].poDetailFetched) {
        return;
      }
      //console.log("item: ", item);
      this.dashboardPODetailsSearchAPI(item.idirId);
    },
    async dashboardPODetailsSearchAPI(POIdirId) {
      const [error, response] = await dashboardPODetailsSearch(POIdirId);
      if (error) {
        console.error("Supervisor dashboard PO search failed: ", error);
      } else {
        //console.log("Supervisor dashboard PO search: ", POIdirId, response);
        //Cache the PO details into this.officerList object
        // Set the poDetailFetched flag to true
        if (this.officerList != null && response != null) {
          for (let el of this.officerList) {
            el.poDetailFetched = true;
            if (el.idirId == POIdirId) {
              el.pcm = response.pcm;
              el.scm = response.scm;
              el.smo = response.smo;
              el.closedIncomplete = response.closedIncomplete;
              el.expiringThirty = response.expiringThirty;
              el.dueSeven = response.dueSeven;
              break;
            }
          }
        }
        // Force refresh the expanded row
        this.keyExpandRow++;
      }
    },
    async getSupervisorTeamMembers() {
      const [error, locations] = await this.mainStore.getUserLocations();
      if (error) {
        console.log(error);
      } else {
        this.locationTypes = this.mainStore.locations;
        const [error1, defaultLocation] = await this.mainStore.getUserDefaultLocation();
        if (error1) {
          console.error(error1);
        } else {
          this.pickedLocationCD = this.mainStore.locationCD;
          this.selectedLocation.key = this.mainStore.locationCD;
          this.selectedLocation.value = this.mainStore.locationDescription;
          this.key_results++;
          this.key_location++;

          this.dashboardSupervisorSearch(this.selectedLocation.key);
        }
      }
    },
    sumField(key) {
      // sum data in give key (property)
      return this.officerList.reduce((total, obj) => total + obj[key], 0);
    },
    applyLocationFilter(locationType) {
      // Clear table and provide loading
      this.officerList = [];
      this.key_results++;
      this.loading = true;
      //console.log("locationType: ", locationType);
      this.pickedLocationCD = locationType;

      // search based on the newly selected location
      this.dashboardSupervisorSearch(locationType);

      this.key_results++;
    },
    getOverdueColor(overdue) {
      if (overdue == null) {
        return '';
      }

      if (overdue <= 2) {
        return 'dashboard-background-color-green';
      } else if (overdue <= 5) {
        return 'dashboard-background-color-yellow';
      } else {
        return 'dashboard-background-color-red';
      }
    },
    async dashboardSupervisorSearch(locationId) {
      const [error, response] = await dashboardSupervisorSearch(locationId);
      this.loading = false;
      if (error) {
        console.error(error);
      } else {
        //console.log("Supervisor dashboard search: ", response);
        this.officerList = response;

        this.loading = false;
        this.key_results++;
      }
    }
  },
  
  computed: {
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore),
    getUserName() {
        return Vue.$keycloak.tokenParsed.given_name + " " + Vue.$keycloak.tokenParsed.family_name;
    }
  }
}
</script>
