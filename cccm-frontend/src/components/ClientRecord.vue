<template>
  <div>
    <section class="mb-3">
      <div class="row justify-content-between mb-2">
        <div class="col-sm-6">
          <h1 class="font-weight-bold">Client Record</h1>
        </div>
        <div :key="theKey" class="col-sm-3 text-right pr-4">
          <section class="row">
            <div class="col-sm-4" >
              <strong>Name</strong>
              <p>{{initData.fullName}}</p>
            </div>
            <div class="col-sm-4" >
              <strong>CS#</strong>
              <p>{{initData.csNumber}}</p>
            </div>
            <div class="col-sm-4" >
              <strong>Date of Birth</strong>
              <p>{{initData.birthDate}}</p>
            </div>
          </section>
        </div>
      </div>
    </section>
    <v-tabs v-model="tab" fixed-tabs color="deep-purple accent-4">
      <v-tab v-for="item in items" :key="item.tab">
        {{ item.tab }}
      </v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab">
      <v-tab-item v-for="item in items" :key="item.tab">
        <FormioClientProfile v-if="item.content === 'cp'" :csNumber="$route.params.csNumber" :clientID="$route.params.clientID"></FormioClientProfile>
        <RNAListView v-if="item.content === 'rl'" :clientNum="$route.params.csNumber"  :clientID="$route.params.clientID"></RNAListView>
        <TrendAnalysisView v-if="item.content === 'ta'" :clientNum="$route.params.csNumber"  :clientID="$route.params.clientID"></TrendAnalysisView>
        <span v-else> {{item.content}}</span>
      </v-tab-item>
    </v-tabs-items>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import FormioClientProfile from "@/components/common/FormioClientProfile.vue";
import RNAListView from '@/components/RNAList.vue';
import {clientProfileSearch} from "@/components/form.api";
import TrendAnalysisView from "./trendanalysis/TrendAnalysisView.vue";

export default {
  name: "FormioClientRecord",
  data() {
    return {
      theKey: 0,
      tab: null,
      items: [
          { tab: 'Community Profile', content: 'cp' },
          { tab: 'Trend Analysis', content: 'ta' },
          { tab: 'RNA List', content: 'rl' },
          { tab: 'Intervention Summary', content: 'is' },
          
        ],
      initData: {},
      theKey: 0
    }
  },
  components: {
    FormioClientProfile,
    RNAListView,
    TrendAnalysisView
},
  mounted() {
    this.clientProfileSearchAPI();
  },
  methods: {
    async clientProfileSearchAPI() {
      const [error, response] = await clientProfileSearch(this.$route.params.csNumber);
      //this.initData = response.data;
      this.initData = {};
      this.initData = 
        {
            "id": "1",
            "fullName": "Ross, Bob",
            "csNumber": "00142091",
            "clientAge": 44,
            "datePhotoTaken": "2022-10-10",
            "photo": "abc",
            "profileClosed": false,
            "communityAlerts": [
              {
                "date": "2022-01-02",
                "details": "Client threatened staff"
              },
              {
                "date": "2022-03-02",
                "details": "Client brought knife to meeting"
              },
              {
                "date": "2022-04-02",
                "details": "Client attacked staff"
              }
            ],
            "outstandingWarrants": [
              {
                "date": "2022-01-02",
                "details": "Client threatened staff"
              },
              {
                "date": "2022-03-02",
                "details": "Client brought knife to meeting"
              },
              {
                "date": "2022-04-02",
                "details": "Client attacked staff"
              }
            ],
            "designation": "GEN, IPV, SMO",
            "supervisionLevel": "High",
            "birthDate": "1979-12-03",
            "communityLocation": "Victoria",
            "communityStatus": "Active",
            "primaryCaseManager": "Smith, Bob",
            "secondaryCaseManagers": "Doe, Jane",
            "orders": "None",
            "nextConditionDueDate": "2022-03-04",
            "orderEffectiveDate": "2022-03-04",
            "finalOrderExpiryDate": "2022-03-05",
            "institution": "0543- Sunshine Coast Health Centre",
            "probDischargeDate": "2022-04-03",
            "institutionStatus": "Inactive",
            "custodyType": "Warrant of commital",
            "inOutCustody": "In (parole)",
            "paroleDate": "2022-03-04",
            "internalLocation": "0543 - Sunshine Coast Health Centre",
            "outLocation": "0543 - Sunshine Coast Health Centre",
            "federalParoleOffice": "0101 - Victoria Corrections",
            "outReason": "Sentence ended",
            "finalWarrantExpDate": "2022-05-04",
            "biometric": "No",
            "eReporting": "No",
            "biometricStatus": "Inactive",
            "eServices": "No",
            "fullAddress": "123 Hello St, Victoria BC", 
            "addressType": "Work",
            "expired": "No",
            "recordSealed": "Yes",
            "gender": "Male",
            "currentName": "Bob Ross",
            "location": "VICTORIA",
            "pcm": "Gillis, Mike",
            "address": [
                {
                    "street": "123 Hello St",
                    "city": "Victoria",
                    "postalCode": "123 abc"
                }
            ]
        };

      if (error) {
        console.error(error);
      }
      this.theKey++;
    }
  }
}
</script>

<style>
</style>