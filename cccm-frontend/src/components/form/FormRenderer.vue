<template>
  <div data-app class="main rna-cmp-form">
    <!-- Form delete modal dialog section-->
    <div>
      <!-- Form delete modal dialog-->
      <v-btn
        :id="`id_modal_deleteForm_${formType}`"
        v-show=false
        @click.stop="deleteDialog = true"
      ></v-btn>
      <v-dialog
          v-model="deleteDialog"
          persistent
          max-width="550"
        >
        <v-card v-if="formType === $CONST_FORMTYPE_CRNA">
          <v-card-title class="text-h5">
            <span v-if="relatedClientFormId">
              Are you sure you want to delete?
            </span>
            <span v-else>
              Are you sure you want to delete this form?
            </span>
          </v-card-title>
          
          <v-card-text >
            <span v-if="relatedClientFormId">
              The CRNA-CMP and the SARA forms and all the information you have entered will be deleted. You will be directed to the client's RNA list.
            </span>
            <span v-else>
              This form and all the information you have entered will be deleted and you will be directed to the client's RNA list. 
            </span>
          </v-card-text>
          <v-card-actions>
            <v-btn
              @click="deleteDialog = false"
            >
            No, I don't want to delete
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
              color="#f81e41"
              dark
              @click="handleDeleteCRNAFormBtnClick"
            >
              <span v-if="relatedClientFormId">
                Yes, delete form(s)
              </span>
              <span v-else>
                Yes, delete this form
              </span>
            </v-btn>
          </v-card-actions>
        </v-card>
        <v-card v-else-if="formType === $CONST_FORMTYPE_SARA">
          <div class="col-sm-12 m-7">
            <v-card-title >
              Select what you want to delete:
            </v-card-title>
            <v-checkbox
              v-model="saraDeleteSelectedFormTypeValue"
              label="CRNA-CMP"
              value="CRNA"
            ></v-checkbox>
            <v-checkbox
              v-model="saraDeleteSelectedFormTypeValue"
              :readonly=true
              label="SARA"
              value="SARA"
            ></v-checkbox>
            <v-card-title>
            Are you sure you want to delete?
          </v-card-title>
          <v-card-text>
            The form(s) and all the information you have entered will be deleted and you will be directed to the client's RNA list. 
          </v-card-text>
          </div>
          
          <v-card-actions>
            <v-btn
              @click="deleteDialog = false"
            >
            No, I don't want to delete
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
              color="#f81e41"
              dark
              @click="handleDeleteSARAFormBtnClick"
            >
              Yes, delete form(s)
            </v-btn>
          </v-card-actions>
        </v-card>
        <v-card v-else>
          <div class="col-sm-12 m-7">
            <v-card-title>
              Are you sure you want to delete?
            </v-card-title>
            <v-card-text>
              The form(s) and all the information you have entered will be deleted and you will be directed to the client's RNA list. 
            </v-card-text>
          </div>
          <v-card-actions>
            <v-btn
              @click="deleteDialog = false"
            >
            No, I don't want to delete
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
              color="#f81e41"
              dark
              @click="handleDeleteSARAFormBtnClick"
            >
              Yes, delete form(s)
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>

    <div class="wrap">
      <div class="mainRow">
        <div class="columnMain L">
          <div class="menu-Sticky">
            <div id="id_infoPanel" class="menuR1">
              <v-alert :key="errorKey" v-if="errorOccurred" type="info" prominent dismissible>
                <h5>{{ errorTitle }}</h5>
                <span v-html="getErrorText"></span>
              </v-alert>
              <FormioFormInfo :key="formStaticInfoKey" :dataModel="formInfoData" @unlockForm="handleChangeFormToIncomplete" />
            </div>
            <div id="id_navPanel" :class="loading ? 'hide' :  loaded ? 'menuR2' : 'menuR2 mask'">
              <FormNavigation :key="componentKey" 
                :dataModel="data_formEntries" 
                :parentNavMoveToNext="parentNavMoveToNext"
                :parentNavJumpToPointed="parentNavJumpToPointed"
                @parentNavClicked="handleNavChildCallback"/>
            </div>
          </div>
          <v-progress-linear v-if="loading || !loaded" indeterminate height="30" color="primary">{{loadingMsg}}</v-progress-linear>
          
          <div id="div_scale" :key="componentKey" :class="loading ? 'hide' :  loaded ? 'mainContent' : 'mainContent mask'">
            <v-row>
              <div class="col-11">
                <FormDataEntry 
                  :csNumber="csNumber"
                  :formId="formId"
                  :dataModel="data_formEntries" 
                  :options="options"
                  :initData="formInitData"
                  :sendData="sendData"
                  @formDataCollected="validateAndCompleteForm"
                  :formType="formType" />   
          
                <FormDataRefreshSection v-if="pageRefreshSectionIndex != '' && displayDSection" 
                  :dataModel="dynamicSectionModel" 
                  :initData="formInitData"
                  :clientFormId="formId"
                  :csNumber="csNumber"
                  :options="options"/>

                <FormCaseplan v-if="isContainCasePlan && displayCasePlan" 
                  :dataModel="casePlanDataModel" 
                  :initData="formInitData"
                  :clientFormId="formId"
                  :csNumber="csNumber"
                  :options="options"/>

                <FormSummary v-if="isContainSummary && displaySummary" 
                  @viewSectionQuestion="navToSectionAndQuestion" 
                  :clientFormId="formId"
                  :csNumber="csNumber" />

                <FormioButtonGroupSubmit 
                  :saveBtnLabel="btnSaveContinueText"
                  :submitDone="submitDone"
                  :dataModel="submitBtnData" 
                  @saveContinueClicked="handleSaveContinue"
                  @cancelFormClicked="handleDeleteForm" />
              </div>
              <div class="scale-icon col-1">
                <font-awesome-icon v-if="!formExpanded" :icon="['fas', 'maximize']" title="Maximize form" @click="formExpandToggle"/>
                <font-awesome-icon v-else :icon="['fas', 'minimize']" title="Minimize form" @click="formExpandToggle"/>
              </div>
            </v-row>
          </div>
        </div>
        <div id="id_sidePanel" class="columnMain R">
          <div class="R-Sticky">
            <section class="crna-right-sticky-panel">
              <div class="crna-right-panel-button-container">
                <!--Save Close button group-->
                <FormioButtonGroupSide v-if="!loading" 
                  :dataModel="sideBtnData" 
                  @saveCloseClicked="handleSaveClose" 
                  @printFormClicked="handlePrintForm" />
              </div>
              <div class="crna-right-panel-details">
                <FormioSidePanel :key="formStaticInfoKey" 
                  :dataModel="clientData" 
                  :clientFormId="formId"
                  :options="options" />
              </div>
            </section>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">

