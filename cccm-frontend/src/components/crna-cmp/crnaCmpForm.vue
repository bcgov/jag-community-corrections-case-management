<template>
    <div class="main">
      <div class="wrap">
        <div class="mainRow">
          <div class="column L">
            <div class="menu-Sticky">
              <div class="menuR1">
                <CrnaCmpFormInfo :dataModelFormInfo="formJSONFormData" :key="componentKey"/>
              </div>
              <div class="menuR2">
                <CrnaCmpFormNavigation @parentNavClicked="handleNavChildCallback" :dataModel="formJSONFormData" :parentNavMoveToNext="parentNavMoveToNext" :key="componentKey"/>
              </div>
            </div>
            <div class="mainContent">
              <CrnaCmpFormDataEntry :dataModel="formJSONFormData" :key="componentKey" :notifySavingData="notifySavingData"></CrnaCmpFormDataEntry>
              <!--Save and continue button group-->
              <FormioButton :dataModel="formJSONFormData.buttonGroupSaveCancel" @saveContinueClicked="handleSaveContinue" @cancelFormClicked="handleCancelForm"/>
            </div>
          </div>
          <div class="column R">
            <div class="R-Sticky">
              <br/>
              <!--Save draft button group-->
              <FormioButton :dataModel="formJSONFormData.buttonGroupSavePrint" @saveContinueClicked="handleSaveContinue" @printFormClicked="handlePrintForm"/>
              <CrnaCmpFormRightPanel :dataModel="formJSONFormData" :key="componentKey"/>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script lang="ts">

import { Component, Vue } from 'vue-property-decorator';
import {getFormDetails} from "@/components/form.api";

import CrnaCmpFormDataEntry from "@/components/crna-cmp/formSections/crnaCmpFormDataEntry.vue";
import CrnaCmpFormNavigation from "@/components/crna-cmp/formSections/crnaCmpFormNavigation.vue";
import CrnaCmpFormRightPanel from "@/components/crna-cmp/formSections/crnaCmpFormRightPanel.vue";
import CrnaCmpFormInfo from "@/components/crna-cmp/formSections/crnaCmpFormInfo.vue";
import FormioButton from "@/components/common/FormioButtons.vue";

import sampleFormData from './sampleData/sampleFormData.json';

export default {
  name: 'crnaForm',
  props: {
    formId: {
      type: Number,
      required: false,
      default: 1
    }
  },
  components: {
    CrnaCmpFormDataEntry,
    CrnaCmpFormNavigation,
    CrnaCmpFormRightPanel,
    CrnaCmpFormInfo,
    FormioButton,
  },
  data() {
    return {
      message: "",
      parentNavMoveToNext: 1,
      notifySavingData: 1,
      totalNumParentNav: 1,
      parentNavCurLocation: '0',
      btnSaveContinueText: "Save and Continue",
      formJSONFormData: sampleFormData,
      componentKey: 0
    }
  },
  mounted(){
    this.getFormData()
  },
  methods: {
    async getFormData() {
      const [error, response] = await getFormDetails(this.formId);
      if (error) {
        console.error(error);
      } else {
        //this.formJSONFormData = response;
        this.totalNumParentNav = this.formJSONFormData.data.length;
        //console.log("totalNumParentNav: ", this.totalNumParentNav);
        this.componentKey += 1;
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

  text-decoration: underline;
  -webkit-text-decoration-color: rgb(255, 208, 0); /* Safari */
  text-decoration-color: rgb(255, 208, 0);
}

.crna-subSectionTitleClass:nth-child(1):after {
  content: "";
  display: block;
  background: #c00;
  height: 18px;
  width: 50px;

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
</style>