<template>
  <div data-app class="p-4">
    <div v-if="showFormCreateButtons">
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
      <!-- STABLE Form creation modal dialog-->
      <v-btn
        :id="`${CONST_MODAL_ID_PREFIX}${$CONST_FORMTYPE_STABLE}`"
        v-show=false
        @click.stop="dialog = true"
      ></v-btn>
      <!-- SMO-Overall-CMP Form creation modal dialog-->
      <v-btn
        :id="`${CONST_MODAL_ID_PREFIX}${$CONST_FORMTYPE_SO_OVERALL}`"
        v-show=false
        @click.stop="dialog = true"
      ></v-btn>
      <!-- CMRP Form creation modal dialog-->
      <v-btn
          :id="`${CONST_MODAL_ID_PREFIX}${$CONST_FORMTYPE_CMRP}`"
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
              label="SARA"
              :value="$CONST_FORMTYPE_SARA"
            ></v-checkbox>
          </div>
          <div v-if="formToCreate == $CONST_FORMTYPE_ACUTE" class="col-sm-10 m-10">
            <strong>Are you sure you want to create a new Acute form?</strong>
          </div>
          <div v-if="formToCreate == $CONST_FORMTYPE_STAT99R" class="col-sm-10 m-10">
            <strong>Are you sure you want to create a new Static-99R form?</strong>
          </div>
          <div v-if="formToCreate == $CONST_FORMTYPE_STABLE" class="col-sm-10 m-10">
            <strong>Are you sure you want to create a new Stable form?</strong>
          </div>
          <div v-if="formToCreate == $CONST_FORMTYPE_SO_OVERALL" class="col-sm-10 m-10">
            <strong>Are you sure you want to create a new SMO-Overall-CMP form?</strong>
          </div>
          <div v-if="formToCreate == $CONST_FORMTYPE_CMRP" class="col-sm-10 m-10">
            <strong>Are you sure you want to create a new Custody-CMRP form?</strong>
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
    </div>

    <!--RNA List-->
    <div class="row justify-content-between mb-2">
        <div class="sectionTitleClass mr-4 col-3 font-weight-bold">RNA List</div>
    </div>
    <v-card class="py-2">
      <section v-if="showFormCreateButtons" class="row justify-content-between align-items-sm-center pr-2 pl-2">
        <div class="col-sm-6"></div>
        <div class="col-sm-2">
          <button class="btn-primary text-center" @click="formCreate($CONST_FORMTYPE_RNA)">Create New RNA-CMP</button>
        </div>
        <div class="col-sm-2">
          <button class="btn-primary text-center" @click="formCreate($CONST_FORMTYPE_STAT99R)">Create New Static-99R</button>
        </div>
        <div class="col-sm-2">
          <button class="btn-primary text-center" @click="formCreate($CONST_FORMTYPE_ACUTE)">Create New Acute</button>
        </div>
      </section>
      <section v-if="showFormCreateButtons" class="row justify-content-between align-items-sm-center pr-2 pl-2">
        <div class="col-sm-6"></div>
        <div class="col-sm-2">
          <button class="btn-primary text-center" @click="formCreate($CONST_FORMTYPE_STABLE)">Create New Stable</button>
        </div>
        <div class="col-sm-2">
          <button class="btn-primary text-center" @click="formCreate($CONST_FORMTYPE_SO_OVERALL)">Create New SMO-Overall-CMP</button>
        </div>
        <div class="col-sm-2">
          <button v-if="showCMRPCreateButton" class="btn-primary text-center" @click="formCreate($CONST_FORMTYPE_CMRP)">Create New Custody-CMRP</button>
        </div>
      </section>
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
        <div class="col-sm-6">
          <strong>Supervision Periods</strong>
          <section class="row pr-2 pl-2">
            
            <div class="col-sm-4">
              <v-radio-group label="" v-model="currentPeriod" row v-on:change="applyPeriodFilter">
                <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="Current"
                  value="current"></v-radio>
                <v-radio off-icon="mdi-radiobox-blank" on-icon="mdi-radiobox-marked" label="All"
                  value="all"></v-radio>
              </v-radio-group>
            </div>
            <div class="col-sm-4">
              <v-checkbox
                v-model="mostRecent"
                label="Most Recent"
                v-on:change="applyPeriodFilter"
              ></v-checkbox>
            </div>
          </section>
        </div>
        <div class="col-sm-2">
          <div class="mt-2 ml-3">
            <div class="rna-overdue-text">RNA form overdue list: </div>
            <div class="rna-overdue-red">{{ getOverdueRNAFormtypes }}</div>
          </div>
        </div>
        <div class="col-sm-2"></div>
      </section>
      <div class="dashboard-v-card">
        <v-data-table
            :key="key_rnalistSearchResult"
            :headers="headers"
            :formTypes="formTypes"
            :items="rnaList"
            item-key="id"
            no-results-text="No results found"
            hide-default-footer
            class="text-center"
            :page.sync="page"
            :loading="loading"
            loading-text="Loading RNA List... Please wait"
            :items-per-page="itemsPerPage"
            @page-count="pageCount = $event"
        >
          <!-- Customize the module value -->
          <template v-slot:item.module="{ item }">
            <div>{{getFormTypeDesc(item.module)}}</div>
          </template>
          <!-- Customize the assessment status -->
          <template v-slot:item.reassessment="{ item }">
            <div>{{getAssessmentStatus(item.reassessment, item.module)}}</div>
          </template>
          <!--Customize the formStatus field -->
          <template v-slot:item.status="{ item }">
            <div :class="getFormStatusColor[item.status]">
              {{item.status}}&nbsp;
              <font-awesome-icon v-if="item.locked" :icon="['fa', 'fa-lock']" />
            </div>
          </template>
          <!--Customize the supervision rating field -->
          <template v-slot:item.supervisionRating="{ item }">
            <div :class="getRatingColor(item.supervisionRating, item.module)">
              {{ getLMHRatingDisplay(item.supervisionRating) }}</div>
          </template>
          <!--Customize the CRNA rating field -->
          <template v-slot:item.crnaRating="{ item }">
            <div :class="getRatingColor(item.crnaRatingVal, item.module)">{{ item.crnaRating }}</div>
          </template>
          <!--Customize the SARA rating field -->
          <template v-slot:item.saraRating="{ item }">
            <div :class="getRatingColor(item.saraRatingVal, item.module)">{{ item.saraRating }}</div>
          </template>
          <!--Customize the smoRating rating field -->
          <template v-slot:item.smoRating="{ item }">
            <div :class="getRatingColor(item.smoRatingVal, item.module)">{{ item.smoRating }}</div>
          </template>
          <!--Customize the action field -->
          <template v-slot:item.action="{ item }">
            <a href="#" @click="formView(item.id)" title="View form">
              <font-awesome-icon :icon="['fa', 'fa-eye']" />
            </a>
            &nbsp;&nbsp;
            <div style="display:inline-block" :title="getCloneTooltip(item)">
              <a href="#" :class="[canClone(item) ? '' : 'disabled']" @click="formClone(item.id, item.module)">
                <font-awesome-icon :icon="['fa', 'fa-copy']" />
              </a>
            </div>
            &nbsp;&nbsp;
            <div style="display:inline-block" :title="getPrintTooltip(item)">
              <a href="#" :class="[canPrint(item) ? '' : 'disabled']" @click="formPrint(item.id)" title="Print form">
                <font-awesome-icon :icon="['fa', 'fa-print']" />
              </a>
          </div>
          </template>
        </v-data-table>
      </div>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center px-3">
        <DatatablePagination :items-per-page.sync="itemsPerPage" :page.sync="page" :page-count="pageCount"/>
      </div>
    </v-card>
    <br /><br />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { formSearch, cloneForm, createForm } from "@/components/form.api";
