<template>
  <div class="main crna-cmp-form">
    <v-alert border="right" color="red" dismissible v-if="errorOccurred" elevation="13" prominent>{{errorText}}</v-alert>
    <div class="wrap">
      <div class="mainRow">
        <div class="column L">
          <div class="menu-Sticky">
            <div class="menuR1">
              <CrnaCmpFormInfo :dataModelFormInfo="formJSONFormData" :key="componentKey" />
            </div>
            <div class="menuR2" v-if="!loading">
              <FormNavComponent :saveInProgress="saving" :sectionQuestionMap="sectionQuestionMap"
                @parentNavClicked="handleNavChildCallback" :dataModel="formJSONFormData"
                :currentSection="currentSection" :currentQuestion="currentQuestion"
                :parentNavMoveToNext="parentNavMoveToNext" :key="componentKey" />
            </div>
            <p />
          </div>
          <v-progress-linear v-if="loading" indeterminate height="30" color="primary">{{loadingMsg}}</v-progress-linear>
          <div :class="loading ? 'hide' : 'mainContent'">
          <!-- <div class="'mainContent'"> -->
            <!-- form gets rendered here -->
            <div id="formio" />

            <FormSummary @viewSectionQuestion="navToSectionAndQuestion" v-if="displaySummary"
              :showSummaryCounter="showSummaryCounter" />

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
import { getFormDetails, updateForm, loadFormData, loadFormDataForSectionSeq, deleteQuestionInterventionsExcept } from "@/components/form.api";

import CrnaCmpFormDataEntry from "@/components/crna-cmp/formSections/crnaCmpFormDataEntry.vue";
import FormNavComponent from "@/components/common/FormNavComponent.vue";
import CrnaCmpFormRightPanel from "@/components/crna-cmp/formSections/crnaCmpFormRightPanel.vue";
import CrnaCmpFormInfo from "@/components/crna-cmp/formSections/crnaCmpFormInfo.vue";
import FormioButton from "@/components/common/FormioButtons.vue";
import { Formio } from "formiojs";
import sampleFormData from './sampleData/sampleFormData.json';
import FormSummary from '../forms/FormSummary.vue';

