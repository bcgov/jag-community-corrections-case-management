<template>
  <div data-app>
    <v-card>
      <div class="row">
        <div class="col-sm-6">
          <h4>My Dashboard</h4>
        </div>
        <div class="col-sm-4"></div>
        <div class="col-sm-2">
          <button @click="print()">Print Report</button>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-6">
          <h5>&nbsp;&nbsp;&nbsp;&nbsp;Designation Total</h5>
          <div><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GEN: </strong> {{ numOfGen }}</div>
          <div><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SMO: </strong> {{ numOfSMO }}</div>
          <div><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;IPV: </strong> {{ numOfIPV }}</div>
        </div>
        <div class="col-sm-4"></div>
        <div class="col-sm-2">
          <div>Due today or overdue</div>
          <div>Due within 1 to 14 days</div>
          <div>Due in over 14 days</div>
        </div>
      </div>
      <v-data-table
        :key="key_results"
        :headers="headers"
        :items="clientList"
        item-key="csNumber"
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
        <!--Customize the Name field, making it clickable-->
        <template v-slot:item.clientName="{ item }">
          <a :href="`/clientprofile/${item.csNumber}`" target="_blank">{{item.clientName}}</a>
        </template>
        <!--Customize the alerts field, show the alert count -->
        <template v-slot:item.communityAlerts="{ item }">
          <span>{{getAlerts[item.csNumber]}}</span>
        </template>
        <!--Customize the warrant field, show the warrant count -->
        <template v-slot:item.outstandingWarrants="{ item }">
          <span>{{getWarrants[item.csNumber]}}</span>
        </template>
        <!--Customize the expanded item to show photo and more-->
        <template v-slot:expanded-item="{ headers, item }">
          <td :colspan="1"></td>
          <td :colspan="11">
            <Form :form="formJSON" :submission="initDataArray[item.csNumber]"/>
          </td>      
        </template>
        <!--Customize the formStatus field -->
        <template v-slot:item.dueNext="{ item }">
          <span :class="getColor(item.dueDate)">{{item.dueNext}}</span>
        </template>
        <!--Customize the supervision rating field -->
        <template v-slot:item.dueDate="{ item }">
          <span :class="getColor(item.dueDate)">{{item.dueDate}}</span>
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
    </v-card>
    <br/><br/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import {clientSearchByPO} from "@/components/form.api";
import { Form } from 'vue-formio';
import templateClientProfile from '@/components/common/templateClientProfilePO.json';


