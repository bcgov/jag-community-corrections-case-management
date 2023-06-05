<template>
  <div>
    <section class="mb-3 mt-3 pr-4 pl-4">
      <div class="row justify-content-between mb-2">
        <div class="col-sm-6">
          <h1 class="font-weight-bold">Client Record</h1>
        </div>
        <div :key="theKey" class="col-sm-4 text-right pr-4">
          <section class="row">
            <div class="col-sm-4" >
              <strong>Name</strong>
              <p>{{initData.data.clientName}}</p>
            </div>
            <div class="col-sm-4" >
              <strong>CS#</strong>
              <p>{{initData.data.clientNum}}</p>
            </div>
            <div class="col-sm-4" >
              <strong>Date of Birth</strong>
              <p>{{initData.data.birthDate}}</p>
            </div>
          </section>
        </div>
      </div>
    </section>
    <section :key="tabKey" class="pr-4 pl-4">
      <v-tabs v-model="current_tab" fixed-tabs color="deep-purple accent-4">
        <v-tab v-for="item in showTabs" :key="item.tab" :href="'#tab-' + item.id"> 
          {{ item.tab }}
        </v-tab>
      </v-tabs>
      <v-tabs-items v-model="current_tab">
        <v-tab-item v-for="item in showTabs" :key="item.tab" :id="'tab-' + item.id">
          <div v-if="item.id === 'cp'" class="p-4">
            <section class="mb-3">
              <v-row :key="theKey" class="row">
                <div class="sectionTitleClass mr-4 col-3 font-weight-bold">Community Profile</div>
                <div :class="['bg-shallow-critical mr-3 mt-3 col-3 align-items-start', showAlertDetails ? 'h-100' : 'center']" v-if="getNumOfAlerts !== 0" @click="showHideMoreAlerts">
                  <div class="w-100 d-flex justify-content-between align-content-center pl-1 pr-1">
                    <span class="fas fa-exclamation-triangle mt-1"/>
                    <h5 class="font-weight-bold">Client has {{getNumOfAlerts}} Community Alerts</h5>
                    <span class="ml-2 d-flex flex-column center align-self-start pr-2" style="margin-top: -5px">
                      <p class="m-0 p-0 small font-weight-bold text-dark">View</p>
                      <i :class="[ showWarrantDetails ? 'fa fa-chevron-up' : 'fa fa-chevron-down', 'text-dark']" style="margin-top:-5px" />
                    </span>
                  </div>
                  <div id="id_alertDetails" :class="[showAlertDetails ? 'show' : 'hide']">
                    <div v-for="(item, index) in getAlertDetails" :key="index">
                      {{item.date}}&nbsp;&nbsp;{{item.comment}}
                    </div>
                  </div>
                </div>
                <div class="bg-shallow-critical text-left mr-3 mt-3 pt-0 col-2 d-flex flex-column" v-if="isProfileClosed">
                  <p class="p-0 m-0">Profile is closed</p>
                  <p class="p-0 m-0   small">Client has been released.</p>
                </div>
              </v-row>
            </section>
            <Form :form="formJSON" :submission="initData"/>
          </div>
          <RNAListView :key="theKey" v-if="item.id === 'rl'" :clientNum="$route.params.csNumber" :IPVClient="IPVClient" :SMOClient="SMOClient"></RNAListView>
          <TrendAnalysisView v-if="showTrendAnalysis && item.id === 'ta'" :clientNum="$route.params.csNumber"  :clientID="$route.params.clientID"></TrendAnalysisView>
          <span v-else> </span>
        </v-tab-item>
      </v-tabs-items>
    </section>

  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import RNAListView from '@/components/RNAList.vue';
import {clientProfileSearch} from "@/components/form.api";
import TrendAnalysisView from "@/components/trendanalysis/TrendAnalysisView.vue";
import templateClientProfile from '@/components/common/templateClientProfile.json';
import {useStore} from "@/stores/store";
import {mapStores} from 'pinia';

