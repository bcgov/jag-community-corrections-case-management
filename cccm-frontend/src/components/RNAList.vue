<template>
  <div data-app class="p-4">
    <!--Form creation modal dialog-->
    <v-btn
      id="id_modal_form_creation"
      v-show=false
      @click.stop="dialog = true"
    ></v-btn>
    <v-dialog
        v-model="dialog"
        persistent
        max-width="550"
      >
      <v-card>
        <div class="col-sm-6 m-7">
          <strong>
            Select Form Type
          </strong>
          <v-checkbox
            v-model="selectedFormTypeValue"
            :readonly="readonly"
            label="CRNA-CMP"
            value="crna"
          ></v-checkbox>
          <v-checkbox
            v-model="selectedFormTypeValue"
            label="SARA-CMP"
            value="sara"
          ></v-checkbox>
        </div>
        <v-card-actions>
          <v-btn
            @click="dialog = false"
          >
            No, Exit
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            dark
            @click="handleFormCreateBtnClick"
          >
            Yes, Continue
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!--RNA List-->
    <div class="row justify-content-between mb-2">
      <div class="col-sm-6">
        <h1 class="font-weight-bold">RNA List</h1>
      </div>
    </div>
    <v-card>
      <section class="row justify-content-between align-items-sm-center pr-2 pl-2">
        <div class="col-sm-2" >
          <div class="mt-2 ml-3">
            <label><strong>Filter RNA Form</strong></label>
            <v-select
              item-text="value"
              item-value="key"
              v-model="selectedFormTypes"
              :items="formTypes"
              label=""
              v-on:change="applyFormTypeFilter"
              outlined
            >
            </v-select>
          </div>
        </div>
        <div class="col-sm-3" >
          <label<strong>Supervision Periods</strong></label>
          <v-radio-group
            label=""
            v-model="selectedSupervisionPeriods"
            row
            v-on:change="applyPeriodFilter"
          >
            <v-radio
              off-icon="mdi-radiobox-blank"
              on-icon="mdi-radiobox-marked"
              label="All Supervision Periods"
              value="false"
            ></v-radio>
            <v-radio
              off-icon="mdi-radiobox-blank"
              on-icon="mdi-radiobox-marked"
              label="Current Supervision Period"
              value="true"
            ></v-radio>
          </v-radio-group>
        </div>
        <div class="col-sm-3" ></div>
        <div class="col-sm-3 text-right pr-4">
          <button class="btn-primary pr-4 pl-4 pt-2 pb-2 text-center" @click="formCreate()">Create New RNA</button>
        </div>
      </section>
      <div class="dashboard-v-card text-center">
        <v-data-table
            :key="key_rnalistSearchResult"
            :headers="headers"
            :formTypes="formTypes"
            :items="filteredRNAList"
            item-key="formID"
            no-results-text="No results found"
            hide-default-footer
            :page.sync="page"
            :items-per-page="itemsPerPage"
            @page-count="pageCount = $event"
        >
          <!--Customize the formStatus field -->
          <template v-slot:item.formStatus="{ item }">
            <div :class="`w-100 h-100 ${getFormStatusColor[item.formStatus]}`">{{item.formStatus}}</div>
          </template>
          <!--Customize the supervision rating field -->
          <template v-slot:item.supervisionRating="{ item }">
            <div :class="`w-100 h-100 text-center ${getRatingColor[item.supervisionRating]}`">{{item.supervisionRating}}</div>
          </template>
          <!--Customize the CRNA rating field -->
          <template v-slot:item.crnaRating="{ item }">
            <div :class="`w-100 h-100 text-center ${getRatingColor[item.crnaRating]}`">{{item.crnaRating}}</div>
          </template>
          <!--Customize the SARA rating field -->
          <template v-slot:item.saraRating="{ item }">
            <div :class="`w-100 h-100 text-center ${getRatingColor[item.saraRating]}`">{{item.saraRating}}</div>
          </template>
          <!--Customize the action field -->
          <template v-slot:item.action="{ item }">
            <a href="#" @click="formView(item.formID, item.formType)" title="View form">
              <i class="fa fa-eye"></i>
            </a>
            &nbsp;&nbsp;
            <a href="#" @click="formClone(item.formID)" title="Copy form">
              <i class="fas fa-copy"></i>
            </a>
          </template>
        </v-data-table>
      </div>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center pt-2">
        <div class="row justify-content-between pl-3 pr-3">
          <div class="col-sm-1">
            <v-select
              solo
              :items="items"
              value=5
              dense
              item-color="primary"
              @input="itemsPerPage = parseInt($event, 10)"
            ></v-select>
          </div>
          <div class="col-sm-10">
            <v-pagination v-model="page" :total-visible="7" :length="pageCount"></v-pagination>
          </div>
        </div>
      </div>
    </v-card>
    <br/><br/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import {lookupFormTypes, formSearch, cloneForm, createForm} from "@/components/form.api";

