<template>
  <div data-app class="p-4">
    <div class="row justify-content-between mb-2">
      <div class="col-sm-6">
        <h1 class="font-weight-bold">RNA List</h1>
      </div>
    </div>
    <v-card>
      <section class="row justify-content-between align-items-sm-center pr-2 pl-2">
        <div class="col-sm-2">
          <div class="mt-2 ml-3">
            <label><strong>Filter RNA Form</strong></label>
            <v-select item-text="text" item-value="value" v-model="selectedFormTypes" :items="formTypes" label=""
              v-on:change="applyFormTypeFilter" outlined>
            </v-select>
          </div>
        </div>
        <div class="col-sm-3">
          <label<strong>Supervision Periods</strong></label>
            <v-radio-group label="" v-model="selectedSupervisionPeriods" row v-on:change="applyPeriodFilter">
              <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="All Supervision Periods"
                value="false"></v-radio>
              <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="Current Supervision Period"
                value="true"></v-radio>
            </v-radio-group>
        </div>
        <div class="col-sm-3"></div>
        <div class="col-sm-3 text-right pr-4">
          <button class="btn-primary pr-4 pl-4 pt-2 pb-2 text-center" @click="formCreate()">New RNA</button>
        </div>
      </section>
      <div class="dashboard-v-card text-center">
        <v-data-table :key="key_rnalistSearchResult" :headers="headers" :formTypes="formTypes" :items="filteredRNAList"
          item-key="formID" no-results-text="No results found" hide-default-footer :page.sync="page"
          :items-per-page="itemsPerPage" @page-count="pageCount = $event">
          <!-- Customize the assessment status -->
          <template v-slot:item.reassessment="{ item }">
            <div class="w-100 h-100">{{getAssessmentStatus(item.reassessment)}}</div>
          </template>
          <!--Customize the formStatus field -->
          <template v-slot:item.status="{ item }">
            <div :class="`w-100 h-100 ${getFormStatusColor[item.status]}`">{{item.status}}</div>
          </template>
          <!--Customize the date field field -->
          <template v-slot:item.updatedDate="{ item }">
            <div class="w-100 h-100">{{ getUpdatedDate(item)}}</div>
          </template>
          <!--Customize the supervision rating field -->
          <template v-slot:item.supervisionRating="{ item }">
            <div :class="`w-100 h-100 text-center ${getRatingColor(item,'Supervision')}`">
              {{getRating(item,"Supervision")}}</div>
          </template>
          <!--Customize the CRNA rating field -->
          <template v-slot:item.crnaRating="{ item }">
            <div :class="`w-100 h-100 text-center ${getRatingColor(item,'CRNA')}`">{{ getRating(item,"CRNA")}} </div>
          </template>
          <!--Customize the SARA rating field -->
          <template v-slot:item.saraRating="{ item }">
            <div :class="`w-100 h-100 text-center ${getRatingColor(item,'SARA')}`">{{ getRating(item,"SARA")}}</div>
          </template>
          <!--Customize the action field -->
          <template v-slot:item.action="{ item }">
            <a href="#" @click="formView(item.id, item.module)" title="View form">
              <i class="fa fa-eye"></i>
            </a>
            &nbsp;&nbsp;
            <a href="#" @click="formClone(item.formID)" title="Copy form">
              <i class="fa fa-copy"></i>
            </a>
            &nbsp;&nbsp;
            <a href="#" @click="formPrint(item.formID)" title="Print form">
              <i class="fa fa-print"></i>
            </a>
          </template>
        </v-data-table>
      </div>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center pt-2">
        <div class="row justify-content-between pl-3 pr-3">
          <div class="col-sm-1">
            <v-select solo :items="items" value=5 dense item-color="primary"
              @input="itemsPerPage = parseInt($event, 10)"></v-select>
          </div>
          <div class="col-sm-10">
            <v-pagination v-model="page" :total-visible="7" :length="pageCount"></v-pagination>
          </div>
        </div>
      </div>
    </v-card>
    <br /><br />
  </div>


</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { formSearch, cloneForm, createForm, getFormSummaries } from "@/components/form.api";

