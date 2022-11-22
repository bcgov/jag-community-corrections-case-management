<template>
  <div data-app class="dashboard-po p-4">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1>{{getUserName}}'s clients</h1>
      </div>
      <div class="col-sm-4"></div>
      
    </div>
    <v-card class="p-3">
      <div class="row pl-4">
        <div class="col-sm-6">
          <table class="designation-totals">
            <thead>
              <tr>
                <th class="pl-4" colspan="3"> Designation Totals </th>
              </tr>
              <tr>
                <th class="pl-4" scope="col">GEN</th>
                <th scope="col">IPV</th>
                <th scope="col">SMO</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ numOfGen }}</td>
                <td>{{ numOfIPV }}</td>
                <td>{{ numOfSMO }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="col-sm-4"></div>
        <div class="col-sm-2">
          <div class="dashboard-table-header-ul float-right pr-3 mr-3">
            <ul>
              <li>Due today or overdue</li>
              <li>Due within 1 to 14 days</li>
              <li>Due in over 14 days</li>
            </ul>
          </div>
        </div>
      </div>
      <div class="dashboard-v-card">
        <v-data-table
            :key="key_results"
            :headers="headers"
            :items="clientList"
            item-key="clientNum"
            :single-expand="singleExpand"
            :expanded.sync="expanded"
            @item-expanded="expandRow"
            no-results-text="No results found"
            show-expand
            class="elevation-1"
            hide-default-footer
            :page.sync="page"
            :items-per-page="itemsPerPage"
            @page-count="pageCount = $event"
        >
          <!--Customize the Name field, making it clickable-->
          <template v-slot:item.clientName="{ item }">
            <div class="w-100 h-100 d-flex align-items-center">
              <a :href="`${baseURL}clientrecord/${item.clientNum}/tab-cp`">{{item.clientName}}</a>
            </div>
          </template>
          <!--Customize the alerts field, show the alert count -->
          <template v-slot:item.communityAlerts="{ item }">
            <div class="w-100 h-100 d-flex align-items-center justify-content-center">{{getAlerts[item.clientNum]}}</div>
          </template>
          <!--Customize the warrant field, show the warrant count -->
          <template v-slot:item.outstandingWarrants="{ item }">
            <div class="w-100 h-100 d-flex align-items-center justify-content-center">{{getWarrants[item.clientNum]}}</div>
          </template>
          <template v-slot:item.designations="{ item }">
            <div class="w-100 h-100 d-flex align-items-center justify-content-center">{{getDesignations[item.clientNum]}}</div>
          </template>
          <template v-slot:item.inCustody="{ item }">
            <div class="w-100 h-100 d-flex align-items-center justify-content-center">{{item.inCustody}}</div>
          </template>
          <template v-slot:item.orderExpiryDate="{ item }">
            <div class="w-100 h-100 d-flex align-items-center justify-content-center">{{item.orderExpiryDate}}</div>
          </template>
          <template v-slot:item.supervisionRating="{ item }">
            <div class="w-100 h-100 d-flex align-items-center justify-content-center">{{item.supervisionRating}}</div>
          </template>
          <template v-slot:item.rnaCompletedDate="{ item }">
            <div class="w-100 h-100 d-flex align-items-center justify-content-center">{{item.rnaCompletedDate}}</div>
          </template>
          <!--Customize the formStatus field -->
          <template v-slot:item.dueNext="{ item }">
            <div :class="`w-100 h-100 d-flex align-items-center justify-content-center p-2 ${getColor(item.dueDate)}`">
              <div :class="`w-100 h-100 text-center ${getColor(item.dueDate)}`">{{item.dueNext}}</div>
            </div>

          </template>
          <!--Customize the supervision rating field -->
          <template v-slot:item.dueDate="{ item }">
            <div :class="`w-100 h-100 d-flex align-items-center justify-content-center p-2 ${getColor(item.dueDate)}`">
              <div :class="`w-100 h-100 text-center ${getColor(item.dueDate)}`">{{item.dueDate}}</div>
            </div>
          </template>
          <!--Customize the expanded item to show photo and more-->
          <template v-slot:expanded-item="{ headers, item }">
            <td :colspan="1"></td>
            <td :colspan="11">
              <Form :key="keyExpandRow" :form="formJSON" :submission="initDataArray[item.clientNum]"/>
            </td>
          </template>
        </v-data-table>
      </div>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center pl-3 pr-3">
        <v-row>
          <v-col cols="1" sm="1" class="pr-4">
            <v-select
              solo
              :items="items"
              v-model="itemsPerPage"
              dense
              item-color="primary"
              @input="itemsPerPage = parseInt($event, 10)"
            ></v-select>
          </v-col>
          <v-col cols="11" sm="11">
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
import {dashboardPOSearch, clientProfileSearch} from "@/components/form.api";
import { Form } from 'vue-formio';
import templateClientProfile from '@/components/common/templateClientProfilePO.json';
import { useStore } from "@/stores/store";
import { mapStores } from 'pinia';

export default {
  name: 'RNAList',
  data() {
    return {
      //const designations
      CONST_DESIGNATION_GEN: "GEN",
      CONST_DESIGNATION_SMO: "SMO",
      CONST_DESIGNATION_IPV: "IPV",
      key_results: 0,
      // datatable variables
      items: this.$CONST_DATATABLE_PAGE_FILTERLSIT,
      page: 1,
      pageCount: 1,
      itemsPerPage: this.$CONST_DATATABLE_ITEMS_PER_PAGE,
      totalClients: 0,
      loading: true,
      headers: [
        { text: 'Client Name', value: 'clientName' },
        { text: 'Alert(s) (Y/N)', value: 'communityAlerts'},
        { text: 'Outstanding Warrant (Y/N)', value: 'outstandingWarrants' },
        { text: 'Designation', value: 'designations' },
        { text: 'In Custody (Y/N)', value: 'inCustody' },
        { text: 'Order Expiry Date', value: 'orderExpiryDate' },
        { text: 'Supervision Rating', value: 'supervisionRating' },
        { text: 'RNA Completed', value: 'rnaCompletedDate' },
        { text: 'Due Next', value: 'dueNext', cellClass: 'p-0 m-0' },
        { text: 'Due Date', value: 'dueDate', cellClass: 'p-0 m-0' },
      ],
      expanded: [],
      singleExpand: false,
      clientList: [],
      initDataArray: {},
      keyExpandRow: 0,
      // calculated val
      numOfGen: 0,
      numOfSMO: 0,
      numOfIPV: 0,
      //data for the expand row
      initData: {},
      formJSON: templateClientProfile,
      baseURL: import.meta.env.BASE_URL,
      POIdirId: null,
      POName: null,
      POLocationId: null
    }
  },
  mounted(){
    this.initPage();
  },
  // watch: {
  //   useStore: {
  //     immediate: true,
  //     deep: true,
  //     handler(newValue, oldValue) {
  //       console.log("Location updated from Header: ", newValue, oldValue);
  //     }
  //   }
  // },
  methods: {
    async initPage() {
      let enCoded = this.$route.params.param;
      // User gets here when navigate from supervisor dashboard
      if (enCoded) {
        try {
          // base64 decode the string
          let POObjString = atob(enCoded);
          let POObj = JSON.parse(POObjString);
          this.POIdirId = POObj.userId;
          this.POName = POObj.userName;
          this.POLocationId = POObj.locationId;
          
          //form search from the backend
          this.dashboardPOSearch(POObj.userId, POObj.locationId)
        } catch (err) {
          console.error("PO dashboard parsing param failed: ", err);
        }
      } else{
        // The login user is a PO
        //form search from the backend
        const [error1, defaultLocation] = await this.mainStore.getUserDefaultLocation();
        if (error1) {
          console.error(error1);
        } else {
          this.dashboardPOSearch(Vue.$keycloak.tokenParsed.preferred_username, this.mainStore.locationCD)
        }
      }
    },
    expandRow ({ item, value }) {
      // call searchPhotoAPI only when the photo hasn't loaded.
      if (this.initDataArray != null && this.initDataArray[item.clientNum] != null 
        && this.initDataArray[item.clientNum].data.detailsFetched) {
        return;
      }
      this.getClientProfile(item.clientNum);
    },
    async getClientProfile(clientNum) {
      //console.log("Client profile search for clientNum: ", clientNum);
      const [error, el] = await clientProfileSearch(clientNum);
      if (error) {
        console.error(error);
      } else {
        //Cache the photoData into this.initDataArray object
        if (this.initDataArray != null && this.initDataArray[clientNum] != null && this.initDataArray[clientNum].data != null) {
          if (el.photo != null) {
            this.initDataArray[clientNum].data.photo = "data:image/png;base64, " + el.photo.image;
            this.initDataArray[clientNum].data.datePhotoTaken = el.photo.photoTakenDate;
          }

          // Build community alerts, outstanding warrants and programs
          //build communityAlerts
          let caVal = "";
          if (el.communityAlerts != null && el.communityAlerts.length > 0) {
            for (let ca of el.communityAlerts) {
              caVal += "<li>" + ca.date + ": " + ca.details + "</li>\r\n"
            }
          } else {
            caVal = "<li>The client doesn't have any community alerts</li>"
          }
          this.initDataArray[clientNum].data.communityAlerts = caVal;
          
          //build outstandingWarrants
          let waVal = "N";
          if (el.outstandingWarrants != null && el.outstandingWarrants.length > 0) {
            waVal = "Y";
          };
          this.initDataArray[clientNum].data.outstandingWarrants = el.outstandingWarrants;
          this.initDataArray[clientNum].data.outstandingWarrants_yn = waVal;

          //build programs
          let pVal = "N";
          if (el.programs != null && el.programs.length > 0) {
            pVal = "Y";
          };
          this.initDataArray[clientNum].data.programs = el.programs;
          this.initDataArray[clientNum].data.programs_yn = pVal;
          
          // Set detailsFetched flag to true
          this.initDataArray[clientNum].data.detailsFetched = true;
        }
        this.keyExpandRow++;
      }
    },
    async dashboardPOSearch(POId, POLocationId) {
      const [error, response] = await dashboardPOSearch(POId, POLocationId);
      if (error) {
        console.error(error);
      } else {
        this.clientList = response;
        //console.log("PO search result: ", response);
        //Update the counts
        for (let el of this.clientList) {
          if (el.designations != null) {
            const designationArray = el.designations.split(" ");
            for (let d of designationArray) {
              d = d.trim();
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
        }

        // populate this.initDataArray
        this.getInitData();
      }
      this.key_results++;
      this.loading = false;
    },
    getColor(dueDateStr) {
      const dueDate = new Date(dueDateStr);
      const dateNow = new Date();
      const diffTime = dueDate - dateNow;
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      //console.log("diffDays: ", dueDate, dateNow, diffDays);
      if (diffDays <= 0) {
        return 'dashboard-background-color-red';
      }
      if (diffDays >= 1 && diffDays <= 14) {
        return 'dashboard-background-color-yellow';
      }
      if (diffDays > 14) {
        return 'dashboard-background-color-green';
      }
    },
    getInitData() {
      for (let el of this.clientList) {
        let initData = {"data": {}};
        
        let dataContent = {};
        //Preset the flag to false;
        dataContent.detailsFetched = false;
        dataContent.fullName = el.clientName;
        dataContent.clientNum = el.clientNum;
        dataContent.birthDate = el.birthDate;
        dataContent.nextAppointmentDate = el.nextAppointmentDate;
        dataContent.orderEffectiveDate = el.orderEffectiveDate;
        dataContent.rnaStatus = el.rnaStatus;
        dataContent.nextCourtDate = el.nextCourtDate;
        
        initData.data = dataContent;
        this.initDataArray[el.clientNum] = initData;
      };
    }
  },
  computed: {
    ...mapStores(useStore),
    getAlerts() {
      let alertArray = [];
      for (let el of this.clientList) {
        let alert = "N";
        if (el.alerts > 0) {
          alert = "Y (" + el.alerts + ")";
        }
        alertArray[el.clientNum] = alert;
      }
      return alertArray;
    },
    getWarrants() {
      let alertWarrant = [];
      for (let el of this.clientList) {
        let warrant = "N";
        if (el.warrants > 0) {
          warrant = "Y (" + el.warrants + ")";
        }
        alertWarrant[el.clientNum] = warrant;
      }
      return alertWarrant;
    },
    getUserName() {
      let name = Vue.$keycloak.tokenParsed.family_name + ", " + Vue.$keycloak.tokenParsed.given_name;
      //console.log("getUserName: ", this.POName);
      if (this.POName) {
        name = this.POName;
      }
      return name;
    }, 
    getDesignations() {
      let designations = [];
      for (let item of this.clientList) {
        let designation = '';
        if (item.designations != null) {
          const designationArray = item.designations.split(" ");
          designation = designationArray.reduce((acc, d) => `${acc}, ${d}`);
        }
        designations[item.clientNum] = designation;
      }
      return designations;
    }
  }
}
</script>
