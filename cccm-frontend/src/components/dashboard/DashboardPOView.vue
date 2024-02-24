<template>

  <div data-app class="dashboard-po p-4">
    <div :key="key_userDisplayName" class="row mb-2">
      <div class="col-sm-6">
        <h1 class="font-weight-bold">{{ userDisplayName }}'s clients</h1>
      </div>
    </div>
    <v-card class="p-2">
      <div class="col-sm-5 m-3">
        <strong>Probation Officers Search</strong>
          <v-select
            :key="key_po"
            item-text="poName"
            item-value="idirId"
            v-model="selectedPO"
            :items="poList"
            label=""
            v-on:change="applyPOFilter"
            outlined
          >
        </v-select>
      </div>
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
        <div class="col-sm-3"></div>
        <div class="col-sm-3">
          <DashboardDueDateLegend/>
        </div>
      </div>
      <div class="dashboard-v-card">
        <v-data-table
            :loading="loading"   
              loading-text="Searching... Please wait"
            :key="key_results"
            :headers="headers"
            :items="clientList"
            item-key="clientNum"
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
          <!--Customize the Name field, making it clickable-->
          <template v-slot:item.clientName="{ item }">
            <td class="text-left">
              <a :href="`${baseURL}${$ROUTER_NAME_CLIENTRECORD}/${item.clientNum}/tab-cp`">{{item.clientName}}</a>
            </td>
          </template>
          <!--Customize the dueNext field -->
          <template v-slot:item.dueNext="{ item }">
            <div class="`p-2 ${getColor(item.dueDate)}`">{{item.dueNext}}</div>
          </template>
          <!--Customize the dueDate rating field -->
          <template v-slot:item.dueDate="{ item }">
            <div class="`p-2 ${getColor(item.dueDate)}`">{{item.dueDateStr}}</div>
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
      <div v-if="!loading" class="text-center px-3">
        <DatatablePagination :items-per-page.sync="itemsPerPage" :page.sync="page" :page-count="pageCount"/>
      </div>
    </v-card>
    <br/><br/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { dashboardPOSearch, clientProfileSearch, getPOList } from "@/components/form.api";
import templateClientProfile from '@/components/common/templateClientProfilePO.json';
import { useStore } from "@/stores/store";
import { mapStores } from 'pinia';
import DashboardDueDateLegend from "@/components/dashboard/util/DashboardDueDateLegend.vue";
import DatatablePagination from "@/components/common/DatatablePagination.vue";