export default {
  name: 'RNAList',
  props: {
    clientID: {
      type: String,
      default: ''
    },
    clientNum: {
      type: String,
      default: '',
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
        { text: 'Id (debug)', align: 'start', sortable: true, value: 'id' },

        { text: 'RNA Form', align: 'start', sortable: true, value: 'module' },
        { text: 'Assessment Status', value: 'reassessment' },
        { text: 'Status', value: 'status' },
        { text: 'Updated Date', value: 'updatedDate' },
        { text: 'Created Location', value: 'location' },
        { text: 'Completed By', value: 'createdBy' },
        { text: 'Supervision Rating', value: 'supervisionRating' },
        { text: 'CRNA Rating', value: 'crnaRating' },
        { text: 'SARA Rating', value: 'saraRating' },
        { text: 'Actions', value: 'action' },
      ],
      filteredRNAList: [],
      rnaList: [],
      selectedFormTypes: { text: "ALL", value: "" },
      formTypes: [{ text: "ALL", value: "" }, { text: "CRNA", value: "CRNA" }, { text: "SARA", value: "SARA" }],
      selectedSupervisionPeriods: "false",
      // form creation payload
      formData: {},
      // newly created formID
      newCreatedFormId: 0,
    }
  },
  mounted() {
    //form search from the backend
    this.formSearchAPI(this.clientNum, false)
  },
  methods: {
    getAssessmentStatus(isReassessment) {
      return (isReassessment) ? "Reassessment" : "Initial";
    },
    getUpdatedDate(item) {
      return (item.updatedDate) ? item.updatedDate : item.createdDate;
    },
    getRating(item, key) {
      console.log("Item %o", item);
      if (item.ratings[key]) {
        return item.ratings[key];
      } else {
        return "Not set";
      }
    },

    getRatingColor(item, key) {
      let colorClass = '';
      let rating = this.getRating(item, key);

      switch (rating) {
        case this.const_rating_low: {
          colorClass = 'dashboard-background-color-green';
          break;
        }
        case this.const_rating_medium: {
          colorClass = 'dashboard-background-color-yellow';
          break;
        }
        case this.const_rating_high: {
          colorClass = 'dashboard-background-color-red';
          break;
        }
      }

      return colorClass;
    },
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
      this.filteredRNAList = this.rnaList; // .filter(el => {
      //   if (currentPeriod == "true") {
      //     return el.formType.includes(formType) && el.formStatus != 'Complete';
      //   } else {
      //     return el.formType.includes(formType);
      //   }
      // });
      this.key_rnalistSearchResult++;
    },
    async createFormAPI() {

      // get latest form version
      const [err, formInfo] = await getFormSummaries('CRNA', true);
      this.formData.clientNumber = "00142091";
      this.formData.formTypeId = formInfo[0].id;

      const [error, formId] = await createForm(this.formData);
      if (error) {
        console.error(error);
      }
      return formId;
    },
    async formCloneAPI(formID) {
      const [error, response] = await cloneForm(formID);
      if (error) {
        console.error(error);
      }
    },
    async formSearchAPI(clientNum, tobeRemoved_addOne) {

      console.log('formSearchAPI(%s,%o)', clientNum, tobeRemoved_addOne);
      const [error, response] = await formSearch(clientNum, 'RNA', true);
      //this.initData = response.data;
      this.key_rnalistSearchResult++;
      this.loading = false;
      console.log("Response %o", response);
      this.rnaList = response;


      this.private_applyFilter(this.selectedFormTypes, this.selectedSupervisionPeriods);

      if (error) {
        console.error(error);
      }
    },
    formView( formID, formType) {
      console.log("formView %s %s", formID, formType);
      if (formType === 'SARA') {
        this.$router.push({
          name: 'saracmp',
          params: {
            formID: formID,
            clientNum: this.clientNum,
            clientID: this.clientID
          }
        });
      } else if (formType === 'CRNA') {
        this.$router.push({
          name: 'crnacmp',
          params: {
            formID: formID,
            clientNum: this.clientNum,
            clientID: this.clientID
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
    formPrint(formID) {
      console.log("formPrint", formID);
    },
    async formCreate() {
      const newFormId = await this.createFormAPI();

      this.$router.push({
        name: 'crnacmp',
        params: { formID: newFormId }
      });
    }
  },
  computed: {
    getFormStatusColor() {
      let colorClass = {};
      colorClass[this.const_formstatus_incomplete] = 'dashboard-background-color-yellow';
      colorClass[this.const_formstatus_complete] = 'dashboard-background-color-green';
      colorClass[this.const_formstatus_overdue] = 'dashboard-background-color-red';
      return colorClass;
    }
  }
}
</script>