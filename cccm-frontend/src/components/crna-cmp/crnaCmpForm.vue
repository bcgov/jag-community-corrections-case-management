<template>
    <div class="main">
      <div class="wrap">
        <form id="formID" @submit.prevent="submit">
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
                <CrnaCmpFormDataEntry :dataModel="formJSONFormData" :key="componentKey"></CrnaCmpFormDataEntry>   
                <!--Save and continue button group-->
                <div class="card card-body bg-light mb-3">
                  <div class="row formio-component formio-component-columns formio-component-btnGroup  formio-component-label-hidden">
                    <div class="col-md-6 col-md-offset-0 col-md-push-0 col-md-pull-0">
                      <button id="btn_saveContinue" class="btn btn-primary btn-md" type="submit" >{{ btnSaveContinueText }}</button>
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
                <!--Save draft button group-->
                <div class="card card-body bg-light mb-3">
                  <div class="row formio-component formio-component-columns formio-component-btnGroup  formio-component-label-hidden">
                    <div class="col-md-6 col-md-offset-0 col-md-push-0 col-md-pull-0">
                      <button id="btn_saveDraft" class="btn btn-primary btn-md" type="submit" >Save as Draft</button>
                    </div>
                    <div class="col-md-6 col-md-offset-0 col-md-push-0 col-md-pull-0">
                      <button id="btn_printForm" class="btn btn-primary btn-md" type="submit" @click="printForm">Print Form</button>
                    </div>
                  </div>
                </div>
                <CrnaCmpFormRightPanel :dataModel="formJSONFormData" :key="componentKey"/>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
</template>

<script lang="ts">

import { Component, Vue } from 'vue-property-decorator';
import {getFormDetails, saveFormData} from "@/components/form.api.ts";

import CrnaCmpFormDataEntry from "@/components/crna-cmp/formSections/crnaCmpFormDataEntry.vue";
import CrnaCmpFormNavigation from "@/components/crna-cmp/formSections/crnaCmpFormNavigation.vue";
import CrnaCmpFormRightPanel from "@/components/crna-cmp/formSections/crnaCmpFormRightPanel.vue";
import CrnaCmpFormInfo from "@/components/crna-cmp/formSections/crnaCmpFormInfo.vue";

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
    CrnaCmpFormInfo,
  },
  data() {
    return {
      message: "",
      parentNavMoveToNext: 1,
      totalNumParentNav: 1,
      parentNavCurLocation: '0',
      btnSaveContinueText: "Save and Continue",
      formJSONFormData: sampleFormData,
      componentKey: 0,
    }
  },
  mounted(){
    //this.getFormData()
  },
  methods: {
    async getFormData() {
      const [error, response] = await getFormDetails(this.formId);
      if (error) {
        console.error(error);
      } else {
        this.formJSONFormData = response;
        this.totalNumParentNav = response.data.length;
        console.log("/forms/ returns: ", this.formJSONFormData);
        this.componentKey += 1;
      }
    },
    handleNavChildCallback(parentNavCurLocationFromChild) {
      this.parentNavCurLocation = parentNavCurLocationFromChild;
      if (parentNavCurLocationFromChild == this.totalNumParentNav - 1) {
        this.btnSaveContinueText = "Submit Form"; 
      } else {
        this.btnSaveContinueText = "Save and Continue"; 
      }
    },
    async submit(evt) {
      // if clicking "Save and Continue" button, increment this.parentNavCurLocation to navigate to the next section
      if (evt.submitter.id == 'btn_saveContinue') {
        if (this.parentNavCurLocation < this.totalNumParentNav - 1) {
          // have to make sure the value is different to trigger the child logic
          this.parentNavMoveToNext++;
        }
      }
      
      //Obtain the form payload
      const payload = Object.fromEntries(new FormData(evt.target));
      
      // call backend to save the data
      const [error, response] = await saveFormData(payload);
      if (error) {
        console.error("SaveFormData error", error);
      } else {
        console.log("SaveFormData success", response);
      }
    },
    printForm() {
      alert('Print Form.')
    },
    cancelForm() {
      alert('Cancel Form.')
    }
  }  
}
</script>