export default {
  name: 'RNAList',
  data() {
    return {
      //const designation
      CONST_DESIGNATION_GEN: "GEN",
      CONST_DESIGNATION_SMO: "SMO",
      CONST_DESIGNATION_IPV: "IPV",
      key_results: 0,
      // datatable variables
      items: ['1', '2', '5', '10', '15'],
      page: 1,
      pageCount: 1,
      itemsPerPage: 5,
      totalClients: 0,
      loading: true,
      headers: [
        { text: 'Client Name', value: 'clientName', sortable: true, class: 'datatable-header-text-style' },
        { text: 'Alert(s) (Y/N)', value: 'communityAlerts', class: 'datatable-header-text-style' },
        { text: 'Outstanding Warrant (Y/N)', value: 'outstandingWarrants', class: 'datatable-header-text-style' },
        { text: 'Designation', value: 'designation', class: 'datatable-header-text-style' },
        { text: 'In Custody (Y/N)', value: 'inCustody', class: 'datatable-header-text-style' },
        { text: 'Order Expiry Date', value: 'orderExpiryDate', class: 'datatable-header-text-style' },
        { text: 'Supervision Rating', value: 'supervisionRating', class: 'datatable-header-text-style' },
        { text: 'RNA Completed', value: 'rnaCompleteDate', class: 'datatable-header-text-style' },
        { text: 'Due Next', value: 'dueNext', class: 'datatable-header-text-style' },
        { text: 'Due Date', value: 'dueDate', class: 'datatable-header-text-style' },
      ],
      expanded: [],
      singleExpand: false,
      clientList: [],
      initDataArray: {},
      // calculated val
      numOfGen: 0,
      numOfSMO: 0,
      numOfIPV: 0,
      //data for the expand row
      initData: {},
      formJSON: templateClientProfile,
    }
  },
  mounted(){
    //form search from the backend
    this.clientSearchAPI(this.$route.params.poID)
  },
  methods: {
    print() {
      console.log("Print client list");
    },
    async clientSearchAPI(POID) {
      const [error, response] = await clientSearchByPO(POID);
      //this.initData = response.data;
      this.key_results++;
      this.loading = false;
      this.clientList =   
        [
          {
            "csNumber": "123456780",
            "clientName": "Arsenault, Sonja",
            "designation": ["GEN", "IPV", "SMO"],
            "inCustody": "Y", 
            "orderExpiryDate": "2022-10-10",
            "supervisionRating": "H",
            "rnaCompleteDate": "2022-08-10",
            "dueNext": "Conditions Due",
            "dueDate": "2022-08-15",
            "datePhotoTaken": "2021-10-10",
            "photo": "https://www.w3schools.com/images/lamp.jpg",
            "birthDate": "1979-12-03",
            "rnaStatus": "OD",
            "communityAlerts": [
              {
                "date": "2022-01-02",
                "details": "Client threatened staff"
              },
              {
                "date": "2022-03-02",
                "details": "Client brought knife to meeting"
              },
              {
                "date": "2022-04-02",
                "details": "Client attacked staff"
              }
            ],
            "outstandingWarrants": [],
            "nextAppointmentDate": "2022-12-01",
            "orderEffectiveDate": "2022-11-01",
            "programs": [
              {
                "referredDate": "2022-01-02",
                "name": "Violence Prevention",
                "status": "Enrolled",
                "startDate": "2022-01-02",
                "outcome": "Failed to complete"
              },
              {
                "referredDate": "2022-01-02",
                "name": "Substance Abuse",
                "status": "Enrolled",
                "startDate": "2022-01-02",
                "outcome": "Failed to complete"
              }
            ],
            "nextCourtDate": "2022-11-01"
          },
          {
            "csNumber": "123456781",
            "clientName": "Kovlachek, Maria",
            "designation": ["GEN"],
            "inCustody": "N", 
            "orderExpiryDate": "2022-10-04",
            "supervisionRating": "H",
            "rnaCompleteDate": "2022-08-12",
            "dueNext": "Bail Expiry Date",
            "dueDate": "2022-08-10",
            "datePhotoTaken": "2021-10-10",
            "photo": "https://www.w3schools.com/images/lamp.jpg",
            "birthDate": "1979-12-03",
            "rnaStatus": "OD",
            "communityAlerts": [],
            "outstandingWarrants": [
              {
                "date": "2022-01-02",
                "details": "Client threatened staff",
                "fileNumber": "18-0184"
              },
              {
                "date": "2022-03-02",
                "details": "Client brought knife to meeting",
                "fileNumber": "18-0184"
              },
              {
                "date": "2022-04-02",
                "details": "Client attacked staff",
                "fileNumber": "18-0184"
              }
            ],
            "nextAppointmentDate": "2022-12-01",
            "orderEffectiveDate": "2022-11-01",
            "programs": [],
            "nextCourtDate": "2022-11-01"
          },
          {
            "csNumber": "123456782",
            "clientName": "Shiau, Ann",
            "designation": ["GEN", "IPV"],
            "inCustody": "N", 
            "orderExpiryDate": "2022-12-10",
            "supervisionRating": "H",
            "rnaCompleteDate": "2022-10-10",
            "dueNext": "CRNA-CMP",
            "dueDate": "2022-08-09",
            "datePhotoTaken": "2021-10-10",
            "photo": "https://www.w3schools.com/images/lamp.jpg",
            "birthDate": "1979-12-03",
            "rnaStatus": "OD",
            "communityAlerts": [
              {
                "date": "2022-01-02",
                "details": "Client threatened staff"
              },
              {
                "date": "2022-03-02",
                "details": "Client brought knife to meeting"
              },
              {
                "date": "2022-04-02",
                "details": "Client attacked staff"
              }
            ],
            "outstandingWarrants": [],
            "nextAppointmentDate": "2022-12-01",
            "orderEffectiveDate": "2022-11-01",
            "programs": [
              {
                "referredDate": "2022-01-02",
                "name": "Violence Prevention",
                "status": "Enrolled",
                "startDate": "2022-01-02",
                "outcome": "Failed to complete"
              },
              {
                "referredDate": "2022-01-02",
                "name": "Substance Abuse",
                "status": "Enrolled",
                "startDate": "2022-01-02",
                "outcome": "Failed to complete"
              }
            ],
            "nextCourtDate": "2022-11-01"
          },
          {
            "csNumber": "123456783",
            "clientName": "Tyler, Steven",
            "designation": ["GEN", "IPV", "SMO"],
            "inCustody": "Y", 
            "orderExpiryDate": "2022-11-10",
            "supervisionRating": "H",
            "rnaCompleteDate": "2022-05-10",
            "dueNext": "CRNA-CMP",
            "dueDate": "2022-10-10",
            "datePhotoTaken": "2021-10-10",
            "photo": "https://www.w3schools.com/images/lamp.jpg",
            "birthDate": "1979-12-03",
            "rnaStatus": "OD",
            "communityAlerts": [
              {
                "date": "2022-01-02",
                "details": "Client threatened staff"
              },
              {
                "date": "2022-04-02",
                "details": "Client attacked staff"
              }
            ],
            "outstandingWarrants": [
              {
                "date": "2022-01-02",
                "details": "Client threatened staff",
                "fileNumber": "18-0184"
              }
            ],
            "nextAppointmentDate": "2022-12-01",
            "orderEffectiveDate": "2022-11-01",
            "programs": [
              {
                "referredDate": "2022-01-02",
                "name": "Violence Prevention",
                "status": "Enrolled",
                "startDate": "2022-01-02",
                "outcome": "Failed to complete"
              },
              {
                "referredDate": "2022-01-02",
                "name": "Substance Abuse",
                "status": "Enrolled",
                "startDate": "2022-01-02",
                "outcome": "Failed to complete"
              }
            ],
            "nextCourtDate": "2022-11-01"
          }
        ];
      //update the counts
      for (let el of this.clientList) {
        for (let d of el.designation) {
          if (d == this.CONST_DESIGNATION_GEN) {
            this.numOfGen++;
          }
          if (d == this.CONST_DESIGNATION_SMO) {
            this.numOfSMO++;
          }
          if (d == this.CONST_DESIGNATION_IPV) {
            this.numOfIPV++;
          }
        }
      }

      // populate this.initDataArray
      this.getInitData();

      if (error) {
        console.error(error);
      }       
    },
    getColor(dueDateStr) {
      const dueDate = new Date(dueDateStr);
      const dateNow = new Date();
      const diffTime = dueDate - dateNow;
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      //console.log("diffDays: ", dueDate, dateNow, diffDays);
      if (diffDays <= 0) {
        return 'red';
      }
      if (diffDays >= 1 && diffDays <= 14) {
        return 'yellow';
      }
      if (diffDays > 14) {
        return 'green';
      }
    },
    getInitData() {
      for (let el of this.clientList) {
        let initData = {"data": {}};
        let dataContent = {};
        dataContent.photo = el.photo;
        dataContent.datePhotoTaken = el.datePhotoTaken;
        dataContent.fullName = el.clientName;
        dataContent.csNumber = el.csNumber;
        dataContent.birthDate = el.birthDate;
        //build communityAlerts
        let caVal = "";
        if (el.communityAlerts != null && el.communityAlerts.length > 0) {
          for (let ca of el.communityAlerts) {
            caVal += "<li>" + ca.date + ": " + ca.details + "</li>\r\n"
          }
        } else {
          caVal = "<li>The client doesn't have any Community Alerts</li>"
        }
        dataContent.communityAlerts = caVal;
        
        //build outstandingWarrants
        let waVal = "N";
        if (el.outstandingWarrants != null && el.outstandingWarrants.length > 0) {
          waVal = "Y";
        };
        dataContent.outstandingWarrants = el.outstandingWarrants;
        dataContent.outstandingWarrants_yn = waVal;

        //build programs
        let pVal = "N";
        if (el.programs != null && el.programs.length > 0) {
          pVal = "Y";
        };
        dataContent.programs = el.programs;
        dataContent.programs_yn = pVal;

        dataContent.nextAppointmentDate = el.nextAppointmentDate;
        dataContent.orderEffectiveDate = el.orderEffectiveDate;
        dataContent.rnaStatus = el.rnaStatus;
        dataContent.nextCourtDate = el.nextCourtDate;
        
        initData.data = dataContent;
        this.initDataArray[el.csNumber] = initData;
      };

      //console.log("initDataArray: ", this.initDataArray);
    }
  },
  computed: {
    getAlerts() {
      let alertArray = [];
      for (let el of this.clientList) {
        let alert = "N";
        if (el.communityAlerts != null && el.communityAlerts.length > 0) {
          alert = "Y (" + el.communityAlerts.length + ")";
        }
        alertArray[el.csNumber] = alert;
      }
      return alertArray;
    },
    getWarrants() {
      let alertWarrant = [];
      for (let el of this.clientList) {
        let warrant = "N";
        if (el.outstandingWarrants != null && el.outstandingWarrants.length > 0) {
          warrant = "Y (" + el.outstandingWarrants.length + ")";
        }
        alertWarrant[el.csNumber] = warrant;
      }
      return alertWarrant;
    }
  }
}
</script>
