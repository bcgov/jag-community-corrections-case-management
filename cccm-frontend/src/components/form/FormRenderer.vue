<template>
  <div class="main crna-cmp-form">
    <!-- <v-alert border="right" color="red" dismissible v-if="errorOccurred" elevation="13" prominent>{{errorText}}</v-alert> -->
    <div class="wrap">
      <div class="mainRow">
        <div class="column L">
          <div class="menu-Sticky">
            <div class="menuR1">
              <FormioFormInfo :key="staticComponentKey" :dataModel="clientData" />
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
          <v-progress-linear v-if="loadingCasePlanIntervention" indeterminate height="30" color="primary">{{loadingMsgCasePlanIntervention}}</v-progress-linear>
          <div :class="loading ? 'hide' : 'mainContent'">
            <FormDataEntry :key="componentKey" 
              :csNumber="csNumber"
              :formId="formId"
              :dataModel="data_formEntries" 
              :initData="formInitData" />   
            
            <FormSummary v-if="displaySummary" 
              @viewSectionQuestion="navToSectionAndQuestion" 
              :showSummaryCounter="showSummaryCounter" />

            <FormioButton 
              :buttonType="'formButton'"
              :saveBtnLabel="btnSaveContinueText" 
              @saveContinueClicked="handleSaveContinue"
              @cancelFormClicked="handleCancelForm" />
          </div>
        </div>
        <div class="column R">
          <div class="R-Sticky">
            <section class="crna-right-sticky-panel">
              <div class="crna-right-panel-button-container">
                <!--Save draft button group-->
                <FormioButton v-if="!loading" 
                  :buttonType="'sideButton'"
                  @saveDraftClicked="handleSaveContinue" 
                  @printFormClicked="handlePrintForm" />
              </div>
              <div class="crna-right-panel-details">
                <FormioSidePanel :key="staticComponentKey" :dataModel="clientData" />
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
import { getFormDetails, loadFormData, loadFormDataForSectionSeq, deleteQuestionInterventionsExcept, clientProfileSearch } from "@/components/form.api";
import FormDataEntry from "@/components/form/formSections/FormDataEntry.vue";
import FormNavigation from "@/components/form/formSections/FormNavigation.vue";
import FormioSidePanel from "@/components/common/FormioSidePanel.vue";
import FormioFormInfo from "@/components/common/FormioFormInfo.vue";
import FormioButton from "@/components/common/FormioButtons.vue";
import FormSummary from '@/components/form/formSections/FormSummary.vue';

