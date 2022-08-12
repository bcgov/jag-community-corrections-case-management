<template>
  <div data-app>
    <v-card>
      <div class="row">
        <div class="col-sm-6">
          <h4>My Dashboard</h4>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-2">
          <v-select
            item-text="text"
            item-value="value"
            v-model="selectedLocation"
            :items="locationTypes"
            label="Location"
            v-on:change="applyLocationFilter"
            outlined
          >
          </v-select>
        </div>
      </div>
      <v-data-table
        :key="key_results"
        :headers="headers"
        :items="officerList"
        item-key="poNumber"
        :single-expand="singleExpand"
        :expanded.sync="expanded"
        no-results-text="No results found"
        hide-default-header
        show-expand
        class="elevation-1"
        hide-default-footer
        :page.sync="page"
        :items-per-page="itemsPerPage"
        @page-count="pageCount = $event"
        >
        <template slot="body.append">
          <tr class="pink--text">
            <th></th>
            <th class="title">Total</th>
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
        <!--Customize the header style-->
        <template v-slot:header="{ props: { headers } }">
          <thead>
            <tr class="datatable-header-bg-style">
              <th v-for="h in headers" :class="h.class" :key="h.id">
                <span>{{h.text}}</span>
              </th>
            </tr>
          </thead>
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
          <span class="red">{{item.numHigh}}</span>
        </template>
        <!--Customize the numMedium field -->
        <template v-slot:item.numMedium="{ item }">
          <span class="yellow">{{item.numMedium}}</span>
        </template>
        <!--Customize the numLow field -->
        <template v-slot:item.numLow="{ item }">
          <span class="green">{{item.numLow}}</span>
        </template>
      </v-data-table>
      <br/>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center pt-2">
        <v-row>
          <v-col cols="2" sm="2">
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
import { Component, Vue } from 'vue-property-decorator';
import {officerSearch} from "@/components/form.api";

export default {
  name: 'OfficerList',
  props: {
    supervisorID: {
      type: String,
      default: '1',
    }
  },
  data() {
    return {
      key_results: 0,
      selectedLocation: 'Victoria',
      locationTypes: ['Victoria', 'Vancouver', 'Nanaimo'],
      // datatable variables
      items: ['1', '2', '5', '10', '15'],
      page: 1,
      pageCount: 1,
      itemsPerPage: 5,
      totalClients: 0,
      loading: true,
      headers: [
        { text: 'PO Name', value: 'poName', sortable: true, class: 'datatable-header-text-style' },
        { text: 'Active/Admin', value: 'numActive', class: 'datatable-header-text-style' },
        { text: 'Admin Closed', value: 'numAdminClosed', class: 'datatable-header-text-style' },
        { text: 'BAL', value: 'numBAL', class: 'datatable-header-text-style' },
        { text: 'High', value: 'numHigh', class: 'datatable-header-text-style' },
        { text: 'Medium', value: 'numMedium', class: 'datatable-header-text-style' },
        { text: 'Low', value: 'numLow', class: 'datatable-header-text-style' },
        { text: 'Unknown', value: 'numUnknown', class: 'datatable-header-text-style' },
        { text: 'Overdue RNA\'s', value: 'numOverdue', class: 'datatable-header-text-style' },
        { text: 'Active Reports', value: 'numActiveReports', class: 'datatable-header-text-style' },
      ],
      expanded: [],
      singleExpand: false,
      filteredOfficerList: [],
      officerList: []
    }
  },
  mounted(){
    //form search from the backend
    this.officerSearchAPI(this.supervisorID)
  },
  methods: {
    sumField(key) {
      // sum data in give key (property)
      return this.officerList.reduce((total, obj) => total + obj[key], 0);
    },
    applyLocationFilter(locationType) {
      console.log("selected locationType: ", locationType);
      this.filteredOfficerList = this.officerList.filter(el => {
        return el.location.includes(locationType);
      });
      this.key_results++;
    },
    async officerSearchAPI(supervisorID) {
      const [error, response] = await officerSearch(supervisorID);
      
      this.key_results++;
      this.loading = false;
      this.officerList =   
        [
          {
            "poNumber": "1233440",
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
          },
          {
            "poNumber": "1233441",
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
          },
          {
            "poNumber": "1233442",
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
          },
          {
            "poNumber": "1233443",
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
          }
        ];
      
      if (error) {
        console.error(error);
      }       
    }
  }
}
</script>
