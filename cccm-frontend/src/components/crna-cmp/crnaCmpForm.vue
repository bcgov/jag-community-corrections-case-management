<template>
    <div class="main">
      <div class="wrap">
        <div class="mainRow">
          <div class="column L">
            <div class="menu-Sticky">
              <div class="menuR1">
                <FormioFormInfo :dataModel="formJSONFormData"/>
              </div>
              <div class="menuR2">
                <CrnaCmpFormNavigation @parentNavClicked="handleNavChildCallback" :dataModel="formJSONFormData" :parentNavMoveToNext="parentNavMoveToNext"/>
              </div>
            </div>
            <div class="mainContent">
              <CrnaCmpFormDataEntry :dataModel="formJSONFormData"></CrnaCmpFormDataEntry>   
              <!--Save and continue button group-->
              <div class="card card-body bg-light mb-3">
                <div class="row formio-component formio-component-columns formio-component-btnGroup  formio-component-label-hidden">
                  <div class="col-md-6 col-md-offset-0 col-md-push-0 col-md-pull-0">
                    <button id="btn_saveContinue" class="btn btn-primary btn-md" type="submit" @click="saveAndContinue">{{ btnSaveContinueText }}</button>
                  </div>
                  <div class="col-md-6 col-md-offset-0 col-md-push-0 col-md-pull-0">
                    <button id="btn_cancelForm" class="btn btn-danger btn-md" type="submit" @click="cancelForm">Cancel Form</button>
                  </div>
                </div>
              </div>         
            </div>
          </div>
          <div class="column R">
            <div class="R-Sticky">
              <!-- <FormioButton v-if="formJSONFormData.saveDraftBtnGroup != null" :dataModel="formJSONFormData.saveDraftBtnGroup" /> -->
              <!--Save draft button group-->
              <div class="card card-body bg-light mb-3">
                <div class="row formio-component formio-component-columns formio-component-btnGroup  formio-component-label-hidden">
                  <div class="col-md-6 col-md-offset-0 col-md-push-0 col-md-pull-0">
                    <button class="btn btn-primary btn-md" type="submit" @click="saveData">Save as Draft</button>
                  </div>
                  <div class="col-md-6 col-md-offset-0 col-md-push-0 col-md-pull-0">
                    <button class="btn btn-primary btn-md" type="submit" @click="printForm">Print Form</button>
                  </div>
                </div>
              </div>
              <CrnaCmpFormRightPanel :dataModel="formJSONFormData"/>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script lang="ts">

import { Component, Vue } from 'vue-property-decorator';
import {getFormDetails} from "@/components/form.api.ts";

import CrnaCmpFormDataEntry from "@/components/crna-cmp/formSections/crnaCmpFormDataEntry.vue";
import CrnaCmpFormNavigation from "@/components/crna-cmp/formSections/crnaCmpFormNavigation.vue";
import CrnaCmpFormRightPanel from "@/components/crna-cmp/formSections/crnaCmpFormRightPanel.vue";
import FormioButton from "@/components/common/FormioButton.vue";
import FormioFormInfo from "@/components/common/FormioFormInfo.vue";

import sampleFormData from './sampleData/sampleFormData.json';

export default {
  name: 'crnaForm',
  props: {
    formId: {
      type: Number,
      requred: false,
      default: 1
    }
  },
  components: {
    CrnaCmpFormDataEntry,
    CrnaCmpFormNavigation,
    CrnaCmpFormRightPanel,
    FormioButton,
    FormioFormInfo
  },
  data() {
    return {
        parentNavMoveToNext: 1,
        totalNumParentNav: 1,
        parentNavCurLocation: '0',
        btnSaveContinueText: "Save and Continue",
        formDetails: {},
        formJSONFormData: sampleFormData
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
        this.formDetails = response.data;
        this.totalNumParentNav = response.data.length;
        console.log("/forms/ returns: ", this.formDetails);
      }
    },
    handleNavChildCallback(parentNavCurLocationFromChild) {
      this.parentNavCurLocation = parentNavCurLocationFromChild;
      //console.log('parentNavCurLocation passed from ChildNav:', parentNavCurLocationFromChild);
      if (parentNavCurLocationFromChild == this.totalNumParentNav - 1) {
        this.btnSaveContinueText = "Submit Form"; 
      } else {
        this.btnSaveContinueText = "Save and Continue"; 
      }
    },
    saveData() {
      alert("Save data as draft");
    },
    printForm() {
      alert('Print Form.')
    },
    saveAndContinue() {
      if (this.parentNavCurLocation < this.totalNumParentNav - 1) {
        // have to make sure the value is different to trigger the child logic
        this.parentNavMoveToNext++;
      }
      
    },
    cancelForm() {
      alert('Cancel Form.')
    }
  }  
}
</script>