<template>
  <div class="main crna-cmp-form">
    <v-alert border="right" color="red" dismissible v-if="errorOccurred" elevation="13" prominent>{{errorText}}</v-alert>
    <div class="wrap">
      <div class="mainRow">
        <div class="column L">
          <div class="menu-Sticky">
            <div class="menuR1">
              <FormioFormInfo :key="staticComponentKey" :dataModel="clientData" />
            </div>
            <div class="menuR2" v-if="!loading">
              <CrnaCmpFormNavigation :key="componentKey" 
                :dataModel="data_formEntries" 
                :parentNavMoveToNext="parentNavMoveToNext"
                @parentNavClicked="handleNavChildCallback"/>
            </div>
          </div>
          <v-progress-linear v-if="loading" indeterminate height="30" color="primary">{{loadingMsg}}</v-progress-linear>
          <div :class="loading ? 'hide' : 'mainContent'">
            <div class="mainContent">
              <CrnaCmpFormDataEntry :key="componentKey" 
                :dataModel="data_formEntries" 
                :initData="formInitData" 
                :saveBtnLabel="btnSaveContinueText" 
                :notifySaveDraft="notifySaveDraft"
                @saveContinueClicked="handleSaveContinue" 
                @cancelFormClicked="handleCancelForm"/>   
              
              <FormioButton 
                :buttonType="'formButton'"
                @saveContinueClicked="handleSaveContinue"
                @cancelFormClicked="handleCancelForm" />
            </div>
          </div>  
        </div>
        <div class="column R">
          <div class="R-Sticky">
            <section class="crna-right-sticky-panel">
              <div class="crna-right-panel-button-container">
                <!--Save draft button group-->
                <FormioButton v-if="!loading" 
                  :buttonType="'sideButton'"
                  @saveContinueClicked="handleSaveContinue" 
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
import { getFormDetails, updateForm, loadFormData, loadFormDataForSectionSeq, deleteQuestionInterventionsExcept, clientProfileSearch } from "@/components/form.api";
import CrnaCmpFormDataEntry from "@/components/crna-cmp/formSections/crnaCmpFormDataEntry.vue";
import CrnaCmpFormNavigation from "@/components/crna-cmp/formSections/crnaCmpFormNavigation.vue";
import FormioSidePanel from "@/components/common/FormioSidePanel.vue";
import FormioFormInfo from "@/components/common/FormioFormInfo.vue";
import FormioButton from "@/components/common/FormioButtons.vue";

export default {
  name: 'crnaForm',
  components: {
    Form,
    CrnaCmpFormDataEntry,
    CrnaCmpFormNavigation,
    FormioSidePanel,
    FormioFormInfo,
    FormioButton
  },
  data() {
    return {
      loadingMsg: "Loading form...",
      loading: false,
      formId: -1,
      csNumber: '',
      notifySaveDraft: 0,
      parentNavMoveToNext: 1,
      totalNumParentNav: 1,
      parentNavCurLocation: '0',
      btnSaveContinueText: "Save and Continue",
      data_formEntries: {},
      clientData: {},
      formInitData: {},
      dataMap: {},
      componentKey: 0,
      staticComponentKey: 0,
    }
  },
  mounted(){
    this.formId = this.$route.params.formID;
    this.csNumber = this.$route.params.csNumber;
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
      this.clientData.formTitle = "Community Risk Needs Assessment Form (CRNA-CMP)";
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
        this.data_formEntries = JSON.parse(response);
        
        // Load form data
        const [error, clientFormData] = await loadFormData(this.csNumber, this.formId);
        if (error) {
          console.error(error);
        } else {
          this.formInitData = JSON.parse(clientFormData);
        }
      }
      this.loading = false;
      this.componentKey++;
    },
    async updateForm(formData) {
      let formId= this.$route.params.formID;
      const [error, response] = await updateForm(formData);
      if (error) {
        console.error("Form update failed: ", error);
      }
    },
    handleSaveDraft(evt) {
      if (evt != null && evt.type === 'evt_saveDraft' ) {
        console.log("handleSaveDraft: ");
        this.notifySaveDraft++;
      }
    },
    handlePrintForm(evt) {
      if (evt != null && evt.type === 'evt_print' ) {
        console.log("handlePrint: ");
      } 
    },
    handleSaveContinue(formData) {
      //console.log("formData: ", formData);
      // if not reaching the last section, increment this.parentNavCurLocation to navigate to the next section
      if (this.parentNavCurLocation < this.totalNumParentNav - 1) {
        this.parentNavMoveToNext++;
      }
      if (formData != null) {
        this.updateForm(formData);
      }
    },
    handleCancelForm() {
      console.log("Cancel Form");
      this.$emit("cancelFormClicked");
    },
    handleNavChildCallback(parentNavCurLocationFromChild) {
      this.parentNavCurLocation = parentNavCurLocationFromChild;
      if (parentNavCurLocationFromChild == this.totalNumParentNav - 1) {
        this.btnSaveContinueText = "Submit Form"; 
      } else {
        this.btnSaveContinueText = "Save and Continue"; 
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
  color: #fcba19;
  text-decoration: underline;
  -webkit-text-decoration-color: rgb(255, 208, 0); /* Safari */  
  text-decoration-color: rgb(255, 208, 0);
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