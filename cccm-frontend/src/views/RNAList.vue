<template>
  <div data-app>
    <v-card>
      <v-row>
        <v-col class="d-flex align-right" cols="6" sm="5" >
          <v-card-title>RNA List</v-card-title>
        </v-col>
        <v-col class="d-flex align-right" cols="6" sm="2" >
          <v-card-text>Name: Smith, John</v-card-text>
        </v-col>
        <v-col class="d-flex align-right" cols="6" sm="2" >
          <v-card-text>CS#: 123456780</v-card-text>
        </v-col>
        <v-col class="d-flex align-right" cols="6" sm="3" >
          <v-card-text>Date of Birth: 1989-02-01</v-card-text>
        </v-col>
      </v-row>
      <v-row>
        <v-col class="d-flex align-left" cols="6" sm="2" >
          <v-select
            item-text="text"
            item-value="value"
            v-model="selectedFormTypes"
            :items="formTypes"
            label="Filter RNA Form"
            v-on:change="applyFormTypeFilter"
            outlined
          >
          </v-select>
        </v-col>
        <v-col class="d-flex align-left" cols="6" sm="2" >
          <v-select 
            :items="supervisionPeriods" 
            item-text="text"
            item-value="value"
            label="Supervision Periods"
            v-model="selectedSupervisionPeriods" 
            v-on:change="applyPeriodFilter"
            outlined>
          </v-select>
        </v-col>
        <v-col class="d-flex align-left" cols="6" sm="6" ></v-col>
        <v-col class="d-flex align-right" cols="6" sm="2" >
          <button @click="formCreate()">New RNA</button>
        </v-col>
      </v-row>
      <v-data-table
        :key="key_rnalistSearchResult"
        :headers="headers"
        :formTypes="formTypes"
        :supervisionPeriods="supervisionPeriods"
        :items="filteredRNAList"
        item-key="formID"
        no-results-text="No results found"
        hide-default-header
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
        <!--Customize the formStatus field -->
        <template v-slot:item.formStatus="{ item }">
          <span :class="getFormStatusColor[item.formStatus]">{{item.formStatus}}</span>
        </template>
        <!--Customize the supervision rating field -->
        <template v-slot:item.supervisionRating="{ item }">
          <span :class="getRatingColor[item.supervisionRating]">{{item.supervisionRating}}</span>
        </template>
        <!--Customize the CRNA rating field -->
        <template v-slot:item.crnaRating="{ item }">
          <span :class="getRatingColor[item.crnaRating]">{{item.crnaRating}}</span>
        </template>
        <!--Customize the SARA rating field -->
        <template v-slot:item.saraRating="{ item }">
          <span :class="getRatingColor[item.saraRating]">{{item.saraRating}}</span>
        </template>
        <!--Customize the action field -->
        <template v-slot:item.action="{ item }">
          <a href="#" @click="formView(item.formID)" title="View form">
            <i class="fa fa-eye"></i>
          </a>
          &nbsp;&nbsp;
          <a href="#" @click="formClone(item.formID)" title="Copy form">
            <i class="fas fa-copy"></i>
          </a>
          &nbsp;&nbsp;
          <a href="#" @click="formPrint(item.formID)" title="Print form">
            <i class="fas fa-print"></i>
          </a>
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
import {formSearch, cloneForm, createForm} from "@/components/form.api";