export default {
  name: 'RNAList',
  props: {
    clientNum: {
      type: String,
      default: '',
    },
    IPVClient: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      //Const
      const_formtype_crna: "crna",
      const_formtype_sara: "sara",
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
        { text: 'RNA Form', align: 'start', sortable: true, value: 'formType' },
        { text: 'Assessment Status', value: 'assessmentStatus' },
        { text: 'Status', value: 'formStatus' },
        { text: 'Updated Date', value: 'updatedDate' },
        { text: 'Created Location', value: 'createdLocation' },
        { text: 'Completed By', value: 'completedBy' },
        { text: 'Supervision Rating', value: 'supervisionRating' },
        { text: 'CRNA Rating', value: 'crnaRating' },
        { text: 'SARA Rating', value: 'saraRating' },
        { text: 'Actions', value: 'action' },
      ],
      filteredRNAList: [],
      rnaList: [],
      selectedFormTypes: {value: "ALL", key: ""},
      formTypes: [],
      selectedSupervisionPeriods: "false",
      // form creation payload
      formData: {},
      // newly created formID
      newCreatedFormId: 0,
      dialog: false,
      readonly: true,
      selectedFormTypeValue: [],
    }
  },
  mounted(){
    this.initFormCreation();

    this.lookupFormTypesAPI();
    //form search from the backend
    this.formSearchAPI(this.clientNum, false)
  },
  methods: {
    initFormCreation() {
      console.log("this.selectedFormTypeValue this.IPVClient: ", this.selectedFormTypeValue, this.IPVClient);
      this.selectedFormTypeValue = [];

      // added const_formtype_crna to this.selectedFormTypeValue
      this.selectedFormTypeValue.push(this.const_formtype_crna);

      // if it's IPVClient, add this.const_formtype_sara to this.selectedFormTypeValue
      if (this.IPVClient) {
        this.selectedFormTypeValue.push(this.const_formtype_sara);
      }
    },
    applyFormTypeFilter(ft) {
      this.private_applyFilter(ft, this.selectedSupervisionPeriods);
    },
    applyPeriodFilter(period) {
      this.private_applyFilter(this.selectedFormTypes, period);
    },
    private_applyFilter(formType, currentPeriod) {
      if (typeof formType == 'object') {
        formType = formType.key;
      }
      if (typeof currentPeriod == 'object') {
        currentPeriod = currentPeriod.value;
      }
      this.filteredRNAList = this.rnaList.filter(el => {
        if (currentPeriod == "true") {
          return el.formType.includes(formType) && el.formStatus != 'Complete';
        } else {
          return el.formType.includes(formType);
        }
      });
      this.key_rnalistSearchResult++;
    },
    async lookupFormTypesAPI() {
      const [error, response] = await lookupFormTypes();
      if (error) {
        console.error(error);
      } else {
        if (response != null && response.items != null) {
            this.formTypes = response.items;
        }
      }
      // to be removed
      this.formTypes = [
        {value: "CRNA", key: "crna"}, 
        {value: "SARA", key: "sara"}
      ];
      this.formTypes.unshift(this.selectedFormTypes);
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
    async formSearchAPI(clientNum, tobeRemoved_addOne) {
      const [error, response] = await formSearch(clientNum, 'RNA', true);
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
      this.private_applyFilter(this.selectedFormTypes, this.selectedSupervisionPeriods);

      if (error) {
        console.error(error);
      }       
    }, 
    formView(formID, formType) {
      console.log("formView", formID);
      if (formType === 'SARA CMP') {
        this.$router.push({
          name: 'saracmp',
          params: {
            formID: formID,
            csNumber: this.clientNum
          }
        });
      } else if (formType === 'CRNA CMP') {
        this.$router.push({
          name: 'crnacmp',
          params: {
            formID: formID,
            csNumber: this.clientNum
          }
        });
      } else {
        console.error("Form type not supported");
      }
    },
    async formClone(formID) {
      console.log("formClone", formID);
      this.formCloneAPI(formID);
      this.formSearchAPI(this.clientNum, true);
    },
    handleFormCreateBtnClick() {
      this.dialog = false;
      console.log("selectedFormTypeValue: ", this.selectedFormTypeValue);

      this.createFormAPI();
      //Redirect User to the newly created form
      console.log("newCreatedFormID: ", this.newCreatedFormId);

      let nextView = "crnacmp";
      if (this.selectedFormTypeValue.includes("sara")) {
        nextView = "saracmp";
      }
      this.$router.push({
        name: nextView,
        params: {
          formID: this.newCreatedFormId,
          csNumber: this.clientNum
        }
      });
    },
    formCreate() {
      console.log("Create form btn click");
      let modal = document.getElementById("id_modal_form_creation");
      if (modal != null) {
        modal.click();
      }
    }
  },
  computed: {
    getFormStatusColor() {
      let colorClass = {};
      colorClass[this.const_formstatus_incomplete]='dashboard-background-color-yellow';
      colorClass[this.const_formstatus_complete]='dashboard-background-color-green';
      colorClass[this.const_formstatus_overdue]='dashboard-background-color-red';
      return colorClass;
    },
    getRatingColor() {
      let colorClass = {};
      colorClass[this.const_rating_low]='dashboard-background-color-green';
      colorClass[this.const_rating_medium]='dashboard-background-color-yellow';
      colorClass[this.const_rating_high]='dashboard-background-color-red';
      return colorClass;
    }
  }
}
</script>