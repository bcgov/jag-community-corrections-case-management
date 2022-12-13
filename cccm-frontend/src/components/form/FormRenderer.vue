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
              The CRNA-CMP and the SARA-CMP forms and all the information you have entered will be deleted. You will be directed to the client's RNA list.
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
        <v-card v-if="formType === $CONST_FORMTYPE_SARA">
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
              label="SARA-CMP"
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
            <div class="menuR1">
              <v-alert v-if="errorOccurred" type="info" prominent dismissible>
                <h5>{{ errorTitle }}</h5>
                <span v-html="getErrorText"></span>
              </v-alert>

              <FormioFormInfo :key="formStaticInfoKey" :dataModel="formInfoData" @unlockForm="handleChangeFormToIncomplete" />
            </div>
            <div class="menuR2" v-if="!loading">
              <FormNavigation :key="componentKey" 
                :dataModel="data_formEntries" 
                :parentNavMoveToNext="parentNavMoveToNext"
                :parentNavJumpToPointed="parentNavJumpToPointed"
                @parentNavClicked="handleNavChildCallback"/>
            </div>
          </div>
          <v-progress-linear v-if="loading" indeterminate height="30" color="primary">{{loadingMsg}}</v-progress-linear>
          
          <div :key="componentKey" :class="loading ? 'hide' : 'mainContent'">
            <FormDataEntry 
              :csNumber="csNumber"
              :formId="formId"
              :dataModel="data_formEntries" 
              :options="options"
              :initData="formInitData" />   
      
            <FormCaseplan v-if="isContainCasePlan && displayCasePlan" 
              :dataModel="casePlanDataModel" 
              :initData="formInitData"
              :clientFormId="formId"
              :csNumber="csNumber"
              :options="options"/>

            <FormSummary v-if="isContainSummary && displaySummary" 
              @viewSectionQuestion="navToSectionAndQuestion" 
              :clientFormId="formId"
              :csNumber="csNumber" 
              :printRequested="printRequested"
              @cancelPrintFlag="handleCancelPrintFlag"/>

            <FormioButtonGroupSubmit 
              :saveBtnLabel="btnSaveContinueText"
              :dataModel="submitBtnData" 
              @saveContinueClicked="handleSaveContinue"
              @cancelFormClicked="handleDeleteForm" />
          </div>
        </div>
        <div class="columnMain R">
          <div class="R-Sticky">
            <section class="crna-right-sticky-panel">
              <div class="crna-right-panel-button-container">
                <!--Save Close button group-->
                <FormioButtonGroupSide v-if="!loading" 
                  @saveCloseClicked="handleSaveClose" 
                  @printFormClicked="handlePrintForm" />
              </div>
              <div class="crna-right-panel-details">
                <FormioSidePanel :key="formStaticInfoKey" 
                  :dataModel="clientData" 
                  :clientFormId="formId"
                  :options="options"
                  @sourcesContactedUpdated="handleSourcesContactedUpdated"/>
              </div>
            </section>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">

import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import { getClientFormMetaData, getFormioTemplate, loadFormData, clientProfileSearch, completeForm, deleteForm, unlockForm } from "@/components/form.api";
import FormDataEntry from "@/components/form/formSections/FormDataEntry.vue";
import FormNavigation from "@/components/form/formSections/FormNavigation.vue";
import FormioSidePanel from "@/components/common/FormioSidePanel.vue";
import FormioFormInfo from "@/components/common/FormioFormInfo.vue";
import FormioButtonGroupSide from "@/components/common/FormioButtonGroupSide.vue";
import FormioButtonGroupSubmit from "@/components/common/FormioButtonGroupSubmit.vue";
import FormSummary from '@/components/form/formSections/FormSummary.vue';
import FormCaseplan from '@/components/form/formSections/FormCasePlan.vue';
import {useStore} from "@/stores/store";
import {mapStores} from 'pinia';