export default {
  name: "FormioClientRecord",
  data() {
    return {
      CONST_DESIGNATION_LOW: 'low',
      CONST_DESIGNATION_IPV: 'ipv',
      CONST_DESIGNATION_SMO: 'smo',
      theKey: 0,
      tabKey: 0,
      current_tab: 'tab-cp',
      items: [
          { tab: 'Community Profile', id: 'cp' },
          { tab: 'Trend Analysis', id: 'ta' },
          { tab: 'RNA List', id: 'rl' }
        ],
      items_noTrend: [
          { tab: 'Community Profile', id: 'cp' },
          { tab: 'RNA List', id: 'rl' }
        ],
      initData: {"data": {}},
      formJSON: templateClientProfile,
      showWarrantDetails: false,
      showAlertDetails: false,
      csNumber: null,
      IPVClient: false,
      SMOClient: false
    }
  },
  components: {
    Form,
    RNAListView,
    TrendAnalysisView
},
  mounted() {
    this.csNumber = this.$route.params.csNumber;
    this.current_tab = this.$route.params.tabIndex;
    this.clientProfileSearchAPI();
  },
  methods: {
    showHideMoreWarrants() {
      if (this.showWarrantDetails) {
        this.showWarrantDetails = false;
      } else {
        this.showWarrantDetails = true;
      }
    },
    showHideMoreAlerts() {
      if (this.showAlertDetails) {
        this.showAlertDetails = false;
      } else {
        this.showAlertDetails = true;
      }
    },
    async clientProfileSearchAPI() {
      // Client profile search.
      const [error, clientProfileResponse] = await clientProfileSearch(this.csNumber);
      if (error) {
        console.error("Failed searching for client profile: ", error);
      } else {
        this.initData = {"data": {}};
        this.initData.data = clientProfileResponse;
        if (clientProfileResponse != null) {
          //Cache the photoData into this.initData object
          if (clientProfileResponse.photo) {
            this.initData.data.photo.image = "data:image/png;base64, " + clientProfileResponse.photo.image;
            this.initData.data.photo.photoTakenDate = clientProfileResponse.photo.photoTakenDate;
          }
          // Build the designations value, set the IPVClient and SMOClient value
          if (clientProfileResponse.designations != null) {
            let designationsVal = "";
            for (let i = 0; i < clientProfileResponse.designations.length; i++) {
              if (clientProfileResponse.designations[i].type.toLowerCase() === this.CONST_DESIGNATION_IPV.toLowerCase()) {
                this.IPVClient = true;
              }
              if (clientProfileResponse.designations[i].type.toLowerCase() === this.CONST_DESIGNATION_SMO.toLowerCase()) {
                this.SMOClient = true;
              }
              // (Sep 21, 2022) BA decided to get ride the color scheme for the designation 
              //let colorClass = this.getDesignationColor(this.initData.data.designations[i].rating);
              let colorClass = "";
              let theVal = "<span class='" + colorClass + "'>" + clientProfileResponse.designations[i].type + "</span></br>";
              designationsVal += theVal;
            }
            this.initData.data.designationsVal = designationsVal;
          }
        }
        this.theKey++;
      }
    },
    getDesignationColor(rating) {
      if (rating == null || rating == '' || rating.toLowerCase() == this.CONST_DESIGNATION_LOW) {
        return "";
      }
      return "critical";
    }
  },
  computed: {
    showTrendAnalysis() {
      return this.mainStore.isShowTrendAnalysis();
    },
    showTabs() {
      if (this.mainStore.isShowTrendAnalysis()) {
        return this.items;
      }
      return this.items_noTrend;
    },
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore),
    getNumOfAlerts() {
      let numAlerts = 0;
      if (this.initData != null && this.initData.data != null && this.initData.data.communityAlerts != null) {
        numAlerts = this.initData.data.communityAlerts.length;
      }
      return numAlerts;
    },
    getAlertDetails() {
      let details = [];
      if (this.initData != null && this.initData.data != null && this.initData.data.communityAlerts != null) {
        details = this.initData.data.communityAlerts;
      }
      return details;
    },
    isProfileClosed() {
      let closed = false;
      if (this.initData != null && this.initData.data != null) {
        closed = this.initData.data.profileClosed;
      }
      return closed;
    }
  }
}
</script>

<style>
  .critical {
    font-size: 15px;
    font-weight: bold;
    background-color: rgb(243, 195, 195);
    color: rgb(255, 0, 0);
    line-height: 1.6;
  }

  .warrants {
    font-size: 15px;
    font-weight: bold;
    background-color: rgb(205, 236, 103);
    line-height: 1.6;
  }

  .center {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .profilePhoto {
    text-align: center;
    max-width: 100%;
  }
</style>