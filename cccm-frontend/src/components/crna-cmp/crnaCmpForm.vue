<template>
  <div class="main">
    <div class="wrap">
      <div class="mainRow">
        <div class="column L">
          <div class="menu-Sticky">
            <div class="menuR1">
              <CrnaCmpFormInfo :dataModelFormInfo="formJSONFormData" :key="componentKey" />
            </div>
            <div class="menuR2">
              <FormNavComponent :sectionQuestionMap="sectionQuestionMap" @parentNavClicked="handleNavChildCallback"
                :dataModel="formJSONFormData" :parentNavMoveToNext="parentNavMoveToNext" :key="componentKey" />
            </div>
            <p/>
          </div>
          <v-progress-circular
      :size="100"
      color="purple" v-if="loading"
      indeterminate
    ></v-progress-circular>
          <div class="mainContent">
 
            <!-- form gets rendered here -->
            <div id="formio" />

            <!-- <CrnaCmpFormDataEntry :dataModel="formJSONFormData" :key="componentKey"
              :notifySavingData="notifySavingData"></CrnaCmpFormDataEntry> -->
            <!--Save and continue button group-->
            <FormioButton :dataModel="formJSONFormData.buttonGroupSaveCancel" @saveContinueClicked="handleSaveContinue"
              @cancelFormClicked="handleCancelForm" />
          </div>
        </div>
        <div class="column R">
          <div class="R-Sticky">
            <section class="crna-right-sticky-panel">
              <div class="crna-right-panel-button-container">
                <!--Save draft button group-->
                <FormioButton :dataModel="formJSONFormData.buttonGroupSavePrint"
                  @saveContinueClicked="handleSaveContinue" @printFormClicked="handlePrintForm" />
              </div>
              <div class="crna-right-panel-details">
                <CrnaCmpFormRightPanel :dataModel="formJSONFormData" :key="componentKey" />
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
import { getFormDetails } from "@/components/form.api";

import CrnaCmpFormDataEntry from "@/components/crna-cmp/formSections/crnaCmpFormDataEntry.vue";
import FormNavComponent from "@/components/common/FormNavComponent.vue";
import CrnaCmpFormRightPanel from "@/components/crna-cmp/formSections/crnaCmpFormRightPanel.vue";
import CrnaCmpFormInfo from "@/components/crna-cmp/formSections/crnaCmpFormInfo.vue";
import FormioButton from "@/components/common/FormioButtons.vue";
import { Formio } from "formiojs";

import sampleFormData from './sampleData/sampleFormData.json';

export default {
  name: 'crnaForm',
  components: {
    CrnaCmpFormDataEntry,
    FormNavComponent,
    CrnaCmpFormRightPanel,
    CrnaCmpFormInfo,
    FormioButton,
  },
  data() {
    return {
      loading: false,
      message: "",
      parentNavMoveToNext: 1,
      notifySavingData: 1,
      totalNumParentNav: 1,
      parentNavCurLocation: '0',
      btnSaveContinueText: "Save and Continue",
      formJSONFormData: sampleFormData,
      componentKey: 0,
      sectionQuestionMap: {},
    }
  },
  mounted() {
    this.getFormData();
  },
  methods: {
    async getFormData() {
      this.loading = true;
      let formId = this.$route.params.formID;
      let vm = this;
      console.log("CRNA formDetails: ", formId);
      const [error, response] = await getFormDetails("00142091", formId);
      if (error) {
        console.error(error);
      } else {
        console.log("Got form %o", response);

        await Formio.createForm(document.getElementById('formio'), response).then(form => {

          form.on("change", function (e) {
            console.log("Change event %o", e);
            console.log(form.schema);
            vm.formJSON = JSON.stringify(form.schema, null, 2);
          });

          form.components.filter((component) => component.hasOwnProperty('components')).forEach(sectionComponent => {
            console.log("Section %s", sectionComponent.component.title);
            let questions = [];

            sectionComponent.components.forEach(question => {
              if (question.path.indexOf("_") === -1) {
                questions.push(question.component.label);
              }
            });
            debugger;
            this.sectionQuestionMap[sectionComponent.component.title] = {};
            this.sectionQuestionMap[sectionComponent.component.title].questions = questions;


          });
          console.log("Map %o", this.sectionQuestionMap);
          this.totalNumParentNav = this.sectionQuestionMap.length;
          this.componentKey += 1;
        });

        this.loading = false;
        console.log("Here %o",this.sectionQuestionMap );


      }
    },

    handleSaveContinue(continueToNextSection) {
      // if continueToNextSection is true and not reaching the last section, increment this.parentNavCurLocation to navigate to the next section
      if (continueToNextSection && this.parentNavCurLocation < this.totalNumParentNav - 1) {
        this.parentNavMoveToNext++;
      }
      // have to make sure the value is different to notify child components to gather the data
      this.notifySavingData++;
    },
    handlePrintForm() {
      console.log('Print Form.')
    },
    handleCancelForm() {
      console.log("Cancel Form");
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
  -webkit-text-decoration-color: rgb(255, 208, 0);
  /* Safari */
  text-decoration-color: rgb(255, 208, 0);
}

.subSectionChildTitleClass {
  font-size: 15px;
  font-weight: bold;
  border-bottom: 1px solid #ccc;
}

.crna-subSectionTitleClass {
  font-size: 20px;
  font-weight: bold;
  padding-bottom: 1px !important;
  padding-top: 20px !important;
}

.crna-subSectionTitleClass:nth-child(1):after {
  content: "";
  height: 0px;
  width: 50px;
  display: block;
  border: 4px solid #FCBA19;
  margin-bottom: 20px;
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