import { Form } from '@formio/vue';
import { getClientFormMetaData, getFormioTemplate, loadFormData, clientProfileSearch, completeForm, deleteForm, unlockForm } from "@/components/form.api";
import FormDataEntry from "@/components/form/formSections/FormDataEntry.vue";
import FormNavigation from "@/components/form/formSections/FormNavigation.vue";
import FormioSidePanel from "@/components/common/FormioSidePanel.vue";
import FormioFormInfo from "@/components/common/FormioFormInfo.vue";
import FormioButtonGroupSide from "@/components/common/FormioButtonGroupSide.vue";
import FormioButtonGroupSubmit from "@/components/common/FormioButtonGroupSubmit.vue";
import FormSummary from '@/components/form/formSections/FormSummary.vue';
import FormCaseplan from '@/components/form/formSections/FormCasePlan.vue';
import FormDataRefreshSection from '@/components/form/formSections/FormDataRefreshSection.vue';
import {useStore} from "@/stores/store";
import { useAutosaveStore } from "@/stores/autoSaveStore";
import {mapStores} from 'pinia';
import { dateToCCCMDateformat } from '../dateUtils';

export default {
  name: 'FormRenderer',
  props: {
    formType: '',
    formId: 0,
    csNumber: '',
    CRNARating: '',
    SARARating: '',
    relatedClientFormId: 0,
    readonly: false,
    locked: false,
    createdByIdir: '',
    canPrint: true,
  },
  components: {
    Form,
    FormDataEntry,
    FormNavigation,
    FormioSidePanel,
    FormioFormInfo,
    FormioButtonGroupSide,
    FormioButtonGroupSubmit,
    FormSummary,
    FormCaseplan,
    FormDataRefreshSection
  },
  data() {
    return {
      CONST_KEY_eventTriggeredAutoCalc: "eventTriggeredAutoCalc",
      CONST_INTERVENTION_CHECKBOX_SUFIX:"_intervention_checkbox",
      CONST_LABEL_CASEPLAN: "CASE PLAN",
      CONST_LABEL_SUMMARY: "SUMMARY",
      theFormConfig: {},
      loadingMsg: "Loading form...",
      loading: false,
      loaded: false,
      displaySummary: false,
      parentNavMoveToNext: 1,
      parentNavJumpToPointed: '',
      parentNavJumpToPointed_sufix: 0,
      totalNumParentNav: 1,
      parentNavCurLocation: '0',
      btnSaveContinueText: "Save and Continue",
      data_formEntries: {},
      clientData: {},
      formInfoData: {},
      formInitData: {},
      dataMap: {},
      componentKey: 0,
      formStaticInfoKey: 0,
      loadingMsgCasePlanIntervention: "Loading intervention data...",
      displayCasePlan: false,
      casePlanDataModel: {"display": "form", "components": []},
      errorOccurred: false,
      errorText: '',
      errorTitle: '',
      deleteDialog: false,
      saraDeleteSelectedFormTypeValue: ["SARA"],
      options: {},
      submitBtnData: {},
      sideBtnData: {"data": {}},
      isContainCasePlan: false,
      isContainSummary: false,
      errorKey: 0,
      pageRefreshSectionIndex: '',
      sendData: 0,
      dynamicSectionModel: {"display": "form", "components": []},
      displayDSection: false,
      showFormExpandedIcon: false,
      formExpanded: false,
      key_formExpandIcon: 0,
      submitDone: 0
    }
  },
  mounted(){
    if (this.readonly) {
      this.options.readOnly = this.readonly;
    } else {
      this.options = {};
    }

    let configuredFormInfo = this.$FORM_INFO.filter( item => item.formType === this.formType );
    if (configuredFormInfo != null && configuredFormInfo[0] != null) {
      this.theFormConfig = configuredFormInfo[0];
      this.pageRefreshSectionIndex = this.theFormConfig.dataRefreshSectionIndex;
    } 
    
    // set sideBtnData
    this.sideBtnData.data.showPrintBtn = this.canPrint;
    // load the page
    this.loadPage();
  },
  methods: {
    doEventTriggerAutoCalc() {
      // push csNumber and formId to autoSaveStore
      this.autosaveStore.setClientNumber(this.csNumber);
      this.autosaveStore.setFormId(this.formId);

      // clear autoSaveDataCandidate
      this.autosaveStore.autoSaveDataCandidate = {};
      this.autosaveStore.autoSaveData = {};

      // set true to CONST_KEY_eventTriggeredAutoCalc field
      this.autosaveStore.autoSaveDataCandidate[this.CONST_KEY_eventTriggeredAutoCalc] = true;

      // Trigger a save
      this.autosaveStore.autoSave();
    },
    pageLoadHelper() {
      this.getClientAndFormMeta(this.readonly);
      this.getFormioTemplate();
      setTimeout(() => {
        this.loaded = true;
        this.navToSectionAndQuestion(0, 0);
      }, 1000);
    },
    loadPage() {
      // check to see if need to trigger an autoCalc before loading the form,
      // only do it if the form is not locked
      if (this.mainStore.isEventTriggerAutoCalcAllowed() && this.theFormConfig.autoCalcOnFormload && !this.locked) {
        this.doEventTriggerAutoCalc();

        //Redirect User to the newly created form
        if (this.autosaveStore.isSavingInProgress()) { 
          // Delay loading summary view data
          console.log("Event triggered autoCalc done, continue lazy loading the form ");
          setTimeout(() => {
            this.pageLoadHelper();
          }, 1000)
        } else {
          console.log("Event triggered autoCalc done, continue prompt loading the form ");
          this.pageLoadHelper();
        }
      } else {
        this.pageLoadHelper();
      }
    },
    formExpandToggle() {
      let scaleDiv = document.getElementById("div_scale");
      let footerDiv = document.getElementById("id_footer");
      let infoDiv = document.getElementById("id_infoPanel");
      let navDiv = document.getElementById("id_navPanel");
      let sideDiv = document.getElementById("id_sidePanel");
      let menuDiv = document.getElementById("div_menu");
      if (scaleDiv != null) {
        if (this.formExpanded) {
          this.formExpanded = false;
          scaleDiv.className = "mainContent";
          footerDiv.setAttribute('style', 'display:block');
          infoDiv.setAttribute('style', 'display:block');
          navDiv.setAttribute('style', 'display:block');
          sideDiv.setAttribute('style', 'display:block');
          menuDiv.setAttribute('style', 'display:block');
        } else {
          this.formExpanded = true;
          scaleDiv.className = "scales scaleOn";
          footerDiv.setAttribute('style', 'display:none');
          infoDiv.setAttribute('style', 'display:none');
          navDiv.setAttribute('style', 'display:none');
          sideDiv.setAttribute('style', 'display:none');
          menuDiv.setAttribute('style', 'display:none');
        }
      }
    },
    handleChangeFormToIncomplete() {
      this.unSetCompleteFlag();
    },
    async unSetCompleteFlag() {
      let unlockFormData = {};
      unlockFormData.clientFormId = Number(this.formId);
      unlockFormData.clientNumber = this.csNumber;
      unlockFormData.module = this.formType;
      unlockFormData.linkedClientFormId = null;

      // call api to unset the complete flag
      const [error, response] = await unlockForm(unlockFormData);
      if (error) {
        console.error("Failed unset complete status", error);
      } else {
        this.options = {};
        this.getClientAndFormMeta(false);
      }
    },
    isShowDeleteButton(completeDate) {
      // Show delete btn, whent he user is sys admin or is the owner and form is incomplete and not locked
      if (this.mainStore.loginUserGroups.includes(this.$USER_GROUP_ADMIN) ||
       (this.createdByIdir != null && 
        this.createdByIdir.toUpperCase() == this.$keycloak.tokenParsed.preferred_username.toUpperCase() &&
        completeDate == null && !this.locked)) {
        return true;
      }
      
      return false;
    },
    isShowEditButton(completeDate) {
      // When form is locked, show edit button if the form is completed and user is admin
      if (this.locked) {
        if (completeDate != null && this.mainStore.loginUserGroups.includes(this.$USER_GROUP_ADMIN)) {
          return true;
        }
        return false;
      } else {
        // Edge case: if user doesn't have idirId, output a warning, and hide the edit button
        if (this.createdByIdir == null) {
          console.warn("The login user doesn't have idirId, hide the edit button.");
          return false;
        }
        
        let groupCheckedAgainst = this.$USER_GROUP_ADMIN;
        if (this.formType === this.$CONST_FORMTYPE_CMRP) {
          groupCheckedAgainst = this.$USER_GROUP_ITRP;
        }
        console.log("groupCheckedAgainst: ", groupCheckedAgainst);
        // Form is unlocked but completed, show edit button when the form is completed and the user is an admin or the form owner
        if (completeDate != null && 
            (this.mainStore.loginUserGroups.includes(groupCheckedAgainst) ||
             this.createdByIdir.toUpperCase() == this.$keycloak.tokenParsed.preferred_username.toUpperCase())) {
          return true; 
        } 
        return false;
      }
    },
    async getClientAndFormMeta(readonly) {
      // ClientForm Meta data search.
      const [error, clientFormMeta] = await getClientFormMetaData(this.csNumber, this.formId);
      if (error) {
        console.error("Failed getting client form metadata: ", error);
      } else {
        console.log("clientFormMeta: ", clientFormMeta);
        this.formInfoData.data = clientFormMeta;
        this.formInfoData.data.status = this.formInfoData.data.completedDate == null ? this.$FORM_STATUS_INCOMPLETE : this.$FORM_STATUS_COMPLETE;
        this.formInfoData.data.showEditBtn = this.isShowEditButton(clientFormMeta.completedDate);
        this.formInfoData.data.clientFormType = this.formInfoData.data.clientFormType == null ? '' : this.formInfoData.data.clientFormType ? this.$FORM_TYPE_REASSESSMENT : this.$FORM_TYPE_INITIAL;
        
        // set the form lock 
        this.formInfoData.data.locked = this.locked;
        
        // set the form title
        if (this.theFormConfig != null) {
          this.formInfoData.data.formTitle = this.theFormConfig.formTitle;
          this.formInfoData.data.assessmentStatusRequired = this.theFormConfig.assessmentStatusRequired;
          this.formInfoData.data.formTypeLabel = this.theFormConfig.formTypeLabel;
        }

        // convert dates
        this.formInfoData.data.createdDate = dateToCCCMDateformat(this.formInfoData.data.createdDate);
        this.formInfoData.data.osuUpdatedDate = dateToCCCMDateformat(this.formInfoData.data.osuUpdatedDate);
        if (this.formInfoData.clientData != null) {
          this.formInfoData.data.clientData.birthDate = dateToCCCMDateformat(this.formInfoData.data.clientData.birthDate);
          this.formInfoData.data.clientData.dueDate = dateToCCCMDateformat(this.formInfoData.data.clientData.dueDate);
          if (this.formInfoData.data.clientData.orderInformation) {
            this.formInfoData.data.clientData.orderInformation.dueDate = dateToCCCMDateformat(this.formInfoData.data.clientData.orderInformation.dueDate);
            this.formInfoData.data.clientData.orderInformation.effectiveDate = dateToCCCMDateformat(this.formInfoData.data.clientData.orderInformation.effectiveDate);
          }
          if (this.formInfoData.data.clientData.generalInformation) {
            this.formInfoData.data.clientData.generalInformation.dischargeDate = dateToCCCMDateformat(this.formInfoData.data.clientData.generalInformation.dischargeDate);
          }
          if (this.formInfoData.data.clientData.locationInformation) {
            this.formInfoData.data.clientData.locationInformation.warrantExpiryDate = dateToCCCMDateformat(this.formInfoData.data.clientData.locationInformation.warrantExpiryDate);
            this.formInfoData.data.clientData.locationInformation.nextCourtDate = dateToCCCMDateformat(this.formInfoData.data.clientData.locationInformation.nextCourtDate);
          }
        }        
        
        // set submitBtnData
        this.submitBtnData = {"data": {}};
        this.submitBtnData.data.showDeleteBtn = this.isShowDeleteButton(clientFormMeta.completedDate);
        this.submitBtnData.data.readonly = readonly;
        
        // Client profile search.
        const [error1, response] = await clientProfileSearch(this.csNumber);
        if (error1) {
          console.error("Failed doing client profile search: ", error1);
        } else {
          console.log("clientData: ", response);
          this.clientData.data = response;
          this.formInfoData.data.clientData = response;
          
          // convert dates
          this.clientData.data.birthDate = dateToCCCMDateformat(this.clientData.data.birthDate);
          this.clientData.data.dueDate = dateToCCCMDateformat(this.clientData.data.dueDate);
          this.clientData.data.completedDate = dateToCCCMDateformat(this.clientData.data.completedDate);
          if (this.clientData.data.orderInformation) {
            this.clientData.data.orderInformation.dueDate = dateToCCCMDateformat(this.clientData.data.orderInformation.dueDate);
            this.clientData.data.orderInformation.effectiveDate = dateToCCCMDateformat(this.clientData.data.orderInformation.effectiveDate);
            this.clientData.data.orderInformation.expiryDate = dateToCCCMDateformat(this.clientData.data.orderInformation.expiryDate);
          }
          if (this.clientData.data.generalInformation) {
            this.clientData.data.generalInformation.dischargeDate = dateToCCCMDateformat(this.clientData.data.generalInformation.dischargeDate);
            this.clientData.data.generalInformation.paroleDate = dateToCCCMDateformat(this.clientData.data.generalInformation.paroleDate);
          }
          if (this.clientData.data.locationInformation) {
            this.clientData.data.locationInformation.warrantExpiryDate = dateToCCCMDateformat(this.clientData.data.locationInformation.warrantExpiryDate);
            this.clientData.data.locationInformation.nextCourtDate = dateToCCCMDateformat(this.clientData.data.locationInformation.nextCourtDate);
          }
          // Set sources contacted
          this.clientData.data.sourcesContacted = {
            showSCPanel: this.theFormConfig ? this.theFormConfig.showSourcesContacted : true,
            hideSCInput: true,
            title: this.formType === this.$CONST_FORMTYPE_CMRP ? "Community Supports" : "Sources Contacted",
            addButtonLabel: this.formType === this.$CONST_FORMTYPE_CMRP ? "Add Support" : "Add Source",
          };
          this.clientData.data.input_key_sourceContacted = this.formInfoData.data.input_key_sourceContacted;

          this.clientData.data.formType = this.formType
          //console.log("clientData: ", this.clientData);
        }
        this.componentKey++;
        this.formStaticInfoKey++;
      };
      
    },
    async getFormioTemplate() {
      // Load formio template
      this.loading = true;
      this.loadingMsg = "Loading form...";
      const [error, response] = await getFormioTemplate(this.csNumber, this.formId);
      if (error) {
        console.error(`Failed getting formio template for formID - ${this.formId}, error: `, error);
      } else {
        //console.log("template: ", response);
        this.loadingMsg = "Setup navigation...";
        this.data_formEntries = response;
        // force FormNavigation to refresh.
        this.componentKey++;

        this.isContainCasePlan = this.private_isContainASection(this.CONST_LABEL_CASEPLAN, response);
        this.isContainSummary = this.private_isContainASection(this.CONST_LABEL_SUMMARY, response);

        //console.log("this.isContainCasePlan, this.isContainSummary: ", this.isContainCasePlan, this.isContainSummary);
        this.totalNumParentNav = response == null || response.components == null ? 0 : response.components.length;
        
        if (this.totalNumParentNav >= this.isContainCasePlan ? 2 : 1) {
          // setup CasePlan 
          if (this.isContainCasePlan) {
            const clone = JSON.parse(JSON.stringify(this.data_formEntries.components[this.totalNumParentNav - 2].components));
            this.casePlanDataModel.components = clone;
            this.data_formEntries.components[this.totalNumParentNav - 2].components = [];
            //console.log("this.data_formEntries:", this.data_formEntries);
          }
          if (this.pageRefreshSectionIndex != '') {
            const clone = JSON.parse(JSON.stringify(this.data_formEntries.components[this.pageRefreshSectionIndex].components));
            this.dynamicSectionModel.components = clone;
            this.data_formEntries.components[this.pageRefreshSectionIndex].components = [];
          }
        }

        // Load form data
        this.loadingMsg = "Loading client form data...";
        const [error, clientFormData] = await loadFormData(this.csNumber, this.formId);
        //console.log("client form data: ", clientFormData)
        if (error) {
          console.error(error);
        } else {
          this.formInitData = clientFormData;
          this.formInitData.data.clientFormTypeDesc = this.formInfoData.data.clientFormType == '' ? 'Assessment' : (this.formInfoData.data.clientFormType == this.$FORM_TYPE_INITIAL) ? "Assessment": this.$FORM_TYPE_REASSESSMENT;
          // setup autoSaveStore data for the form saving by doing the following:
          // 1. emtpy the autoSaveDataCandidate
          // 2. initialize the autoSaveData to be the initial form data, which is used to avoid unnessisary save upon form load
          this.autosaveStore.autoSaveData = JSON.parse(JSON.stringify(this.formInitData.data));
          this.autosaveStore.autoSaveData.input_key_sourceContacted = this.formInitData.data.Sources_Contacted;
          this.autosaveStore.autoSaveDataCandidate = {};
          console.log("form loaded, this.autosaveStore.autoSaveData: this.autosaveStore.autoSaveDataCandidate: ", this.autosaveStore.autoSaveData, this.autosaveStore.autoSaveDataCandidate);
        }
      }
      this.loading = false;
    },
    private_isContainASection(sectionLabel, template) {
      if (template == null || template.components == null) {
        return false;
      }
      for(let el of template.components) {
        if (el.label.toUpperCase() == sectionLabel) {
          return true;
        }
      }
      return false;
    },
    navToSectionAndQuestion(section: number, question: number) {
      // console.log("Nav to :", section, question);
      // update the displaySummary flag to hide summary panel
      this.displaySummary = false;
      this.displayCasePlan = false;
      this.displayDSection = false;

      // navigate to the pointed section, 
      // parentNavJumpToPointed_sufix is used to ensure the value of parentNavJumpToPointed is different each time,
      // so it reactively refreshes the FormNavigation component
      if (this.parentNavJumpToPointed_sufix == '0') {
        this.parentNavJumpToPointed_sufix = "1";
      } else {
        this.parentNavJumpToPointed_sufix = "0";
      }
      this.parentNavJumpToPointed = section + "Q" + question + "_" + this.parentNavJumpToPointed_sufix;
    },
    handlePrintForm(evt) {
      let param = {};
      param.csNumber = this.csNumber;
      param.formID = this.formId;
      param.CRNARating = this.CRNARating;
      param.SARARating = this.SARARating;
      //console.log("Set PO param: ", param);
      let base64EncodeParam = btoa(JSON.stringify(param));

      //Bring user to the print view in a new tab
      const route = this.$router.resolve({
          name: this.$ROUTER_NAME_PRINT,
          params: {
            param: base64EncodeParam
          }
        });

      window.open(route.href, '_blank');
    },
    redirectOnFormClose(){
      this.$router.push({
        name: this.$ROUTER_NAME_CLIENTRECORD,
        params: {
          clientNum: this.csNumber,
          tabIndex: 'tab-rl'
        }
      });
    },
    handleSaveClose() {
      //console.log("handleSaveClose");
      if (this.mainStore.isEventTriggerAutoCalcAllowed() && this.theFormConfig.autoCalcOnFormComplete && !this.locked) {
        this.doEventTriggerAutoCalc();
        if (this.autosaveStore.isSavingInProgress()) { 
          console.log("Event triggered autoCalc done, continue lazy closing form ");
          setTimeout(() => {
            this.redirectOnFormClose();
          }, 1000);
        } else {
          console.log("Event triggered autoCalc done, continue prompt closing form ");
          this.redirectOnFormClose();
        }
      } else {
        //Redirect User back to clientRecord.RNAList
        this.redirectOnFormClose();
      }
    },
    handleSaveContinue(continueToNextSection) {
      //console.log("handleSaveContinue, continueToNextSection: ", continueToNextSection);
      // if not reaching the last section, increment this.parentNavCurLocation to navigate to the next section
      if (continueToNextSection && this.parentNavCurLocation < this.totalNumParentNav - 1) {
        //show case plan
        if (this.parentNavCurLocation == this.totalNumParentNav - 2) {
          this.displayCasePlan = true;
        } 
        if (this.parentNavCurLocation == this.pageRefreshSectionIndex) {
          this.displayDSection = true;
        }
        this.parentNavMoveToNext++;
      }

      // If reaching the last section, time to validate the form and complete the form
      if (continueToNextSection && this.parentNavCurLocation == this.totalNumParentNav - 1) {
        // Notify child components (dataEntry (section level answers) to send their data for validation
        this.sendData++;
      }
    },
    handleDeleteForm() {
      //console.log("Delete Form btn clicked");
      this.showDeleteDialog();
    },
    handleNavChildCallback(parentNavCurLocationFromChild) {
      this.parentNavCurLocation = parentNavCurLocationFromChild;
      this.displaySummary = false;
      this.displayCasePlan = false;
      this.displayDSection = false;
      
      // User clicked 'Case plan' section from the navigation panel
      if (this.parentNavCurLocation == this.totalNumParentNav - 2) {
        this.displayCasePlan = true;
      }

      // User clicked on configured dynamic section from the navigation panel
      if (this.parentNavCurLocation == this.pageRefreshSectionIndex) {
        this.displayDSection = true;
      }

      // User clicked 'Summary' section from the navigation panel
      if (this.parentNavCurLocation == this.totalNumParentNav - 1) {
        this.displaySummary = true;
        this.btnSaveContinueText = this.$BUTTON_TEXT_SUBMIT; 
      } else {
        this.btnSaveContinueText = this.$BUTTON_TEXT_SAVE_CONTINUE; 
      }
    },
    formatValidationPayload(formData) {
      var result = [];
      var keys = Object.keys(formData);
      // extract intervention checkbox value from the formData 
      Object.keys(formData).filter((key) => key.endsWith(this.CONST_INTERVENTION_CHECKBOX_SUFIX) && formData[key])
                           .forEach((key) => result.push({"key": key.substring(0, key.indexOf(this.CONST_INTERVENTION_CHECKBOX_SUFIX))}))
      return result;
    },
    async completeFormAPI(completeFormData) {
      const [error, completResult] = await completeForm(completeFormData);
      if (error) {
        console.error("Failed completing a form instance", error);
        if (this.formExpanded) {
          console.log("form expanded, unexpand it, and show the error");
          this.formExpandToggle();
        }

        this.errorOccurred = true;
        this.errorTitle = error.response.data.errorMessage;
        this.errorText = error.response.data.validationResult == null ? null : error.response.data.validationResult.errors;
        this.errorKey++;
        this.submitDone++;
      } else {
        console.log("Successfully completed the form: ", this.formId);
        this.submitDone++;
        //Redirect User back to clientRecord.RNAList
        this.$router.push({
          name: this.$ROUTER_NAME_CLIENTRECORD,
          params: {
            clientNum: this.csNumber,
            tabIndex: 'tab-rl'
          }
        });
      }
    },
    async completeFormWrapper(completeFormData) {
      if (this.mainStore.isEventTriggerAutoCalcAllowed() && this.theFormConfig.autoCalcOnFormComplete && !this.locked) {
        this.doEventTriggerAutoCalc();
        
        //Redirect User to the newly created form
        if (this.autosaveStore.isSavingInProgress()) { 
          // Delay loading summary view data
          console.log("Event triggered autoCalc done, continue lazy completing form ");
          setTimeout(() => {
            this.completeFormAPI(completeFormData);
          }, 1000)
        } else {
          console.log("Event triggered autoCalc done, continue prompt completing the form ");
          this.completeFormAPI(completeFormData);
        }
      } else {
        this.completeFormAPI(completeFormData);
      }
    },
    async validateAndCompleteForm(formData) {
      if (formData != null) {
        // build completeFormData
        let completeFormData = {};
        completeFormData.clientFormId = Number(this.formId);
        completeFormData.clientNumber = this.csNumber;
        completeFormData.module = this.formType;
        completeFormData.linkedClientFormId = this.relatedClientFormId;
        completeFormData.interventionCheckboxChecked = this.formatValidationPayload(formData);
        //console.log("completeFormData: ", completeFormData);
        this.completeFormWrapper(completeFormData);
      }
    },
    showDeleteDialog() {
      //console.log("show delete modal, formType", this.formType);
      let modal = document.getElementById("id_modal_deleteForm_" + this.formType);
      if (modal != null) {
        modal.click();
      }
    },
    async formDeleteHelper(fullDelete) {
      // delete the form instance
      let redirect = false;
      const [error, response] = await deleteForm(this.formId, this.csNumber);
      if (error) {
        console.error("Failed deleting the form instance: ", error);
      } else {
        // delete the linked form instance
        if (fullDelete && this.relatedClientFormId) {
          //console.log("Delete linked form instance");
          const [error1, response1] = await deleteForm(this.relatedClientFormId, this.csNumber);
          if (error1) {
            console.error("Failed deleting the linked form instance: ", error1);
          } else {
            redirect = true;
          }
        } else {
          redirect = true;
        }

        if (redirect) {
          //Redirect User back to clientRecord.RNAList
          this.$router.push({
            name: this.$ROUTER_NAME_CLIENTRECORD,
            params: {
              clientNum: this.csNumber,
              tabIndex: 'tab-rl'
            }
          });
        }
      }
    },
    async handleDeleteCRNAFormBtnClick() {
      //console.log("Delete a CRNA form: ", this.formId, this.csNumber, this.relatedClientFormId);
      this.deleteDialog = false;
      this.formDeleteHelper(true);
    },
    async handleDeleteSARAFormBtnClick() {
      this.deleteDialog = false;
      
      let fullDelete = false;
      if (this.saraDeleteSelectedFormTypeValue != null) {
        for (let i = 0; i < this.saraDeleteSelectedFormTypeValue.length; i++) {
          if (this.saraDeleteSelectedFormTypeValue[i] == this.$CONST_FORMTYPE_CRNA) {
            fullDelete = true;
            break;
          }
        }
      }
      this.formDeleteHelper(fullDelete);
    }
  },
  computed: {
    getErrorText() {
      let error = '';
      if (this.errorText != null && this.errorText.length > 0) {
        for (let i = 0; i < this.errorText.length; i++) {
          error += this.errorText[i].message + "<br>";
        }
      }
      return error;
    },
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore, useAutosaveStore)
  }
}
</script>

