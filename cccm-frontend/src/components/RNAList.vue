<template>
  <div data-app class="p-4">
    <!-- CRNA/SARA Form creation modal dialog-->
    <v-btn
      :id="`${CONST_MODAL_ID_PREFIX}${$CONST_FORMTYPE_RNA}`"
      v-show=false
      @click.stop="dialog = true"
    ></v-btn>
    <!-- Acute Form creation modal dialog-->
    <v-btn
      :id="`${CONST_MODAL_ID_PREFIX}${$CONST_FORMTYPE_ACUTE}`"
      v-show=false
      @click.stop="dialog = true"
    ></v-btn>
    <!-- STAT99R Form creation modal dialog-->
    <v-btn
      :id="`${CONST_MODAL_ID_PREFIX}${$CONST_FORMTYPE_STAT99R}`"
      v-show=false
      @click.stop="dialog = true"
    ></v-btn>
    <v-dialog
        v-model="dialog"
        persistent
        max-width="550"
      >
      <v-card>
        <div v-if="formToCreate == $CONST_FORMTYPE_RNA" class="col-sm-6 m-7">
          <strong>
            Select Form Type
          </strong>
          <v-checkbox
            v-model="selectedFormtypeForFormCreate"
            :readonly="readonly"
            label="CRNA-CMP"
            :value="$CONST_FORMTYPE_CRNA"
          ></v-checkbox>
          <v-checkbox
            v-model="selectedFormtypeForFormCreate"
            label="SARA-CMP"
            :value="$CONST_FORMTYPE_SARA"
          ></v-checkbox>
          <v-checkbox
            v-model="selectedFormtypeForFormCreate"
            label="STABLE"
            :value="$CONST_FORMTYPE_STABLE"
            @click="onSTABLESelectionChange"
          ></v-checkbox>
          <v-checkbox
            v-model="selectedFormtypeForFormCreate"
            :readonly="readonly"
            label="OVERALL"
            :value="$CONST_FORMTYPE_OVERALL"
          ></v-checkbox>
        </div>
        <div v-if="formToCreate == $CONST_FORMTYPE_ACUTE" class="col-sm-10 m-10">
          <strong>Are you sure you want to create a new Acute form?</strong>
        </div>
        <div v-if="formToCreate == $CONST_FORMTYPE_STAT99R" class="col-sm-10 m-10">
          <strong>Are you sure you want to create a new Static 99R form?</strong>
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
        <div class="sectionTitleClass mr-4 col-3 font-weight-bold">RNA List</div>
      </div>
    </div>
    <v-card>
      <section class="row justify-content-between align-items-sm-center pr-2 pl-2">
        <div class="col-sm-2">
          <div class="mt-2 ml-3">
            <strong>Filter RNA Form</strong>
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
        <div class="col-sm-2">
          <strong>Supervision Periods</strong>
          <v-radio-group label="" v-model="currentPeriod" row v-on:change="applyPeriodFilter">
            <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="All Supervision Periods"
              value="all"></v-radio>
            <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="Current Supervision Period"
              value="current"></v-radio>
          </v-radio-group>
        </div>
        <div class="col-sm-2">
          <div class="rna-overdue-text">RNA form overdue list: </div>
          <div class="rna-overdue-red">{{ getOverdueRNAFormtypes }}</div>
        </div>
        <div class="col-sm-2">
          <button class="btn-primary text-center" @click="formCreate($CONST_FORMTYPE_STAT99R)">Create New Static 99R</button>
        </div>
        <div class="col-sm-2">
          <button class="btn-primary text-center" @click="formCreate($CONST_FORMTYPE_ACUTE)">Create New Acute</button>
        </div>
        <div class="col-sm-2">
          <button class="btn-primary text-center" @click="formCreate($CONST_FORMTYPE_RNA)">Create New RNA</button>
        </div>
      </section>
      <section>
        
      </section>
      <div class="dashboard-v-card text-center">
        <v-data-table :key="key_rnalistSearchResult" :headers="headers" :formTypes="formTypes" :items="rnaList"
          item-key="id" no-results-text="No results found" hide-default-footer :page.sync="page"
          :loading="loading"   loading-text="Loading RNA List... Please wait"
          :items-per-page="itemsPerPage" @page-count="pageCount = $event">
          <!-- Customize the assessment status -->
          <template v-slot:item.reassessment="{ item }">
            <div class="w-100 h-100 d-flex justify-content-center align-items-center">{{getAssessmentStatus(item.reassessment, item.module)}}</div>
          </template>
          <!--Customize the formStatus field -->
          <template v-slot:item.status="{ item }">
            <div :class="`w-100 h-100 d-flex align-items-center justify-content-center ${getFormStatusColor[item.status]}`">{{item.status}}&nbsp;<i :class="[item.locked ? 'fa fa-lock' : '']"></i></div>
          </template>
          <!--Customize the date field field -->
          <template v-slot:item.updatedDate="{ item }">
            <div class="w-100 h-100 d-flex justify-content-center align-items-center">{{ getUpdatedDate(item)}}</div>
          </template>
          <!--Customize the supervision rating field -->
          <template v-slot:item.supervisionRating="{ item }">
            <div :class="`w-100 h-100 d-flex align-items-center justify-content-center ${getRatingColor(item,'Supervision')}`">
              {{getRating(item,"Supervision")}}</div>
          </template>
          <!--Customize the CRNA rating field -->
          <template v-slot:item.crnaRating="{ item }">
            <div :class="`w-100 h-100 d-flex align-items-center justify-content-center ${getRatingColor(item,'CRNA')}`">{{ getRating(item,"CRNA")}} </div>
          </template>
          <!--Customize the SARA rating field -->
          <template v-slot:item.saraRating="{ item }">
            <div :class="`w-100 h-100 d-flex align-items-center justify-content-center ${getRatingColor(item,'SARA')}`">{{ getRating(item,"SARA")}}</div>
          </template>
          <!--Customize the action field -->
          <template v-slot:item.action="{ item }">
            <a href="#" @click="formView(item.id)" title="View form">
              <i class="fa fa-eye"></i>
            </a>
            &nbsp;&nbsp;
            <div style="display:inline-block" :title="getCloneTooltip(item)">
              <a href="#" :class="[canClone(item) ? '' : 'disabled']" @click="formClone(item.id)">
                <i class="fa fa-copy"></i>
              </a>
            </div>
            &nbsp;&nbsp;
            <a href="#" @click="formPrint(item.id)" title="Print form">
              <i class="fa fa-print"></i>
            </a>
          </template>
        </v-data-table>
      </div>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center pt-2">
        <div class="row justify-content-between pl-3 pr-3">
          <div class="col-sm-1">
            <v-select solo :items="items" v-model="itemsPerPage" dense item-color="primary"
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
import { formSearch, cloneForm, createForm } from "@/components/form.api";

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
    },
    IPVClient: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      //Const
      const_rating_low: "Low",
      const_rating_medium: "Medium",
      const_rating_high: "High",
      CONST_MODAL_ID_PREFIX: 'id_modal_form_creation_',
      key_rnalistSearchResult: 0,
      // datatable variables
      items: this.$CONST_DATATABLE_PAGE_FILTERLSIT,
      page: 1,
      pageCount: 1,
      itemsPerPage: this.$CONST_DATATABLE_ITEMS_PER_PAGE,
      currentPeriod: "all",
      totalClients: 0,
      loading: true,
      headers: [
        { text: 'RNA Form', align: 'start', sortable: true, value: 'module' },
        { text: 'Assessment Status', value: 'reassessment' },
        { text: 'Status', value: 'status' },
        { text: 'Updated Date', value: 'updatedDate' },
        { text: 'Created Location', value: 'location' },
        { text: 'Created By', value: 'createdBy' },
        { text: 'Supervision Rating', value: 'supervisionRating' },
        { text: 'CRNA Rating', value: 'crnaRating' },
        { text: 'SARA Rating', value: 'saraRating' },
        { text: 'Actions', value: 'action' },
      ],
      rnaList: [],
      selectedFormTypes: {value: "ALL", key: this.$CONST_FORMTYPE_RNA},
      formTypes: this.$CONST_FORM_TYPES,
      dialog: false,
      readonly: true,
      selectedFormtypeForFormCreate: [],
      overdueRNAFormtypes: '',
      formToCreate: ''
    }
  },
  mounted() {
    this.initFormCreationSelection();
    this.formSearchAPI(this.selectedFormTypes.key);
  },
  methods: {
    onSTABLESelectionChange() {
      // if 'STABLE' is selected, auto select 'OVERALL'
      if (this.selectedFormtypeForFormCreate.includes(this.$CONST_FORMTYPE_STABLE)) {
        this.selectedFormtypeForFormCreate.push(this.$CONST_FORMTYPE_OVERALL);
      } else {
        // remove 'OVERALL' if 'STABLE' is unselected
        this.selectedFormtypeForFormCreate = this.selectedFormtypeForFormCreate.filter(arrayItem => arrayItem !== this.$CONST_FORMTYPE_OVERALL);
      }
    },
    canClone(item) {
      // User cannot clone:
      // 1. another user’s CRNA-SARA-CMP,
      // 2. an incomplete CRNA-SARA-CMP, nor
      // 3. a previous version of the CRNA-SARA-CMP.
      if (item.createdBy != Vue.$keycloak.tokenParsed.preferred_username || 
          !item.complete ||
          !item.mostRecent) {
        return false
      }
      return true
    },
    getCloneTooltip(item) {
      if (item.createdBy != Vue.$keycloak.tokenParsed.preferred_username) {
        return "User cannot clone another user's form"
      }
      if (!item.complete) {
        return 'User cannot clone an incomplete form'
      }
      if (!item.mostRecent) {
        return 'User cannot clone a previous version of the form'
      }
      return 'Copy form';
    },
    getAssessmentStatus(isReassessment, formType) {
      if (formType == 'CRNA-SARA') {
        formType = this.$CONST_FORMTYPE_SARA;
      }
      let theForm = this.$FORM_INFO.filter( item => item.formType === formType );
      //console.log("formType: ", formType);
      if (theForm != null && theForm[0] != null && !theForm[0].assessmentStatusRequired) {
        return '';
      }
      return (isReassessment) ? this.$FORM_TYPE_REASSESSMENT : this.$FORM_TYPE_INITIAL;
    },
    getUpdatedDate(item) {
      return (item.updatedDate) ? item.updatedDate : item.createdDate;
    },
    getRating(item, key) {
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
    initFormCreationSelection() {
      //console.log("this.selectedFormtypeForFormCreate this.IPVClient: ", this.selectedFormtypeForFormCreate, this.IPVClient);
      this.selectedFormtypeForFormCreate = [];

      // added $CONST_FORMTYPE_CRNA to this.selectedFormtypeForFormCreate
      this.selectedFormtypeForFormCreate.push(this.$CONST_FORMTYPE_CRNA);

      // if it's IPVClient, add this.$CONST_FORMTYPE_SARA to this.selectedFormtypeForFormCreate
      if (this.IPVClient) {
        this.selectedFormtypeForFormCreate.push(this.$CONST_FORMTYPE_SARA);
      }
    },
    applyPeriodFilter() {
      //console.log("Filter updating periods %o", this.currentPeriod);
      let formType = this.$CONST_FORMTYPE_RNA;
      if (typeof this.selectedFormTypes == 'object') {
        formType = this.selectedFormTypes.key;
      } else {
        formType = this.selectedFormTypes;
      }
      this.formSearchAPI(formType);
    },
    applyFormTypeFilter(formType) {
      if (typeof formType == 'object') {
        formType = formType.key;
      }
      this.formSearchAPI(formType);
    },
    async createFormAPI(formType) {
      let formData = {};
      // set formData
      formData.clientNumber = this.clientNum;
      formData.linkedClientFormId = null;

      // need to create a new form instance
      //console.log("create {}", formType);
      const [error, newformId] = await createForm(formType.toLowerCase() , formData);
      if (error) {
        console.error("Failed creating a(an) new {} form instance", formType, error);
      } else {
        //Redirect User to the newly created form
        this.$router.push({
          name: this.$ROUTER_NAME_CMPFORM,
          params: {
            formID: newformId,
            csNumber: this.clientNum
          }
        });
      }
    },
    async formCloneAPI(formID) {
      let formData = {};
      // set formData
      formData.clientNumber = this.clientNum;
      formData.clientFormId = formID;

      const [error, newFormId] = await cloneForm(formData);
      if (error) {
        console.error(error);
      } else {
        //Redirect User to the newly created form
        this.$router.push({
          name: this.$ROUTER_NAME_CMPFORM,
          params: {
            formID: newFormId,
            csNumber: this.clientNum
          }
        });
      }
    },
    async formSearchAPI(formType) {
      this.loading = true;
      try {
        let period = (this.currentPeriod === 'current') ? true : false;
        const [error, response] = await formSearch(this.clientNum, formType, period);
        if (error) {
          console.error(error);
        } else {
          this.key_rnalistSearchResult++;
          this.rnaList = response;
          //console.log("RNAList search: ", response);
          this.rnaList = this.rnaList.filter(el => {
            el.status = this.$FORM_STATUS_INCOMPLETE;
            if (el.complete) {
              el.status = this.$FORM_STATUS_COMPLETE;
            }
            
            return el;
          });
          //console.log("RNAList: ", this.rnaList);
        }
      } finally {
        this.loading = false;
      }
    },
    formView( formID ) {
      if (formID != null) {
        formID = formID.toString();
      }
      this.$router.push({
        name: this.$ROUTER_NAME_CMPFORM,
        params: {
          formID: formID,
          csNumber: this.clientNum
        }
      });
    },
    async formClone(formID) {
      this.formCloneAPI(formID);
      this.formSearchAPI(this.$CONST_FORMTYPE_RNA);
    },
    formPrint(formID) {
      //Bring User to the form instance and trigger the print
      const route = this.$router.resolve({ 
        name: this.$ROUTER_NAME_CMPFORM,
        params: {
          formID: formID,
          csNumber: this.clientNum,
          print: true
        }
      }); 
      window.open(route.href, '_blank');
    },
    async handleFormCreateBtnClick() {
      this.dialog = false;
      let formType = this.formToCreate;
      if (this.formToCreate == this.$CONST_FORMTYPE_RNA) {
        formType = this.$CONST_FORMTYPE_CRNA;
        if (this.selectedFormtypeForFormCreate.includes(this.$CONST_FORMTYPE_SARA)) {
          formType = this.$CONST_FORMTYPE_SARA;
        }
      }
      this.createFormAPI(formType);
    },
    formCreate(formType) {
      //console.log("Create form btn click");
      let modal = document.getElementById(this.CONST_MODAL_ID_PREFIX + formType);
      this.formToCreate = formType;
      if (modal != null) {
        modal.click();
      }
    }
  },
  computed: {
    getOverdueRNAFormtypes() {
      return this.overdueRNAFormtypes;
    },
    getFormStatusColor() {
      let colorClass = {};
      colorClass[this.$FORM_STATUS_INCOMPLETE] = 'dashboard-background-color-yellow';
      colorClass[this.$FORM_STATUS_COMPLETE] = 'dashboard-background-color-green';
      return colorClass;
    }
  }
}
</script>

<style scoped>
  a.disabled {
    opacity: 0.5;
    pointer-events: none;
  }
</style>
