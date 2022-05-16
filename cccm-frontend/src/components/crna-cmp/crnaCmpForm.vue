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
                <CrnaCmpFormNavigation :dataModel="formJSONFormData"/>
              </div>
            </div>
            <div class="mainContent">
              <CrnaCmpFormDataEntry :dataModel="formJSONFormData"></CrnaCmpFormDataEntry>            
            </div>
          </div>
          <div class="column R">
            <div class="R-Sticky">
              <CrnaCmpFormBtnGroup />
              <CrnaCmpFormSideCards :dataModel="formJSONFormData"/>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script lang="ts">

import { Component, Vue } from 'vue-property-decorator';
import {getFormDetails, getNavigationDetails} from "@/components/form.api.ts";
import CrnaCmpFormDataEntry from "@/components/crna-cmp/formSections/crnaCmpFormDataEntry.vue";
import CrnaCmpFormNavigation from "@/components/crna-cmp/formSections/crnaCmpFormNavigation.vue";
import CrnaCmpFormSideCards from "@/components/crna-cmp/formSections/crnaCmpFormSideCards.vue";
import CrnaCmpFormBtnGroup from "@/components/common/FormioDraftBtnGroup.vue";
import FormioFormInfo from "@/components/common/FormioFormInfo.vue";

import sampleFormData from './sampleData/sampleFormData.json';

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
    CrnaCmpFormSideCards,
    CrnaCmpFormBtnGroup,
    FormioFormInfo
  },
  data() {
    return {
        formDetails: {},
        formJSONFormData: sampleFormData
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
        this.formDetails = response.data;
        
        console.log("/forms/ returns: ");
        console.log(this.formDetails)
      }
    },
  }  
}
</script>