export default {
  name: 'FormRenderer',
  props: {
    formType: '',
    formId: 0,
    csNumber: '',
    relatedClientFormId: 0,
    readonly: false,
    locked: false,
    printParam: false
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
    FormCaseplan
  },
  data() {
    return {
      CONST_LABEL_CASEPLAN: "CASE PLAN",
      CONST_LABEL_SUMMARY: "SUMMARY",
      loadingMsg: "Loading form...",
      loading: false,
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
      printRequested: false,
      submitBtnData: {},
      isContainCasePlan: false,
      isContainSummary: false
    }
  },
  mounted(){
    if (this.readonly) {
      this.options.readOnly = this.readonly;
    } else {
      this.options = null;
    }
    //console.log("form renderer mounted: ", this.readonly, this.options, this.formType, this.formId , this.relatedClientFormId, this.csNumber);
    this.getClientAndFormMeta();
    this.getFormioTemplate();
    
    if (this.printParam) {
      setTimeout(() => {
        this.printRequested = true;
        this.navToSectionAndQuestion(this.totalNumParentNav - 1, 1);
      }, 5000);
    }
  },
  methods: {
    handleCancelPrintFlag() {
      //console.log("cancel print requested");
      this.printRequested = false;
    },
    handleSourcesContactedUpdated(sourcesContacted) {
      this.formInfoData.data.input_key_sourceContacted = sourcesContacted;
      this.formStaticInfoKey++;
    },
    handleChangeFormToIncomplete() {
      this.options = null;
      this.formInfoData.data.showEditBtn = false;
      this.formInfoData.data.status = this.$FORM_STATUS_INCOMPLETE;
      this.submitBtnData.data.readonly = false;
      this.componentKey++;
      this.formStaticInfoKey++;

      // call api to unset the complete flag
      this.unSetCompleteFlag();
    },
    async unSetCompleteFlag() {
      let unlockFormData = {};
      unlockFormData.clientFormId = Number(this.formId);
      unlockFormData.clientNumber = this.csNumber;
      unlockFormData.linkedClientFormId = null;

      const [error, response] = await unlockForm(unlockFormData);
      if (error) {
        console.error("Failed unset complete status", error);
      } 
    },
    isShowDeleteButton(createdBy) {
      // Show delete btn is login user is sys admin or login user is the form owner
      if (this.mainStore.loginUserGroup == this.$USER_GROUP_ADMIN ||
          createdBy == Vue.$keycloak.tokenParsed.preferred_username) {
        return true;
      }
      return false;
    },
    isShowEditButton(createdBy, completeDate) {
      // When form is locked, hide edit button 
      if (this.locked) {
        if (completeDate != null) {
          return true;
        }
        return false;
      } else {
        // Show edit button when:
        // 1. The user is an admin
        // 2. The user is the one who created the form
        let showEditBtn = false;
        if (createdBy != Vue.$keycloak.tokenParsed.preferred_username) {
          showEditBtn = false;
          if (this.mainStore.loginUserGroup == this.$USER_GROUP_ADMIN &&
            completeDate != null) {
            showEditBtn = true;
          } 
        } else {
          showEditBtn = completeDate != null;
        }
        return showEditBtn;
      }
    },
    async getClientAndFormMeta() {
      // ClientForm Meta data search.
      const [error, clientFormMeta] = await getClientFormMetaData(this.csNumber, this.formId);
      if (error) {
        console.error("Failed getting client form metadata: ", error);
      } else {
        //console.log("clientFormMeta: ", clientFormMeta);
        this.formInfoData.data = clientFormMeta;
        this.formInfoData.data.status = this.formInfoData.data.completedDate == null ? this.$FORM_STATUS_INCOMPLETE : this.$FORM_STATUS_COMPLETE;
        this.formInfoData.data.showEditBtn = this.isShowEditButton(clientFormMeta.createdBy, clientFormMeta.completedDate);
        this.formInfoData.data.clientFormType = (this.formInfoData.data.clientFormType) ? this.$FORM_TYPE_REASSESSMENT : this.$FORM_TYPE_INITIAL;

        // set the form lock 
        this.formInfoData.data.locked = this.locked;
        
        // set the form title
        let theForm = this.$FORM_INFO.filter( item => item.formType === this.formType );
        //console.log('form info const:', this.formType, this.$FORM_INFO, theForm);

        this.formInfoData.data.formTitle = theForm[0].formTitle;
        this.formInfoData.data.formTypeLabel = theForm[0].formTypeLabel;

        // set submitBtnData
        this.submitBtnData = {"data": {}};
        this.submitBtnData.data.showDeleteBtn = this.isShowDeleteButton(clientFormMeta.createdBy);
        this.submitBtnData.data.readonly = this.readonly;
        
        // Client profile search.
        const [error1, response] = await clientProfileSearch(this.csNumber);
        if (error1) {
          console.error("Failed doing client profile search: ", error1);
        } else {
          this.clientData.data = response;
          this.formInfoData.data.clientData = response;
          
          //set sources contacted
          this.clientData.data.hideSCInput = true;
          this.clientData.data.input_key_sourceContacted = this.formInfoData.data.input_key_sourceContacted;
          this.clientData.data.formType = this.formType
          //console.log("clientData: ", this.clientData);
        }
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
        }

        // Load form data
        this.loadingMsg = "Loading client form data...";
        const [error, clientFormData] = await loadFormData(this.csNumber, this.formId);
        //console.log("client form data: ", clientFormData)
        if (error) {
          console.error(error);
        } else {
          this.formInitData = clientFormData;
          this.formInitData.data.clientFormTypeDesc = (this.formInfoData.data.clientFormType == this.$FORM_TYPE_INITIAL) ? "Assessment": this.$FORM_TYPE_REASSESSMENT;
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
      // update the displaySummary flag to hide summary panel
      this.displaySummary = false;
      this.displayCasePlan = false;
      
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
      // Current on 'Summary', cancel the print flag, and start the print
      if (this.parentNavCurLocation == this.totalNumParentNav - 1) {
        window.print();
      } else {
        // Navigate to the 'Summary' section, and set the printRequested flag to true
        this.printRequested = true;
        this.navToSectionAndQuestion(this.totalNumParentNav - 1, 1);
      }
    },
    handleSaveClose() {
      //console.log("handleSaveClose");
      //Redirect User back to clientRecord.RNAList
      this.$router.push({
        name: this.$ROUTER_NAME_CLIENTRECORD,
        params: {
          clientNum: this.csNumber,
          tabIndex: 'tab-rl'
        }
      });
    },
    handleSaveContinue(continueToNextSection) {
      //console.log("handleSaveContinue, continueToNextSection: ", continueToNextSection);
      // if not reaching the last section, increment this.parentNavCurLocation to navigate to the next section
      if (continueToNextSection && this.parentNavCurLocation < this.totalNumParentNav - 1) {
        //show case plan
        if (this.parentNavCurLocation == this.totalNumParentNav - 2) {
          this.displayCasePlan = true;
        } 
        this.parentNavMoveToNext++;
      }

      // If reaching the last section, time to validate the form and complete the form
      if (continueToNextSection && this.parentNavCurLocation == this.totalNumParentNav - 1) {
        // Notify child components (dataEntry (section level answers), caseplan(form Level answers) and sidepanel(sourceContacted)) to send their data for validation
        this.validateAndCompleteForm();
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
      
      // User clicked 'Case plan' section from the navigation panel
      if (this.parentNavCurLocation == this.totalNumParentNav - 2) {
        this.displayCasePlan = true;
      }
      // User clicked 'Summary' section from the navigation panel
      if (this.parentNavCurLocation == this.totalNumParentNav - 1) {
        this.displaySummary = true;
        this.btnSaveContinueText = "Submit Form"; 
      } else {
        this.btnSaveContinueText = "Save and Continue"; 
      }
    },
    async validateAndCompleteForm() {
      // build completeFormData
      let completeFormData = {};
      completeFormData.clientFormId = Number(this.formId);
      completeFormData.clientNumber = this.csNumber;
      completeFormData.linkedClientFormId = this.relatedClientFormId;
      console.log("completeFormData: ", completeFormData);
      const [error, completResult] = await completeForm(completeFormData);
      if (error) {
        console.error("Failed completing a form instance", error);
        this.errorOccurred = true;
        this.errorTitle = error.response.data.errorMessage;
        this.errorText = error.response.data.validationResult == null ? null : error.response.data.validationResult.errors;
      } else {
        console.log("Successfully completed the form: ", this.formId);
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
    showDeleteDialog() {
      //console.log("show delete modal, formType", this.formType);
      let modal = document.getElementById("id_modal_deleteForm_" + this.formType);
      if (modal != null) {
        modal.click();
      }
    },
    async formDeleteHelper(fullDelete) {
      // delete the form instance
      console.log("Delete form instance: ", fullDelete);
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
      console.log("saraDeleteSelectedFormTypeValue: ", this.saraDeleteSelectedFormTypeValue);
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
    ...mapStores(useStore)
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