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
                <CrnaCmpFormNavigation :navHeaders="formJSONNavigation"/>
              </div>
            </div>
            <div class="mainContent">
              <CrnaCmpFormDataEntry :formJSON="formJSONDataEntry" :navHeaders="formJSONDataEntry"></CrnaCmpFormDataEntry>            
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

import sampleFormDataEntry from './sampleData/sampleDataEntry.json';
import sampleFormSideCards from './sampleData/sampleStatics.json';
import sampleFormNavigation from './sampleData/sampleNavigation.json';
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
        formJSONDataEntry: sampleFormNavigation,
        formJSONSideCards: {},
        formJSONNavigation: sampleFormNavigation,
        formInfo: sampleFormInfo,
        formBtnGroup: sampleFormBtnGroup
    }
  },
  mounted(){
    //this.buildFormInfoNDataEntry()
    this.getFormSideCards()
    //this.getFormNavigationContent()
  },
  methods: {
    async buildFormInfoNDataEntry() {
      const [error, response] = await getFormDetails(this.formId);
      if (error) {
        console.error(error);
      } else {
        this.formDetails = response.data;
        // parse formJSONDataEntry to build dataEntry section
        //this.formJSONDataEntry

        // parse formJSONDataEntry to build formInfo setion
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
        console.log("formJSONSideCards content: ");
        console.log(this.formJSONSideCards)
      }
    },
    async getFormNavigationContent() {
      const [error, response] = await getNavigationDetails();
      if (error) {
        console.error(error);
      } else {
        this.formJSONNavigation = response.data;
        console.log("formJSONNavigation content: ");
        console.log(this.formJSONNavigation)
      }
    },
  }  
}
</script>