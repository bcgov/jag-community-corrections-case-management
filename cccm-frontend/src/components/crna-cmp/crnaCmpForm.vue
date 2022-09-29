<template>
    <div class="main">
      <div class="wrap">
        <div class="mainRow">
          <div class="column L">
            <div class="menu-Sticky">
              <div class="menuR1">
                <Form :key="componentKey" :form="data_formInfo" />
              </div>
              <div class="menuR2">
                <CrnaCmpFormNavigation :key="componentKey" :dataModel="formJSONFormData" @parentNavClicked="handleNavChildCallback" :parentNavMoveToNext="parentNavMoveToNext"/>
              </div>
            </div>
            <div class="mainContent">
              <CrnaCmpFormDataEntry :key="componentKey" :dataModel="data_formEntries" :initData="initData" 
                  :dataMap="formJSONFormData.dataMap" :saveBtnLabel="btnSaveContinueText" :notifySaveDraft="notifySaveDraft"
                  @saveContinueClicked="handleSaveContinue" @cancelFormClicked="handleCancelForm"/>   
            </div>
          </div>
          <div class="column R">
            <div class="R-Sticky">
              <section class="crna-right-sticky-panel">
                <div class="crna-right-panel-button-container">
                  <Form :key="componentKey" :form="data_rightPanel_buttonGroup" @evt_saveDraft="handleSaveDraft" @evt_print="handlePrint" />
                </div>
                <div class="crna-right-panel-details">
                  <Form :key="componentKey" :form="data_rightPanel" />
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
import { getFormDetails, updateForm, loadFormData, loadFormDataForSectionSeq, deleteQuestionInterventionsExcept } from "@/components/form.api";
import CrnaCmpFormDataEntry from "@/components/crna-cmp/formSections/crnaCmpFormDataEntry.vue";
import CrnaCmpFormNavigation from "@/components/crna-cmp/formSections/crnaCmpFormNavigation.vue";
import sampleFormData from './sampleData/crnaSampleData.json';

export default {
  name: 'crnaForm',
  components: {
    Form,
    CrnaCmpFormDataEntry,
    CrnaCmpFormNavigation
  },
  data() {
    return {
      formId: -1,
      csNumber: '',
      notifySaveDraft: 0,
      parentNavMoveToNext: 1,
      totalNumParentNav: 1,
      parentNavCurLocation: '0',
      btnSaveContinueText: "Save and Continue",
      formJSONFormData: sampleFormData,
      componentKey: 0,
      data_rightPanel: {"display": "form"},
      data_rightPanel_buttonGroup: {"display": "form"},
      data_formEntries: {"display": "form"},
      data_formInfo: {"display": "form"},
      data_buttonGroup: {},
      initData: {},
      dataMap: {}
    }
  },
  mounted(){
    this.formId = this.$route.params.formID;
    this.csNumber = this.$route.params.csNumber;
    this.getFormDetails();
  },
  methods: {
    async getFormDetails() {
      // Load formio template
      const [error, response] = await getFormDetails(this.csNumber, this.formId);
      if (error) {
        console.error(error);
      } else {
        //this.private_process_formData(response);
        this.data_formEntries = response;
        // Load form data
        const [error, clientFormData] = await loadFormData(this.csNumber, this.formId);
        if (error) {
          console.error(error);
        } else {
          this.initData = clientFormData;
        }
      }
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
    handlePrint(evt) {
      if (evt != null && evt.type === 'evt_print' ) {
        console.log("handlePrint: ");
      } 
    },
    private_process_formData(response) {
      //console.log("Form payload: ", response);
      //this.formJSONFormData = response;
      const formInfo = this.formJSONFormData.components.filter(obj => {
        return obj.key === 'section_formInfo';
      });
      this.data_formInfo = formInfo[0];

      const formdata = this.formJSONFormData.components.filter(obj => {
        return obj.key === 'section_data';
      });
      this.data_formEntries = formdata[0];
      //console.log("this.data_formEntries: ", this.data_formEntries);

      this.data_rightPanel.components = this.formJSONFormData.components.filter(obj => {
        return obj.key === 'section_rightpanel';
      });
      //console.log("this.data_rightPanel: ", this.data_rightPanel);

      this.data_rightPanel_buttonGroup.components = this.formJSONFormData.components.filter(obj => {
        return obj.key === 'buttonGroup_rightPanel';
      });
      
      this.totalNumParentNav = this.data_formEntries.components.length - 1;
      //console.log("totalNumParentNav: ", this.totalNumParentNav);
      this.componentKey++;
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