<template>
    <div class="main">
      <div class="wrap">
        <div class="row">
          <div class="column L">
            <div class="menu-Sticky">
              <CrnaCmpFormInfo :formJSON="formInfo"/>
              <CrnaCmpFormNavigation :formJSON="formJSONNavigation"/>
              <CrnaCmpFormDataEntry :formJSON="getDataEntryFormData" ></CrnaCmpFormDataEntry>
            </div>
          </div>
          <div class="column R">
            <div class="R-Sticky">
              <CrnaCmpFormBtnGroup :formJSON="formBtnGroup" />
              <CrnaCmpFormStaticContent :formJSON="formJSONStatics"/>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script lang="ts">

import { Component, Vue } from 'vue-property-decorator';
import CrnaCmpFormDataEntry from "@/components/crnaCmpFormDataEntry.vue";
import CrnaCmpFormInfo from "@/components/crnaCmpFormInfo.vue";
import CrnaCmpFormNavigation from "@/components/crnaCmpFormNavigation.vue";
import CrnaCmpFormStaticContent from "@/components/crnaCmpFormStaticContent.vue";
import CrnaCmpFormBtnGroup from "@/components/crnaCmpFormBtnGroup.vue";
import axios from 'axios';
import sampleFormDataEntry from './sampleDataEntry.json';
import sampleFormStatics from './sampleStatics.json';
import sampleFormNavigation from './sampleNavigation.json';
import sampleFormInfo from './sampleFormInfo.json';
import sampleFormBtnGroup from './sampleBtnGroup.json';

export default {
  name: 'crnaForm',
  props: {
    
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
        formJSONDataEntry: sampleFormDataEntry,
        formJSONStatics: sampleFormStatics,
        formJSONNavigation: sampleFormNavigation,
        formInfo: sampleFormInfo,
        formBtnGroup: sampleFormBtnGroup,
        crna_cmp_form_dataEntry_endpoint: "https://hcydsplculhonzk.form.io/demoform1",
        crna_cmp_form_statics_endpoint: "https://hcydsplculhonzk.form.io/demoform",
        crna_cmp_form_navigation_endpoint: "https://hcydsplculhonzk.form.io/demoform1",
        periodic_check_milliseconds: 5000,
    }
  },
  mounted(){
    //this.getFormDataEntryContent()
    //this.getFormStaticsContent()
    //this.getFormNavigationContent()
  },
  methods: {
    getFormDataEntryContent() {
      axios.get(this.getServiceEndpoint_DataEntry)
      .then( response => {
        this.formJSONDataEntry = response.data;
        console.log("formJSONDataEntry content: ");
        console.log(this.formJSONDataEntry)
      }).catch(
          error => (console.log(error))
      )
      
    },
    getFormStaticsContent() {
      axios.get(this.crna_cmp_form_statics_endpoint)
      .then( response => {
        this.formJSONStatics = response.data;
        console.log("formJSONStatics content: ");
        console.log(this.formJSONStatics)
      }).catch(
          error => (console.log(error))
      )
      return null
    },
    getFormNavigationContent() {
      axios.get(this.crna_cmp_form_navigation_endpoint)
      .then( response => {
        this.formJSONNavigation = response.data;
        console.log("formJSONNavigation content: ");
        console.log(this.formJSONNavigation)
      }).catch(
          error => (console.log(error))
      )
      return null
    },
  },
  computed: {
    getDataEntryFormData() {
        console.log("getDataEntryFormData: ");
        console.log(this.formJSONDataEntry);
        return this.formJSONDataEntry;
    },
    getServiceEndpoint_DataEntry() {
      return this.crna_cmp_form_dataEntry_endpoint;
    },
    getServiceEndpoint_Statics() {
      return this.crna_cmp_form_statics_endpoint;
    },
    getServiceEndpoint_Navigation() {
      return this.crna_cmp_form_navigation_endpoint;
    }
  }  
}
</script>