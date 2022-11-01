<template>
  <div class="main crna-cmp-form">
    <!-- <v-alert border="right" color="red" dismissible v-if="errorOccurred" elevation="13" prominent>{{errorText}}</v-alert> -->
    <div class="wrap">
      <div class="mainRow">
        <div class="column L">
          <div class="menu-Sticky">
            <div class="menuR1">
              <FormioFormInfo :key="formInfoKey" :dataModel="formInfoData" />
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
          
          <div :class="loading ? 'hide' : 'mainContent'">
            <FormDataEntry :key="componentKey" 
              :csNumber="csNumber"
              :formId="formId"
              :dataModel="data_formEntries" 
              :initData="formInitData" />   
      
            <FormCaseplan v-if="displayCasePlan" 
              :dataModel="casePlanDataModel" 
              :initData="formInitData"
              :clientFormId="formId"
              :csNumber="csNumber"/>

            <FormSummary v-if="displaySummary" 
              @viewSectionQuestion="navToSectionAndQuestion" 
              :showSummaryCounter="showSummaryCounter"
              :clientFormId="formId"
              :csNumber="csNumber" />

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
                  @saveCloseClicked="handleSaveClose" 
                  @printFormClicked="handlePrintForm" />
              </div>
              <div class="crna-right-panel-details">
                <FormioSidePanel :key="formStaticInfoKey" 
                  :dataModel="clientData" 
                  :clientFormId="formId"/>
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
import { getClientFormMetaData, getFormioTemplate, loadFormData, clientProfileSearch } from "@/components/form.api";
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
    csNumber: ''
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
      showSummaryCounter: 0,
      loadingMsgCasePlanIntervention: "Loading intervention data...",
      displayCasePlan: false,
      casePlanDataModel: {"display": "form", "components": []}
    }
  },
  mounted(){
    this.displaySummary = false;
    this.getClientAndFormMeta();
    this.getFormioTemplate();
  },
  methods: {
    async getClientAndFormMeta() {
      // ClientForm Meta data search.
      const [error, clientFormMeta] = await getClientFormMetaData(this.csNumber, this.formId);
      if (error) {
        console.error("Failed getting client form metadata: ", error);
      } else {
        console.log("clientFormMeta: ", clientFormMeta);
        this.formInfoData.data = clientFormMeta;
        this.formInfoData.data.clientFormType = (this.formInfoData.data.clientFormType) ? "Reassessment" : "Initial"

        // set the form title
        if (this.formType == this.$CONST_FORMTYPE_CRNA) {
          this.formInfoData.data.formTitle = "Community Risk Needs Assessment Form (CRNA-CMP)";
          this.formInfoData.data.formType = "CRNA-CMP Type"
        } else if (this.formType == this.$CONST_FORMTYPE_SARA) {
          this.formInfoData.data.formTitle = "SARA (SARA-CMP)";
          this.formInfoData.data.formType = "SARA-CMP Type"
        }
        this.formInfoKey++;

        // Client profile search.
        const [error1, response] = await clientProfileSearch(this.csNumber);
        if (error1) {
          console.error("Failed doing client profile search: ", error1);
        } else {
          this.clientData.data = response;
          
          //set sources contacted
          this.clientData.data.input_key_sourceContacted = this.formInfoData.data.input_key_sourceContacted;
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

        this.totalNumParentNav = response == null || response.components == null ? 0 : response.components.length;
        if (this.totalNumParentNav >= 2) {
          const clone = JSON.parse(JSON.stringify(this.data_formEntries.components[this.totalNumParentNav - 2].components));
          this.casePlanDataModel.components = clone;
          this.data_formEntries.components[this.totalNumParentNav - 2].components = [];
          //console.log("caseplan template:", this.casePlanDataModel);
        }

        // Load form data
        this.loadingMsg = "Loading client form data...";
        const [error, clientFormData] = await loadFormData(this.csNumber, this.formId);
        //console.log("client form data: ", clientFormData)
        if (error) {
          console.error(error);
        } else {
          this.formInitData = clientFormData;
        }
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
      console.log("handleSaveClose");
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
    },
    handleCancelForm() {
      //console.log("Cancel Form");
      this.$emit("cancelFormClicked");
    },
    async handleNavChildCallback(parentNavCurLocationFromChild) {
      //console.log("handleNavChildCallback parentNavCurLocationFromChild", parentNavCurLocationFromChild);
      this.parentNavCurLocation = parentNavCurLocationFromChild;
      this.displaySummary = false;
      this.displayCasePlan = false;
      //this.private_showHideCasePlanInterventions(false);

      // User clicked 'Case plan' section from the navigation panel
      if (this.parentNavCurLocation == this.totalNumParentNav - 2) {
        //console.log("set displayCasePlan to true");
        this.displayCasePlan = true;
      }
      // User clicked 'Summary' section from the navigation panel
      if (this.parentNavCurLocation == this.totalNumParentNav - 1) {
        this.displaySummary = true;
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