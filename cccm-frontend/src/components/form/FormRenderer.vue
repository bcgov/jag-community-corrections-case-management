<template>
  <div data-app class="main crna-cmp-form">
    <!-- CRNA form instance. Delete modal dialog section-->
    <div v-if="formType === $CONST_FORMTYPE_CRNA">
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
        <v-card>
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
      </v-dialog>
    </div>

    <!-- SARA form instance. Delete modal dialog section-->
    <div v-if="formType === $CONST_FORMTYPE_SARA">
      <!--Form delete modal dialog -->
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
        <v-card>
          <div class="col-sm-12 m-7">
            <v-card-title >
              Select what you want to delete:
            </v-card-title>
            <v-checkbox
              v-model="saraDeleteSelectedFormTypeValue"
              label="CRNA-CMP"
              value="crna"
            ></v-checkbox>
            <v-checkbox
              v-model="saraDeleteSelectedFormTypeValue"
              :readonly=true
              label="SARA-CMP"
              value="sara"
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
      </v-dialog>
    </div>
    <div class="wrap">
      <div class="mainRow">
        <div class="column L">
          <div class="menu-Sticky">
            <div class="menuR1">
              <v-alert v-if="errorOccurred" color="#f81e41" dismissible  elevation="13" prominent>
                <span v-html="getErrorText"></span>
              </v-alert>

              <FormioFormInfo :key="formInfoKey" :dataModel="formInfoData" @unlockForm="handleUnlockForm" />
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
      
            <FormCaseplan v-if="displayCasePlan" 
              :dataModel="casePlanDataModel" 
              :initData="formInitData"
              :clientFormId="formId"
              :csNumber="csNumber"
              :options="options"/>

            <FormSummary v-if="displaySummary" 
              @viewSectionQuestion="navToSectionAndQuestion" 
              :clientFormId="formId"
              :csNumber="csNumber" />

            <FormioButton 
              :buttonType="'formButton'"
              :saveBtnLabel="btnSaveContinueText" 
              @saveContinueClicked="handleSaveContinue"
              @cancelFormClicked="handleDeleteForm" 
              :options="options"/>
          </div>
        </div>
        <div class="column R">
          <div class="R-Sticky">
            <section class="crna-right-sticky-panel">
              <div class="crna-right-panel-button-container">
                <!--Save Close button group-->
                <FormioButton v-if="!loading" 
                  :buttonType="'sideButton'"
                  @saveCloseClicked="handleSaveClose" 
                  @printFormClicked="handlePrintForm" />
              </div>
              <div class="crna-right-panel-details">
                <FormioSidePanel :key="formStaticInfoKey" 
                  :dataModel="clientData" 
                  :clientFormId="formId"
                  :options="options"/>
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
import { getClientFormMetaData, getFormioTemplate, loadFormData, clientProfileSearch, validateCRNAForm, validateSARAForm, completeForm, deleteForm, unlockForm } from "@/components/form.api";
import FormDataEntry from "@/components/form/formSections/FormDataEntry.vue";
import FormNavigation from "@/components/form/formSections/FormNavigation.vue";
import FormioSidePanel from "@/components/common/FormioSidePanel.vue";
import FormioFormInfo from "@/components/common/FormioFormInfo.vue";
import FormioButton from "@/components/common/FormioButtons.vue";
import FormSummary from '@/components/form/formSections/FormSummary.vue';
import FormCaseplan from '@/components/form/formSections/FormCasePlan.vue';

