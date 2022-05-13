<template>
    <div class="main">
      <div class="wrap">
        <div class="mainRow">
          <div class="column L">
            <div class="menu-Sticky">
              <div class="menuR1">
                <CrnaCmpFormInfo :formJSON="formInfo"/>
              </div>
              <div class="menuR2">
                <CrnaCmpFormNavigation :navHeaders="formJSONFormData"/>
              </div>
            </div>
            <div class="mainContent">
              <CrnaCmpFormDataEntry :navHeaders="formJSONFormData"></CrnaCmpFormDataEntry>            
            </div>
          </div>
          <div class="column R">
            <div class="R-Sticky">
              <CrnaCmpFormBtnGroup :formJSON="formBtnGroup" />
              <CrnaCmpFormStaticContent :formJSON="formJSONSideCards"/>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script lang="ts">

import { Component, Vue } from 'vue-property-decorator';
import {getFormDetails, getSideCardsDetails, getNavigationDetails} from "@/components/form.api.ts";
import CrnaCmpFormDataEntry from "@/components/crna-cmp/formSections/crnaCmpFormDataEntry.vue";
import CrnaCmpFormInfo from "@/components/crna-cmp/formSections/crnaCmpFormInfo.vue";
import CrnaCmpFormNavigation from "@/components/crna-cmp/formSections/crnaCmpFormNavigation.vue";
import CrnaCmpFormStaticContent from "@/components/crna-cmp/formSections/crnaCmpFormStaticContent.vue";
import CrnaCmpFormBtnGroup from "@/components/crna-cmp/formSections/crnaCmpFormBtnGroup.vue";

import sampleFormSideCards from './sampleData/sampleStatics.json';
import sampleFormData from './sampleData/sampleFormData.json';
import sampleFormInfo from './sampleData/sampleFormInfo.json';
import sampleFormBtnGroup from './sampleData/sampleBtnGroup.json';

export default {
  name: 'crnaForm',
  props: {
    formId: {
      type: Number,
      requred: false
    }
  },
  components: {
    CrnaCmpFormDataEntry,
    CrnaCmpFormNavigation,
    CrnaCmpFormStaticContent,
    CrnaCmpFormInfo,
    CrnaCmpFormBtnGroup
  },
  data() {
    return {
        formDetails: {},
        formJSONFormData: sampleFormData,
        formJSONSideCards: {},
        formInfo: sampleFormInfo,
        formBtnGroup: sampleFormBtnGroup
    }
  },
  mounted(){
    //this.getFormData()
    this.getFormSideCards()
  },
  methods: {
    async getFormData() {
      const [error, response] = await getFormDetails(this.formId);
      if (error) {
        console.error(error);
      } else {
        this.formDetails = response.data;
        // parse formJSONFormData to build dataEntry section
        //this.formJSONFormData

        // parse formJSONFormData to build formInfo setion
        //this.formInfo
        console.log("/forms/ returns: ");
        console.log(this.formDetails)
      }
    },
    async getFormSideCards() {
      const [error, response] = await getSideCardsDetails();
      if (error) {
        console.error(error);
      } else {
        this.formJSONSideCards = response;
        //console.log("formJSONSideCards content: ");
        //console.log(this.formJSONSideCards)
      }
    },
  }  
}
</script>