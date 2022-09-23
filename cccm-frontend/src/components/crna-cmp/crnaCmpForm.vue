<template>
  <div class="main">
    <div class="wrap">
      <div class="mainRow">
        <div class="column L">
          <div class="menu-Sticky">
            <div class="menuR1">
              <CrnaCmpFormInfo :dataModelFormInfo="formJSONFormData" :key="componentKey" />
            </div>
            <div class="menuR2" v-if="!loading">
              <FormNavComponent :sectionQuestionMap="sectionQuestionMap" @parentNavClicked="handleNavChildCallback"
                :dataModel="formJSONFormData" :parentNavMoveToNext="parentNavMoveToNext" :key="componentKey" />
            </div>
            <p />
          </div>
          <v-progress-linear v-if="loading" indeterminate height="30" color="primary">{{loadingMsg}}</v-progress-linear>
          <div :class="loading ? 'hide' : 'mainContent'" class="mainContent">

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
                <FormioButton v-if="!loading" :dataModel="formJSONFormData.buttonGroupSavePrint"
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
import { getFormDetails, updateForm, loadFormData, loadFormDataForSectionSeq } from "@/components/form.api";

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
      form: null,
      loadingMsg: "Loading form...",
      trackedData: {},
      loading: false,
      message: "",
      parentNavMoveToNext: 1,
      notifySavingData: 1,
      totalNumParentNav: 0,
      parentNavCurLocationFromChild: 0,
      parentNavCurLocation: 0,
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
      this.loadingMsg = "Loading form...";
      let formId = this.$route.params.formID;
      let csNumber = this.$route.params.csNumber;
      let vm = this;
      console.log("CRNA formDetails: ", formId);
      const [error, response] = await getFormDetails("00142091", formId);
      if (error) {
        console.error(error);
      } else {
        console.log("Got form %o", response);
        try {

          await Formio.createForm(document.getElementById('formio'), response).then(form => {
            // Prevent the submission from going to the form.io server.
            form.nosubmit = true;
            vm.form = form;
            form.on("change", function (e) {
              console.log("Change event %o", e);
              vm.trackedData[vm.parentNavCurLocation] = form.data;
              console.log("--> DATA %o", vm.trackedData);
            });

            // capture the submit
            form.on('submit', function (submissionData) {
              vm.submission = JSON.stringify(submissionData, null, 2);
              form.emit('submitDone', submissionData);

            });

            form.on('error', function (errorData) {
              errorData.forEach(error => {
                console.error("Error in renderer %o", error);
              })
            });




            form.components.filter((component) => component.hasOwnProperty('components')).forEach(sectionComponent => {
              console.log("Section %s", sectionComponent.component.title);
              let questions = [];

              // track questions for section
              sectionComponent.components.forEach(question => {
                if (question.path.indexOf("_") === -1) {
                  questions.push(question.component.label);
                }
              });

              this.sectionQuestionMap[sectionComponent.component.title] = {};
              this.sectionQuestionMap[sectionComponent.component.title].questions = questions;


            });
            console.log("Map %o", this.sectionQuestionMap);
            this.componentKey += 1;



          }).then(async () => {
            console.log("Ready");
            vm.loadingMsg = "Loading client form data...";
            const [error, clientFormData] = await loadFormData(csNumber, formId);
            if (error) {

            } else {
              vm.form.data = clientFormData.data;
              for (const [key, value] of Object.entries(clientFormData.data)) {
                let field = vm.form.getComponent(key);
                if (field) {

                  field.setValue(value);
                  if (value === true) {
                    field.updateValue(!value);
                    field.updateValue(value);

                  }
                }
              }
            }

          });
        } catch (err) {
          console.log("Failed to render form %o", err);
        }





        // hide other sections
        this.hideSectionsExceptCurrent(0);


        this.totalNumParentNav = Object.keys(this.sectionQuestionMap).length;
        this.loading = false;
      }
    },

    async handleSaveContinue(continueToNextSection) {

      console.log("Saving data %o", this.form.data);
      let formId = this.$route.params.formID;
      let csNumber = this.$route.params.csNumber;
      const [error, response] = await updateForm(csNumber, formId, this.form.data);
      if (error) {
        console.error(error);
      } else {
        console.log("Saved form %o", response);
      }

      // if continueToNextSection is true and not reaching the last section, increment this.parentNavCurLocation to navigate to the next section
      if (continueToNextSection && this.parentNavCurLocation < this.totalNumParentNav - 1) {
        this.hideSectionsExceptCurrent(this.parentNavMoveToNext);
        this.parentNavMoveToNext++;
      }

      // have to make sure the value is different to notify child components to gather the data
      this.notifySavingData++;
    },
    handlePrintForm() {
      console.log('Print Form.')
    },
    hideSectionsExceptCurrent(sectionIdx) {
      // hide all non-selected sections
      this.form.components.forEach(sectionComponent => {
        let sectionNumber = Number.parseInt(sectionComponent.key.substr(1, 2)) - 1;
        if (sectionNumber !== sectionIdx) {
          sectionComponent.element.setAttribute('style', 'display:none');
        } else {
          sectionComponent.element.setAttribute('style', 'display:block');
        }
      });
    },
    handleCancelForm() {
      console.log("Cancel Form");
    },
    handleNavChildCallback(parentNavCurLocationFromChild) {
      console.log("Nav event %o", parentNavCurLocationFromChild);

      // hide other sections
      this.hideSectionsExceptCurrent(parentNavCurLocationFromChild);

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
  padding: 20px 0 5px 0!important;
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