export default {
  name: 'FormRenderer',
  props: {
    formType: '',
    formId: 0,
    csNumber: '',
    relatedClientFormId: 0,
    readonly: false
  },
  components: {
    Form,
    FormDataEntry,
    FormNavigation,
    FormioSidePanel,
    FormioFormInfo,
    FormioButton,
    FormSummary,
    FormCaseplan
  },
  data() {
    return {
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
      formInfoKey: 0,
      formStaticInfoKey: 0,
      loadingMsgCasePlanIntervention: "Loading intervention data...",
      displayCasePlan: false,
      casePlanDataModel: {"display": "form", "components": []},
      errorOccurred: false,
      errorText: '',
      deleteDialog: false,
      saraDeleteSelectedFormTypeValue: ["sara"],
      options: {},
    }
  },
  mounted(){
    this.options.readOnly = this.readonly;
    //console.log("form renderer mounted: ", this.readonly, this.options, this.formType, this.formId , this.relatedClientFormId, this.csNumber);
    this.getClientAndFormMeta();
    this.getFormioTemplate();
  },
  methods: {
    handleUnlockForm() {
      this.options.readOnly = false;
      this.formInfoData.data.readonly = false;
      this.componentKey++;
      this.formStaticInfoKey++;
    },
    async getClientAndFormMeta() {
      // ClientForm Meta data search.
      const [error, clientFormMeta] = await getClientFormMetaData(this.csNumber, this.formId);
      if (error) {
        console.error("Failed getting client form metadata: ", error);
      } else {
        //console.log("clientFormMeta: ", clientFormMeta);
        this.formInfoData.data = clientFormMeta;
        this.formInfoData.data.readonly = this.readonly;
        this.formInfoData.data.clientFormType = (this.formInfoData.data.clientFormType) ? "Reassessment" : "Initial"

        // set the form title
        if (this.formType == this.$CONST_FORMTYPE_CRNA) {
          this.formInfoData.data.formTitle = "Community Risk Needs Assessment Form (CRNA-CMP)";
          this.formInfoData.data.formType = "CRNA-CMP Type"
        } else if (this.formType == this.$CONST_FORMTYPE_SARA) {
          this.formInfoData.data.formTitle = "SARA (SARA-CMP)";
          this.formInfoData.data.formType = "SARA-CMP Type"
        }
        //console.log("this.formInfoData: ", this.formInfoData);
        this.formInfoKey++;

        // Client profile search.
        const [error1, response] = await clientProfileSearch(this.csNumber);
        if (error1) {
          console.error("Failed doing client profile search: ", error1);
        } else {
          this.clientData.data = response;
          
          //set sources contacted
          this.clientData.data.hideSCInput = true;
          this.clientData.data.input_key_sourceContacted = this.formInfoData.data.input_key_sourceContacted;
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
        console.error(error);
      } else {
        this.loadingMsg = "Setup navigation...";
        this.data_formEntries = response;
        // force FormNavigation to refresh.
        this.componentKey++;

        this.totalNumParentNav = response == null || response.components == null ? 0 : response.components.length - 1;
        if (this.totalNumParentNav >= 2) {
          const clone = JSON.parse(JSON.stringify(this.data_formEntries.components[this.totalNumParentNav - 2].components));
          this.casePlanDataModel.components = clone;
          this.data_formEntries.components[this.totalNumParentNav - 2].components = [];
          //console.log("this.data_formEntries:", this.data_formEntries);
        }

        // Load form data
        this.loadingMsg = "Loading client form data...";
        const [error, clientFormData] = await loadFormData(this.csNumber, this.formId);
        //console.log("client form data: ", clientFormData)
        if (error) {
          console.error(error);
        } else {
          this.formInitData = clientFormData;
          this.formInitData.data.clientFormType = this.formInfoData.data.clientFormType.toLowerCase();
        }
        //console.log("this.formInitData:", this.formInitData);
      }
      this.loading = false;
    },
    navToSectionAndQuestion(section: number, question: number) {
      //console.log("Navigating %d %d", section, question);
      // update the displaySummary flag to hide summary panel
      this.displaySummary = false;
      this.displayCasePlan = false;
      //this.private_showHideCasePlanInterventions(false);

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
      console.log("handlePrint: "); 
      window.print();
    },
    handleSaveClose() {
      //console.log("handleSaveClose");
      //Redirect User back to clientRecord.RNAList
      this.$router.push({
        name: 'clientrecord',
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
    async handleNavChildCallback(parentNavCurLocationFromChild) {
      //console.log("handleNavChildCallback parentNavCurLocationFromChild", parentNavCurLocationFromChild);
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
      completeFormData.formLevelComments = '';
      completeFormData.sourcesContacted = '';
      completeFormData.planSummary = '';
      console.log("completeFormData: ", completeFormData);
      
      const [error, completResult] = await completeForm(completeFormData);
      if (error) {
        console.error("Failed completing a form instance", error);
        this.errorOccurred = true;
        this.errorText = error.response.data.errorMessage;
      } else {
        console.log("Successfully completed the form: ", this.formId);
        //Redirect User back to clientRecord.RNAList
        this.$router.push({
          name: 'clientrecord',
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
      //console.log("Delete form instance");
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
            name: 'clientrecord',
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
      //console.log("saraDeleteSelectedFormTypeValue: ", this.saraDeleteSelectedFormTypeValue);
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
      for (let i = 0; i < this.errorText.length; i++) {
        error += this.errorText[i].message + "<br>";
      }
      return error;
    }
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

.crna-subSectionTitleClass {
  font-size: 20px;
  font-weight: bold;
  padding-bottom: 1px!important;
}

.crna-subSectionTitleClass:nth-child(1):after {
  content: "";
  height: 0px;
  width: 50px;
  display: block;
  border: 4px solid #FCBA19;
  margin-bottom:20px;
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