export default {
  name: 'RNAList',
  props: {
    clientID: {
      type: Number,
      default: 1,
    }
  },
  data() {
    return {
      //Const
      const_formstatus_incomplete: "Incomplete",
      const_formstatus_complete: "Complete",
      const_formstatus_overdue: "Overdue",
      const_rating_low: "Low",
      const_rating_medium: "Medium",
      const_rating_high: "High",
      key_rnalistSearchResult: 0,
      // datatable variables
      items: ['1', '2', '5', '10', '15'],
      page: 1,
      pageCount: 1,
      itemsPerPage: 5,
      totalClients: 0,
      loading: true,
      headers: [
        { text: 'RNA Form', align: 'start', sortable: true, value: 'formType', class: 'datatable-header-text-style' },
        { text: 'Assessment Status', value: 'assessmentStatus', class: 'datatable-header-text-style' },
        { text: 'Status', value: 'formStatus', class: 'datatable-header-text-style' },
        { text: 'Updated Date', value: 'updatedDate', class: 'datatable-header-text-style' },
        { text: 'Created Location', value: 'createdLocation', class: 'datatable-header-text-style' },
        { text: 'Completed By', value: 'completedBy', class: 'datatable-header-text-style' },
        { text: 'Supervision Rating', value: 'supervisionRating', class: 'datatable-header-text-style' },
        { text: 'CRNA Rating', value: 'crnaRating', class: 'datatable-header-text-style' },
        { text: 'SARA Rating', value: 'saraRating', class: 'datatable-header-text-style' },
        { text: 'Actions', value: 'action', class: 'datatable-header-text-style' },
      ],
      filteredRNAList: [],
      rnaList: [],
      selectedFormTypes: {text: "ALL", value: ""},
      formTypes: [{text: "ALL", value: ""},{text: "CRNA", value: "CRNA"},{text: "SARA", value: "SARA"}],
      selectedSupervisionPeriods: {text: "Current", value: true},
      supervisionPeriods: [{text: "ALL", value: false}, {text: "Current", value: true}],
      // form creation payload
      formData: {},
      // newly created formID
      newCreatedFormId: 0,
    }
  },
  mounted(){
    //form search from the backend
    this.formSearchAPI(this.clientID, false)
  },
  methods: {
    applyFormTypeFilter(ft) {
      this.private_applyFilter(ft, this.selectedSupervisionPeriods);
    },
    applyPeriodFilter(period) {
      this.private_applyFilter(this.selectedFormTypes, period);
    },
    private_applyFilter(formType, currentPeriod) {
      if (typeof formType == 'object') {
        formType = formType.value;
      }
      if (typeof currentPeriod == 'object') {
        currentPeriod = currentPeriod.value;
      }
      //console.log("filters formType: ", formType);
      //console.log("filters currentPeriod", currentPeriod);
      this.filteredRNAList = this.rnaList.filter(el => {
        if (currentPeriod) {
          return el.formType.includes(formType) && el.formStatus != 'Complete';
        } else {
          return el.formType.includes(formType);
        }
      });
      this.key_rnalistSearchResult++;
    },
    async createFormAPI() {
      const [error, response] = await createForm(this.formData);
      if (error) {
        console.error(error);
      } 
      //newCreatedFormId = response.formID;
    },
    async formCloneAPI(formID) {
      const [error, response] = await cloneForm(formID);
      if (error) {
        console.error(error);
      } 
    },
    async formSearchAPI(clientID, tobeRemoved_addOne) {
      const [error, response] = await formSearch(clientID, 'RNA', true);
      //this.initData = response.data;
      this.key_rnalistSearchResult++;
      this.loading = false;
      this.rnaList =   
        [
          {
            "csNumber": "123456780",
            "formID": 1212121,
            "formType": "SARA CMP",
            "assessmentStatus": "Initial",
            "formStatus": "Overdue",
            "updatedDate": "2022-04-05", 
            "createdLocation": "VIC",
            "completedBy": "Smith, Jamie",
            "supervisionRating": "Low",
            "crnaRating": "Low",
            "saraRating": "Low",
          },
          {
            "csNumber": "123456780",
            "formID": 1212122,
            "formType": "SARA CMP",
            "assessmentStatus": "Reassessment",
            "formStatus": "Complete",
            "updatedDate": "2022-01-05", 
            "createdLocation": "VIC",
            "completedBy": "Smith, Jamie",
            "supervisionRating": "Low",
            "crnaRating": "Low",
            "saraRating": "Low",
          },
          {
            "csNumber": "123456780",
            "formID": 1212124,
            "formType": "CRNA CMP",
            "assessmentStatus": "Initial",
            "formStatus": "Incomplete",
            "updatedDate": "2022-04-05", 
            "createdLocation": "VIC",
            "completedBy": "Smith, Jamie",
            "supervisionRating": "Medium",
            "crnaRating": "Low",
            "saraRating": "Medium",
          },
          {
            "csNumber": "123456780",
            "formID": 1212128,
            "formType": "CRNA CMP",
            "assessmentStatus": "Initial",
            "formStatus": "Complete",
            "updatedDate": "2022-01-05", 
            "createdLocation": "VIC",
            "completedBy": "Smith, Jamie",
            "supervisionRating": "High",
            "crnaRating": "High",
            "saraRating": "High",
          }
        ];
      if (tobeRemoved_addOne) {
        this.rnaList.unshift({
          "csNumber": "123456780",
          "formID": 1212130,
          "formType": "CRNA CMP",
          "assessmentStatus": "Initial",
          "formStatus": "Incomplete",
          "updatedDate": "2022-08-05", 
          "createdLocation": "VIC",
          "completedBy": "Smith, Jamie",
          "supervisionRating": "",
          "crnaRating": "",
          "saraRating": "",
        });
      }
      //apply filter
      this.private_applyFilter(this.selectedFormTypes.value, this.selectedSupervisionPeriods.value);

      if (error) {
        console.error(error);
      }       
    }, 
    formView(formID) {
      console.log("formView", formID);
      this.$router.push({
        name: 'crnacmp',
        params: {formID: formID}
      });
    },
    async formClone(formID) {
      console.log("formClone", formID);
      this.formCloneAPI(formID);
      this.formSearchAPI(this.clientID, true);
    },
    formPrint(formID) {
      console.log("formPrint", formID);
    },
    formCreate() {
      console.log("formCreate", this.formData);
      this.createFormAPI();
      console.log("newCreatedFormID: ", this.newCreatedFormId);
      this.$router.push({
        name: 'crnacmp',
        params: {formID: this.newCreatedFormId}
      });
    }
  },
  computed: {
    getFormStatusColor() {
      let colorClass = {};
      colorClass[this.const_formstatus_incomplete]='yellow';
      colorClass[this.const_formstatus_complete]='green';
      colorClass[this.const_formstatus_overdue]='red';
      return colorClass;
    },
    getRatingColor() {
      let colorClass = {};
      colorClass[this.const_rating_low]='green';
      colorClass[this.const_rating_medium]='yellow';
      colorClass[this.const_rating_high]='red';
      return colorClass;
    }
  }
}
</script>

<style>
.red {
  font-size: 15px;
  color: rgb(255, 0, 0);
}

.yellow {
  font-size: 15px;
  color: rgb(205, 236, 103);
  line-height: 1.6;
}

.green {
  font-size: 15px;
  color: rgb(103, 236, 147);
  line-height: 1.6;
}

</style>