export default {
  name: 'crnaForm',
  props: {
    formId: {
      type: String,
      required: true,
      default: ''
    },
    csNumber: {
      type: String,
      required: true,
      default: ''
    }
  },
  components: {
    CrnaCmpFormDataEntry,
    FormNavComponent,
    CrnaCmpFormRightPanel,
    CrnaCmpFormInfo,
    FormioButton,
    FormSummary
  },
  data() {
    return {
      form: null,
      loadingMsg: "Loading form...",
      loading: false,
      message: "",
      displaySummary: false,
      currentSection: 0,
      currentQuestion: 0,
      parentNavMoveToNext: 0,
      dataChangeCount: 0,
      notifySavingData: 1,
      totalNumParentNav: 0,
      parentNavCurLocationFromChild: 0,
      parentNavCurLocation: 0,
      btnSaveContinueText: "Continue",
      formJSONFormData: sampleFormData,
      componentKey: 0,
      errorOccurred: false,
      saving: false,
      errorText: '',
      showSummaryCounter: 0,
      sectionQuestionMap: {},
      autoSaveData: {},
    }
  },
  mounted(){
    this.formId = this.$route.params.formID;
    if (this.formId != '') {
      this.formId = Number(this.formId);
      this.getFormData()
     }
  },
  methods: {

    async getFormData() {
      this.loading = true;
      this.loadingMsg = "Loading form...";
      let vm = this;

      let autoSaveData = {};

      const [error, response] = await getFormDetails("00142091", this.formId);
      if (error) {
        console.error(error);

      } else {
        try {
          // ----------------------------------------------
          // -------------- FORM RENDER -------------------
          // ----------------------------------------------
          await Formio.createForm(document.getElementById('formio'), response).then(form => {
            // capture changes
            let debounceChanges = vm.debounce((changeEvent) => vm.autoSave(), 1000);

            // Prevent the submission from going to the form.io server.
            form.nosubmit = true;
            vm.form = form;

            // ----------------------------------------------
            // -------------- CHANGE EVENT ------------------
            // ----------------------------------------------
            form.on("change", function (changeEvent) {
              // key is the form S00Q00[_intv...]
              let componentKey = changeEvent.changed.component.key;

              console.log("Change event saving  %s %o", componentKey, changeEvent.changed.value);
              // todo - all this sort of logic should be moved to a service/util function

              if (componentKey.indexOf("intervention_checkbox") !== -1) {
                // ignore intervention checkboxes
              }
              else if (componentKey.indexOf("intervention") !== -1) {
                let questionKey = componentKey.substr(0, 6);
                let gridKey = questionKey + "_intervention_datagrid";
                let interventionTypeKey = questionKey + "_intervention_type";
                let grid = vm.form.getComponent(gridKey);
                // check that object has required values
                let gridData = vm.form.data[questionKey + "_intervention_datagrid"];

                //
                let changeGridDataEvent = changeEvent.changed;

                // grid was updated with a delete
                if (changeGridDataEvent.flags.isReordered) {
                  vm.saving = true;

                  // this is really ugly but the datagrid does not send delete events - just change event
                  // and the form data is already updated on the delete - so we have to send the list of interventions
                  // that are still present after a delete rather than sending a delete for whats removed 
                  let remainingInterventionTypes = changeGridDataEvent.value.map(intervention => intervention[interventionTypeKey]);
                  console.log("Types %o", remainingInterventionTypes);
                  try {
                    let error = deleteQuestionInterventionsExcept(csNumber, formId, questionKey, remainingInterventionTypes);
                    if (error) {
                      console.error("Failed to update intervention list %o", error);
                    }
                  } catch (err) {
                    console.error("Failed to delete interventions %o", err);
                  } finally {
                    vm.saving = false;

                  }

                } else {


                  gridData.forEach((entry) => {
                    let valid = true;

                    let desc = entry[questionKey + "_intervention_desc"];
                    let specify = entry[questionKey + "_intervention_specify"];
                    let type = entry[questionKey + "_intervention_type"];

                    // check if we have what we need
                    if (desc && (specify || type)) {

                      // if sending an intervention we must also send the question/comment values too 
                      // as interventions are actually tied to questions
                      if (!vm.autoSaveData[questionKey]) {
                        vm.autoSaveData[questionKey] = vm.form.data[questionKey];
                        vm.autoSaveData[questionKey + "_COMMENT"] = vm.form.data[questionKey + "_COMMENT"];

                      }

                      // add new array for grid saving
                      if (!vm.autoSaveData[gridKey]) {
                        vm.autoSaveData[gridKey] = [];
                      }
                      // make sure we only have one entry for each type
                      let current = vm.autoSaveData[gridKey].filter((existing) => existing[questionKey + "_intervention_type"] === type)[0];
                      if (!current) {
                        console.log("1 Adding intervention update %o", entry);
                        vm.autoSaveData[gridKey].push(entry);
                      } else {
                        // replace current entry
                        console.log("Updating intervention %o", entry);
                      }

                    }
                  });
                }


              } else if (componentKey.indexOf("_COMMENT") !== -1) {
                // comments have to go with the question
                let commentQuestion = componentKey.substr(0, 6);
                if (vm.form.data[commentQuestion]) {
                  vm.autoSaveData[commentQuestion] = vm.form.data[commentQuestion];
                  vm.autoSaveData[componentKey] = changeEvent.changed.value;
                }
              } else {
                let changedDate = {};
                let commentKey = componentKey + "_COMMENT";
                // if we send an answer with a blank comment it will be removed from the db as
                // its no different to the user blanking the comment
                if (vm.form.data[commentKey]) {
                  vm.autoSaveData[commentKey] = vm.form.data[commentKey];
                }
                vm.autoSaveData[componentKey] = changeEvent.changed.value;
              }

              if (Object.keys(vm.autoSaveData).length > 0) {
                vm.saving = true;

                debounceChanges(vm.autoSaveData);
              }

              vm.dataChangeCount++;

            });



            // ----------------------------------------------
            // -------------- FORM SUBMIT -------------------
            // ----------------------------------------------
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
            const [error, clientFormData] = await loadFormData(this.csNumber, this.formId);
            if (error) {
              vm.errorOccurred = true;
              vm.errorText = "Failed to load form data: " + error;
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
          vm.errorOccurred = true;
          vm.errorText = "Failed to render form: " + err;
        }





        // hide other sections
        this.hideSectionsExceptCurrent(0);


        this.totalNumParentNav = Object.keys(this.sectionQuestionMap).length;
        this.loading = false;
      }
    },
    debounce(func, timeout = 300) {
      let timer;
      return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => {
          func.apply(this, args);
        }, timeout);
      };
    },
    async autoSave() {
      this.saving = true;

      let formId = this.$route.params.formID;
      let csNumber = this.$route.params.csNumber;

      try {
        const [error, response] = await updateForm(csNumber, formId, this.autoSaveData);
        if (error) {

          console.error(error);
          this.errorOccurred = true;
          this.errorText = "Failed to save form: " + err;
        } 
      } catch (err) {
        console.error("Saving failed %o", err);
      } finally {
        this.saving = false;
      }
    },
    navToSectionAndQuestion(section: number, question: number) {
      console.log("Navigating %d %d", section, question);
      this.parentNavMoveToNext = section;
      this.currentSection = section;
      this.currentQuestion = question;
      this.hideSectionsExceptCurrent(section);
    },

    async handleSaveContinue(continueToNextSection) {

      let formId = this.$route.params.formID;
      let csNumber = this.$route.params.csNumber;
      // const [error, response] = await updateForm(csNumber, formId, this.form.data);
      // if (error) {
      //   console.error(error);
      // } else {
      //   console.log("Saved form %o", response);
      // }

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
      let formId = this.$route.params.formID;
      let csNumber = this.$route.params.csNumber;
      let vm = this;

      // hide all non-selected sections
      this.form.components.forEach(sectionComponent => {

        vm.displaySummary = false;


        let sectionNumber = Number.parseInt(sectionComponent.key.substr(1, 2)) - 1;
        if (sectionNumber !== sectionIdx) {
          sectionComponent.element.setAttribute('style', 'display:none');
        } else {
          sectionComponent.element.setAttribute('style', 'display:block');

          // see if this section is the summary section
          if (sectionComponent.component && sectionComponent.component.properties) {
            // is this a summary
            if (sectionComponent.component.properties['summary']) {
              sectionComponent.element.setAttribute('style', 'display:none');


              vm.showSummaryCounter++;
              console.log("Showing summary details %d", vm.showSummaryCounter);
              vm.displaySummary = true;
            }
          }
        }
      });
    },
    handleCancelForm() {
      console.log("Cancel Form");
      this.$emit('cancelFormClicked');
    },
    handleNavChildCallback(parentNavCurLocationFromChild, o) {
      console.log("Nav event %o %o, o", parentNavCurLocationFromChild);

      // get the section
      let section = this.form.components[parentNavCurLocationFromChild];
      console.log("Section %o", section);

      // hide other sections
      this.hideSectionsExceptCurrent(parentNavCurLocationFromChild);

      this.parentNavCurLocation = parentNavCurLocationFromChild;
      if (parentNavCurLocationFromChild == this.totalNumParentNav) {
        this.btnSaveContinueText = "Complete";
      } else {
        this.btnSaveContinueText = "Continue";
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
  padding: 20px 0 5px 0 !important;
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