import {useStore} from "@/stores/store";
import {mapStores} from 'pinia';
import DatatablePagination from "@/components/common/DatatablePagination.vue";
import { dateToCCCMDateformat } from './dateUtils';

export default {
  name: 'RNAList',
  components: { DatatablePagination },
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
    },
    SMOClient: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      //Const
      const_crna_sara: 'CRNA-SARA',
      const_supervision: "Supervision",
      const_rating_low: "L",
      const_rating_medium: "M",
      const_rating_high: "H",
      const_rating_display_low: "Low",
      const_rating_display_medium: "Medium",
      const_rating_display_high: "High",
      CONST_MODAL_ID_PREFIX: 'id_modal_form_creation_',
      key_rnalistSearchResult: 0,
      // datatable variables
      page: 1,
      pageCount: 1,
      itemsPerPage: this.$CONST_DATATABLE_ITEMS_PER_PAGE,
      currentPeriod: "current",
      totalClients: 0,
      loading: true,
      headers: [
        { text: 'RNA Form', align: 'start', sortable: true, value: 'module' },
        { text: 'Assessment Status', sortable: true, value: 'assessmentStatusDisplay' },
        { text: 'Status', sortable: true, value: 'status' },
        { text: 'Updated Date', sortable: true, value: 'updatedDateDisplay' },
        { text: 'Created Location', sortable: true, value: 'location' },
        { text: 'Created By', sortable: true, value: 'createdBy' },
        { text: 'Supervision Level', sortable: true, value: 'supervisionRating' },
        { text: 'CRNA Rating', sortable: true, value: 'crnaRating' },
        { text: 'SARA Rating', sortable: true, value: 'saraRating' },
        { text: 'SMO Ratings', sortable: true, value: 'smoRating' },
        { text: 'Actions', value: 'action' },
      ],
      rnaList: [],
      selectedFormTypes: {value: "ALL", key: this.$CONST_FORMTYPE_RNA},
      formTypes: [],
      dialog: false,
      readonly: true,
      selectedFormtypeForFormCreate: [],
      overdueRNAFormtypes: '',
      formToCreate: '',
      mostRecent: false
    }
  },
  created() {
    this.rnaList = [];
  },
  mounted() {
    this.formTypes = this.mainStore.getSupportedFormTypes();
    this.formSearchAPI(this.selectedFormTypes.key);
  },
  methods: {
    canClone(item) {
      // User can clone when:
      // User is an admin or the form is CMRP and use is in ITRP group
      if (this.mainStore.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ADMIN) || (
          this.mainStore.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ITRP) &&
          item.module === this.$CONST_FORMTYPE_CMRP )) {
        return true;
      }

      // Edge case: if user doesn't have idirId, return false
      if (item.createdByIdir == null) {
        return false;
      }

      // User cannot clone when:
      // 0. user is a researcher
      // 1. another user’s CRNA-SARA-CMP,
      // 2. an incomplete CRNA-SARA-CMP, nor
      // 3. a previous version of the CRNA-SARA-CMP.
      if (this.mainStore.loginUserGroups.includes(Vue.prototype.$USER_GROUP_RESEARCHER) ||
          this.mainStore.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ADMIN_COMM)) {
        return false;
      }

      if (item.createdByIdir.toUpperCase() != Vue.$keycloak.tokenParsed.preferred_username.toUpperCase() || 
          !item.complete ||
          !item.mostRecent) {
        return false;
      }

      return true
    },
    getCloneTooltip(item) {
      if (this.mainStore.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ADMIN) || (
          this.mainStore.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ITRP) &&
          item.module === this.$CONST_FORMTYPE_CMRP )) {
        return 'Copy form';
      }
      
      if (this.mainStore.loginUserGroups.includes(Vue.prototype.$USER_GROUP_RESEARCHER) ||
          this.mainStore.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ADMIN_COMM)) {
        return "User is not allowed to clone form";
      }
      if (!item.complete) {
        return 'User cannot clone an incomplete form'
      }
      if (!item.mostRecent) {
        return 'User cannot clone a previous version of the form'
      }

      // Edge case: if user doesn't have idirId, return false
      if (item.createdByIdir == null) {
        return "Login user doesn't contain idirId, cannot determine the ability to clone";
      }
      if (item.createdByIdir.toUpperCase() != Vue.$keycloak.tokenParsed.preferred_username.toUpperCase()) {
        return "User cannot clone another user's form"
      }

      return 'Copy form';
    },
    canPrint(item) {
      if (this.mainStore.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ADMIN_COMM)) {
        return false;
      }
      if (item.formTypeExpiryDate != null) {
        return false;
      }
      return true;
    },
    getPrintTooltip(item) {
      if (this.mainStore.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ADMIN_COMM)) {
        return 'User is not allowed to print form';
      }
      if (item.formTypeExpiryDate != null) {
        return 'Print old forms are not supported yet.';
      }
      return 'Print form';
    },
    getFormTypeDesc(formType) {
      let theForm = this.$FORM_INFO.filter( item => item.formType === formType );
      if (theForm != null && theForm[0] != null) {
          return theForm[0].formTypeDesc;
      }
      return formType;
    },
    getAssessmentStatus(isReassessment, formType) {
      if (formType == this.const_crna_sara) {
        formType = this.$CONST_FORMTYPE_SARA;
      }
      let theForm = this.$FORM_INFO.filter( item => item.formType === formType );
      //console.log("formType: ", formType);
      if (theForm != null && theForm[0] != null && !theForm[0].assessmentStatusRequired) {
        return '';
      }
      return isReassessment == null ? "" : (isReassessment) ? this.$FORM_TYPE_REASSESSMENT : this.$FORM_TYPE_INITIAL;
    },
    getLMHRatingDisplay(rating) {
      if (rating == null || rating == '') {
        return '';
      }
      let displayText = rating;
      switch (rating.toUpperCase()) {
        case this.const_rating_low: {
          displayText = this.const_rating_display_low;
          break;
        }
        case this.const_rating_medium: {
          displayText = this.const_rating_display_medium;
          break;
        }
        case this.const_rating_high: {
          displayText = this.const_rating_display_high;
          break;
        }
      }
      return displayText;
    },
    getRatingColor(rating, formType) {
      //console.log("rating, formtype: ", rating, formType);
      if (rating == null || rating == '') {
        return '';
      }
      let colorClass = '';
      switch (rating.toUpperCase()) {
        case '1': 
        case '2': 
          if (formType == this.$CONST_FORMTYPE_STAT99R) {
            colorClass = 'dashboard-background-color-green';
            break;
          } else {
            colorClass = '';
            break;
          }
        case this.const_rating_low: 
        case "LOW":
        case '1AE': 
        case '1HE': 
        case '2AE': 
        case '2HE': {
          colorClass = 'dashboard-background-color-green';
          break;
        }
        case '3': 
          if (formType == this.$CONST_FORMTYPE_STAT99R) {
            colorClass = 'dashboard-background-color-yellow';
            break;
          } else {
            colorClass = '';
            break;
          }
        case this.const_rating_medium: 
        case "MEDIUM": 
        case '3AE': 
        case '3HE': {
          colorClass = 'dashboard-background-color-yellow';
          break;
        }
        case '4A': 
        case '4B': 
          if (formType == this.$CONST_FORMTYPE_STAT99R) {
            colorClass = 'dashboard-background-color-red';
            break;
          } else {
            colorClass = '';
            break;
          }
        case this.const_rating_high: 
        case "HIGH": 
        case '4ALE': 
        case '4AAE': 
        case '4AHE': 
        case '4BLE': 
        case '4BAE': {
          colorClass = 'dashboard-background-color-red';
          break;
        }
      }
      return colorClass;
    },
    initFormCreationSelection() {
      if (this.formToCreate === this.$CONST_FORMTYPE_RNA) {
        this.selectedFormtypeForFormCreate = [];

        // added $CONST_FORMTYPE_CRNA to this.selectedFormtypeForFormCreate
        this.selectedFormtypeForFormCreate.push(this.$CONST_FORMTYPE_CRNA);

        // if it's IPVClient, add this.$CONST_FORMTYPE_SARA to this.selectedFormtypeForFormCreate
        if (this.IPVClient) {
          this.selectedFormtypeForFormCreate.push(this.$CONST_FORMTYPE_SARA);
        }
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
      // set formData
      const formData = {
        clientNumber: this.clientNum,
        linkedClientFormId: null
      };

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
    async formCloneAPI(formID, formType) {
      // set formData
      const formData = {
        clientNumber: this.clientNum,
        clientFormId: formID, 
        module: formType
      };

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
        let period = (this.currentPeriod === 'current');
        const [error, response] = await formSearch(this.clientNum, formType, period, this.mostRecent);
        if (error) {
          console.error(error);
        } else {
          this.key_rnalistSearchResult++;
          this.rnaList = response;
          console.log("RNAList search: ", response);
          this.rnaList = this.rnaList.filter(el => {
            el.status = this.$FORM_STATUS_INCOMPLETE;
            if (el.complete) {
              el.status = this.$FORM_STATUS_COMPLETE;
            }
            let updDateDisplay = (el.osuUpdateDate) ? el.osuUpdateDate : (el.completedDate) ? el.completedDate : el.createdDate;
            el.updatedDateDisplay = dateToCCCMDateformat(updDateDisplay);
            el.createdDate = dateToCCCMDateformat(el.createdDate);
            el.assessmentStatusDisplay = this.getAssessmentStatus(el.reassessment, el.module);
            
            // CRNA or SARA form, there should be only CRNA or SARA rating returned
            // CRNA_SARA form, there should be CRNA and/or SARA rating returned
            // SMO_Overall form, there should be SMO_Overall, CRNA and/or SARA rating returned
            // All other SMO form types, there should be only the rating for the specific form type
            if (el.ratings != null) {
              el.ratings.filter(rating => {
                if (rating.formType == this.$CONST_FORMTYPE_CRNA || rating.formType == this.$CONST_FORMTYPE_CMRP) {
                  el.crnaRating = rating.desc;
                  el.crnaRatingVal = rating.text;
                  // CBCCCM-1114: don't show supervisionRating for CMRP form
                  if (rating.formType == this.$CONST_FORMTYPE_CMRP) {
                    el.supervisionRating = '';
                  }
                } else if (rating.formType == this.$CONST_FORMTYPE_SARA) {
                  el.saraRating = rating.desc;
                  el.saraRatingVal = rating.text;
                } else {
                  el.smoRating = rating.desc;
                  el.smoRatingVal = rating.text;
                  // Don't show supervisionRating for SMO forms other than SMO_Overall
                  if (rating.formType != this.$CONST_FORMTYPE_SO_OVERALL) {
                    el.supervisionRating = '';
                  }
                } 
              });
            }
            return el;
          });
        }
      } finally {
        this.loading = false;
      }
    },
    buildPrintParam(formID) {
      if (formID != null) {
        formID = formID.toString();
      }
      let theForm = this.rnaList.filter(item => item.id == formID);
      
      const param = {
        csNumber: this.clientNum,
        formID: formID,
        CRNARating: theForm != null ? theForm[0].crnaRating : '',
        SARARating: theForm != null ? theForm[0].saraRating : ''
      }
      return btoa(JSON.stringify(param));
    },
    formView(formID) {
      let printParam = this.buildPrintParam(formID);
      this.$router.push({
        name: this.$ROUTER_NAME_CMPFORM,
        params: {
          formID: formID,
          csNumber: this.clientNum,
          param: printParam
        }
      });
    },
    async formClone(formID, formType) {
      this.formCloneAPI(formID, formType);
      this.formSearchAPI(this.$CONST_FORMTYPE_RNA);
    },
    formPrint(formID) {
      let printParam = this.buildPrintParam(formID);

      //Bring user to the print view in a new tab
      const route = this.$router.resolve({
          name: this.$ROUTER_NAME_PRINT,
          params: {
            param: printParam
          }
        });

      window.open(route.href, '_blank');
    },
    async handleFormCreateBtnClick() {
      this.dialog = false;
      // Create RNA-CMP
      if (this.formToCreate == this.$CONST_FORMTYPE_RNA) {
        // if contains SARA, create CRNA-SARA
        if (this.selectedFormtypeForFormCreate.includes(this.$CONST_FORMTYPE_SARA)) {
          this.createFormAPI(this.$CONST_FORMTYPE_SARA);
        } else {
          // if doesn't contain SARA, create CRNA
          this.createFormAPI(this.$CONST_FORMTYPE_CRNA);
        }
      } else {
        this.createFormAPI(this.formToCreate);
      }
    },
    formCreate(formType) {
      //console.log("Create form btn click");
      this.formToCreate = formType;
      let modal = document.getElementById(this.CONST_MODAL_ID_PREFIX + this.formToCreate);
      this.initFormCreationSelection();
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
    },
    showFormCreateButtons() {
      return this.mainStore.isAllowFormWrite();
    },
    showCMRPCreateButton() {
      return this.mainStore.loginUserGroups.includes(this.$USER_GROUP_ITRP) && this.mainStore.isShowCmrpForm();
    },
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore)
  }
}
</script>

<style scoped>
  a.disabled {
    opacity: 0.5;
    pointer-events: none;
  }
</style>