export default {
  name: 'RNAList',
  components: { DatatablePagination, DashboardDueDateLegend },
  data() {
    return {
      //const designations
      CONST_DESIGNATION_GEN: "GEN",
      CONST_DESIGNATION_SMO: "SMO",
      CONST_DESIGNATION_IPV: "IPV",
      key_results: 0,
      // datatable variables
      page: 1,
      pageCount: 1,
      itemsPerPage: this.$CONST_DATATABLE_ITEMS_PER_PAGE,
      totalClients: 0,
      loading: true,
      headers: [
        { text: 'Client Name', value: 'clientName' },
        { text: 'Alert(s) (Y/N)', value: 'alertDisplay'},
        { text: 'Designation', value: 'designationDisplay' },
        { text: 'In Custody (Y/N)', value: 'inCustody' },
        { text: 'Order Expiry Date', value: 'orderExpiryDate' },
        { text: 'Supervision Rating', value: 'supervisionRating' },
        { text: 'RNA Completed', value: 'rnaCompletedDate' },
        { text: 'Due Next', value: 'dueNext', cellClass: 'p-0 m-0' },
        { text: 'Due Date', value: 'dueDate', cellClass: 'p-0 m-0' },
      ],
      expanded: [],
      singleExpand: false,
      keyExpandRow: 0,
      // calculated val
      numOfGen: 0,
      numOfSMO: 0,
      numOfIPV: 0,
      formJSON: templateClientProfile,
      baseURL: import.meta.env.BASE_URL,
      POIdirId: null,
      POName: null,
      POLocationId: null, 
      key_po: 0,
      selectedPO: {},
      userDisplayName: '',
      key_userDisplayName: 0
    }
  },
  created() {
    this.clientList = [];
    this.initDataArray = [];
    this.poList = [];
    //data for the expand row
    this.initData = {};
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
      this.initPOList();
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
          
          // populate the poList
          this.selectedPO.idirId = this.POIdirId;
          this.selectedPO.poName = this.POName;
          
          //console.log("poIdirId, poName: ", this.POIdirId, this.POName, this.selectedPO);
          this.key_po++;
          
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
          this.selectedPO.idirId = Vue.$keycloak.tokenParsed.preferred_username;
          this.selectedPO.poName = this.mainStore.loginUserName;
          this.key_po++;

          // populate the clients data table
          this.dashboardPOSearch(Vue.$keycloak.tokenParsed.preferred_username, defaultLocation)
        }
      }

      // set the display name
      this.userDisplayName = this.selectedPO.poName;
      this.key_userDisplayName++;
    },
    expandRow ({ item, value }) {
      // call searchPhotoAPI only when the photo hasn't loaded.
      if (this.initDataArray != null && this.initDataArray[item.clientNum] != null 
        && this.initDataArray[item.clientNum].data.detailsFetched) {
        return;
      }
      this.getClientProfile(item.clientNum);
    },
    async initPOList() {
      const [error1, defaultLocation] = await this.mainStore.getUserDefaultLocation();
      if (error1) {
        console.error(error1);
      } else {
        // populate the poList
        //console.log("search from podashboard: ", defaultLocation);
        const [error2, defaultPOList] = await this.mainStore.getPOList(defaultLocation);
        if (error2) {
          console.error(error2);
        } else {
          this.poList = defaultPOList;
          //console.log("poList: ", this.poList);
          this.key_po++;
        }
      }
    },
    applyPOFilter(idirId) {
      // Clear the previous search results
      this.clientList = [];
      this.loading = true;
      this.numOfGen = 0;
      this.numOfIPV = 0;
      this.numOfSMO = 0;
      this.key_results++;

      // search based on the newly selected PO
      this.dashboardPOSearch(idirId, this.mainStore.locationCD);
      
      //update the title
      let thePO = this.poList.find(item => item.idirId == idirId);
      this.userDisplayName = thePO == null ? '' : thePO.poName;
      this.key_userDisplayName++;
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

          // map birthDate
          this.initDataArray[clientNum].data.birthDate = el.birthDate;
          this.initDataArray[clientNum].data.orderEffectiveDate = el.orderInformation == null ? '' : el.orderInformation.effectiveDate;
          this.initDataArray[clientNum].data.nextCourtDate = el.courtInformation == null ? '' : el.courtInformation.dueDate;
          this.initDataArray[clientNum].data.rnaStatus = el.rnaStatus;

          // Build community alerts, outstanding warrants and programs
          //build communityAlerts
          let caVal = "";
          if (el.communityAlerts != null && el.communityAlerts.length > 0) {
            for (let ca of el.communityAlerts) {
              var alertDate = ca.date == null ? '' : ca.date;
              caVal += "<li>" + alertDate + ": " + ca.comment + "</li>\r\n"
            }
          } else {
            caVal = "<li>The client doesn't have any community alerts</li>"
          }
          this.initDataArray[clientNum].data.communityAlerts = caVal;
          
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
          el.dueDateStr = el.dueDate;
          el.dueDate = el.dueDate != null ? new Date(el.dueDate) : null;
          if (el.designations != null) {
            if (el.designations.indexOf(this.CONST_DESIGNATION_SMO) >= 0) {
              this.numOfSMO++;
            } else if (el.designations.indexOf(this.CONST_DESIGNATION_IPV) >= 0) {
              this.numOfIPV++;
            } else if (el.designations.indexOf(this.CONST_DESIGNATION_GEN) >= 0) {
              this.numOfGen++;
            }
          }

          // Update the alert display
          let alert = "N";
          if (el.alerts != null && el.alerts > 0) {
            alert = "Y (" + el.alerts + ")";
          }
          el.alertDisplay = alert;

          // Update the designation display
          let designation = '';
          if (el.designations != null) {
            const designationArray = el.designations.split(", ");
            designation = designationArray.reduce(
              (accumulator, currentValue) => `${accumulator} ${currentValue.trim() == this.CONST_DESIGNATION_GEN ? '' : currentValue.trim()}`,
              designationArray != null && designationArray.length == 1 && designationArray[0].trim() == this.CONST_DESIGNATION_GEN ? this.CONST_DESIGNATION_GEN : ''
            );
          }
          el.designationDisplay = designation;
        }

        // populate this.initDataArray
        this.getInitData();
      }
      this.key_results++;
      this.loading = false;
    },
    getColor(dueDate) {
      if (dueDate == null) {
        return '';
      }
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
      return '';
    },
    getInitData() {
      for (let el of this.clientList) {
        let initData = {"data": {}};
        
        let dataContent = {};
        //Preset the flag to false;
        dataContent.detailsFetched = false;
        dataContent.fullName = el.clientName;
        dataContent.csNumber = el.clientNum;
        
        initData.data = dataContent;
        this.initDataArray[el.clientNum] = initData;
      };
    }
  },
  computed: {
    ...mapStores(useStore),
  }
}
</script>