export default {
  name: 'CmpFormDetail',
  components: {
    Form,
    FormDataEntry,
    FormNavigation,
    FormioSidePanel,
    FormioFormInfo,
    FormioButton,
    FormSummary
  },
  data() {
    return {
      loadingMsg: "Loading form...",
      loading: false,
      formType: '',
      formId: -1,
      csNumber: '',
      displaySummary: false,
      parentNavMoveToNext: 1,
      parentNavJumpToPointed: '',
      parentNavJumpToPointed_sufix: 0,
      totalNumParentNav: 1,
      parentNavCurLocation: '0',
      btnSaveContinueText: "Save and Continue",
      data_formEntries: {},
      clientData: {},
      formInitData: {},
      dataMap: {},
      componentKey: 0,
      staticComponentKey: 0,
      showSummaryCounter: 0,
      loadingCasePlanIntervention: false,
      loadingMsgCasePlanIntervention: "Loading intervention data..."

    }
  },
  mounted(){
    this.formType = this.$route.params.formType;
    this.formId = this.$route.params.formID;
    this.csNumber = this.$route.params.csNumber;
    this.displaySummary = false;
    this.clientProfileSearchAPI();
    this.getFormDetails();
  },
  methods: {
    async clientProfileSearchAPI() {
      // Client profile search.
      const [error, response] = await clientProfileSearch(this.csNumber);
      //this.clientData = response.data;
      this.clientData = {};
      this.clientData =
        {
          "clientId": "1",
          "clientName": "Ross, Bob",
          "clientNum": "123456780",
          "clientAge": 44,
          "profileClosed": false,
          "communityAlerts": [
            {
              "date": "2022-01-02",
              "comment": "Client threatened staff"
            },
            {
              "date": "2022-03-02",
              "comment": "Client brought knife to meeting"
            },
            {
              "date": "2022-04-02",
              "comment": "Client attacked staff"
            }
          ],
          "outstandingWarrants": [
            {
              "type": "string",
              "date": "2022-01-02",
              "courtFile": "Client threatened staff"
            },
            {
              "type": "string",
              "date": "2022-03-02",
              "courtFile": "Client brought knife to meeting"
            },
            {
              "type": "string",
              "date": "2022-04-02",
              "courtFile": "Client attacked staff"
            }
          ],
          "supervisionLevel": "High",
          "birthDate": "1979-12-03",
          "communityInformation": {
            "communityLocation": "Victoria",
            "status": "Active",
            "caseManager": "Smith, Bob",
            "secondaryManager": "Doe, Jane"
          },
          "orderInformation": {
            "orders": "None",
            "effectiveDate": "2022-03-04",
            "expiryDate": "2022-03-05",
            "dueDate": "2022-03-04"
          },
          "generalInformation": {
            "institution": "0543- Sunshine Coast Health Centre",
            "status": "Inactive",
            "custody": "Warrant of commital",
            "dischargeDate": "2022-04-03",
            "type": "In (parole)",
            "paroleDate": "2022-03-04"
          },
          "locationInformation": {
            "internalLocation": "0543 - Sunshine Coast Health Centre",
            "outLocation": "0543 - Sunshine Coast Health Centre",
            "federalParole": "0101 - Victoria Corrections",
            "outReason": "Sentence ended",
            "warrantExpiryDate": "2022-05-04"
          },
          "biometric": {
            "type": "No",
            "status": "Inactive",
            "eServices": "No",
            "eReporting": "No"
          },
          "address": [
            {
              "fullAddress": "123 Hello St, Victoria BC, 123 abc",
              "type": "Work",
              "primary": true
            }
          ],
          "designations": [
            {
              "type": "GEN",
              "rating": "low"
            },
            {
              "type": "SMO",
              "rating": "high"
            }
          ],
          "programs": [
            {
              "name": "string",
              "status": "string",
              "referredDate": "string",
              "startDate": "string",
              "outcome": "string"
            }
          ],
          "sealed": "Yes",
          "gender": "Male"
      };
      // set the form title
      if (this.formType == this.$CONST_FORMTYPE_CRNA) {
        this.clientData.formTitle = "Community Risk Needs Assessment Form (CRNA-CMP)";
        this.clientData.formType = "CRNA-CMP Type"
      } else if (this.formType == this.$CONST_FORMTYPE_SARA) {
        this.clientData.formTitle = "SARA (SARA-CMP)";
        this.clientData.formType = "SARA-CMP Type"
      }
      this.staticComponentKey++;
    },
    async getFormDetails() {
      // Load formio template
      this.loading = true;
      this.loadingMsg = "Loading form...";
      const [error, response] = await getFormDetails(this.csNumber, this.formId);
      if (error) {
        console.error(error);
      } else {
        this.loadingMsg = "Loading client form data...";
        this.data_formEntries = response;
        this.totalNumParentNav = response == null || response.components == null ? 0 : response.components.length;
        
        // Load form data
        const [error, clientFormData] = await loadFormData(this.csNumber, this.formId);
        if (error) {
          console.error(error);
        } else {
          this.formInitData = clientFormData;
        }
      }
      this.loading = false;
      this.componentKey++;
    },
    navToSectionAndQuestion(section: number, question: number) {
      //console.log("Navigating %d %d", section, question);
      // update the displaySummary flag to hide summary panel
      this.displaySummary = false;
      this.private_showHideCasePlanInterventions(false);

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
    handleSaveContinue(continueToNextSection) {
      console.log("handleSaveContinue, continueToNextSection: ", continueToNextSection);
      // if not reaching the last section, increment this.parentNavCurLocation to navigate to the next section
      if (continueToNextSection && this.parentNavCurLocation < this.totalNumParentNav - 1) {
        //show case plan
        if (this.parentNavCurLocation == this.totalNumParentNav - 2) {
          this.private_getCasePlanData();
        }
        this.parentNavMoveToNext++;
      }
    },
    handleCancelForm() {
      console.log("Cancel Form");
      this.$emit("cancelFormClicked");
    },
    async handleNavChildCallback(parentNavCurLocationFromChild) {
      this.parentNavCurLocation = parentNavCurLocationFromChild;
      this.displaySummary = false;
      this.private_showHideCasePlanInterventions(false);

      // User clicked 'Case plan' section from the navigation panel
      if (this.parentNavCurLocation == this.totalNumParentNav - 2) {
        //console.log("set show_intervention to true");
        this.private_getCasePlanData();
      }
      // User clicked 'Summary' section from the navigation panel
      if (this.parentNavCurLocation == this.totalNumParentNav - 1) {
        this.displaySummary = true;
        this.btnSaveContinueText = "Submit Form"; 
      } else {
        this.btnSaveContinueText = "Save and Continue"; 
      }
    },
    async private_getCasePlanData() {
      this.loadingCasePlanIntervention = true;
      this.loadingMsgCasePlanIntervention = "Loading intervention data...";
      const [error, clientFormData] = await loadFormData(this.csNumber, this.formId);
      if (error) {
        console.error(error);
      } else {
        // do something
        this.loadingCasePlanIntervention = false;
        this.loadingMsgCasePlanIntervention = "";
        this.private_showHideCasePlanInterventions(true);
      }
    },
    private_showHideCasePlanInterventions(isShow) {
      // get intervention panel
      const thePanel = document.querySelector(`div[ref="nested-intervention_panel"]`);
      //console.log("thePanel: ", thePanel);
      if (isShow) {
        //console.log("show intervention");
        if (thePanel != null) {
          thePanel.setAttribute('style', 'display:block');
        }
      } else {
        //console.log("hide intervention");
        if (thePanel != null) {
          thePanel.setAttribute('style', 'display:none');
        }
      }
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