<style>
.sectionTitleClass {
  font-size: 35px;
}

.subSectionTitleClass {
  font-size: 20px;
  font-weight: bold;
  -webkit-text-decoration-color: rgb(255, 208, 0); 
  text-decoration-color: rgb(255, 208, 0);
}

.subSectionTitleClass::after {
    content: "";
    height: 0px;
    width: 50px;
    display: block;
    border-bottom: 6px solid rgb(255, 208, 0);
    margin-bottom: 25px;
}

.subSectionChildTitleClass {
  font-size: 15px;
  font-weight: bold;
  
}

/* 
 * Need to add the following definition, 
 * vuetify/dist/vuetify.min.css override it's original definition
 */
ol {
    display: block;
    margin-block-start: 1em;
    margin-block-end: 1em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
    padding-inline-start: 40px;
}

ul {
    display: block;
    list-style-type: disc;
    margin-block-start: 1em;
    margin-block-end: 1em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
    padding-inline-start: 40px;
}

li {
    display: list-item;
    text-align: -webkit-match-parent;
}

.hide {
	display: none;
}
.show {
	display: block;
}

.red {
  font-size: 15px;
  background-color: rgb(255, 0, 0);
  color: hsl(0, 0%, 1%)
}

.yellow {
  font-size: 15px;
  background-color: rgb(236, 216, 103);
  color: hsl(0, 0%, 1%);
  line-height: 1.6;
}

.green {
  font-size: 15px;
  background-color: rgb(103, 236, 147);
  color: hsl(0, 0%, 1%);
  line-height: 1.6;
}
</style>