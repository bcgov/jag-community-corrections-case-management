<template>
  <div>
    <br>
    <v-row :key="theKey">
      <div class="sectionTitleClass">&nbsp;&nbsp;&nbsp;&nbsp;Client Profile</div>
      <div :class="['fas fa-exclamation-triangle warrants', showWarrantDetails ? '' : 'center']" v-if="getNumOfWarrants !== 0" @click="showHideMoreWarrants">
        Client has {{getNumOfWarrants}} Outstanding Warrants 
        <div id="id_warrantDetails" :class="[showWarrantDetails ? 'show' : 'hide', '']">
          <div v-for="(item, index) in getWarrantDetails" :key="index">
            {{item.date}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.details}} <br>
          </div>
        </div>
      </div>
      <div class="critical center" v-if="isProfileClosed">
        Profile is closed <br>
        Client has been released
      </div>
      <div :class="['fas fa-exclamation-triangle critical', showAlertDetails ? '' : 'center']" v-if="getNumOfAlerts !== 0" @click="showHideMoreAlerts">
        Client has {{getNumOfAlerts}} Community Alerts
        <div id="id_alertDetails" :class="[showAlertDetails ? 'show' : 'hide']">
          <div v-for="(item, index) in getAlertDetails" :key="index">
            {{item.date}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.details}} <br>
          </div>
        </div>
      </div>
    </v-row>
    <br/><br/><br/>
    <Form :form="formJSON" :submission="initData"/>
    
  </div>
  
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import {clientProfileSearch} from "@/components/form.api";
import templateClientProfile from '@/components/common/templateClientProfile.json';
import { kill } from 'process';

export default {
  name: "FormioClientprofile",
  props: {
    csNum: 0
  },
  data() {
    return {
      theKey: 0,
      formJSON: templateClientProfile,
      initData: {},
      showWarrantDetails: false,
      showAlertDetails: false,
    }
  },
  components: {
    Form
  },
  mounted(){
    this.clientProfileSearch(this.csNum);
    this.theKey++;
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
    async clientProfileSearch(csNum) {
      const [error, response] = await clientProfileSearch(csNum);
      //this.initData = response.data;
      this.initData = {"data": {}};
      this.initData.data = 
        {
            "id": "1",
            "fullName": "Ross, Bob",
            "csNumber": "123456780",
            "photo": "https://www.w3schools.com/images/lamp.jpg",
            "clientAge": 44,
            "datePhotoTaken": "2022-10-10",
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
            "photoURL": "https://www.w3schools.com/images/lamp.jpg", 
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
    }
  },
  computed: {
    getNumOfWarrants() {
      let numWarrants = 0;
      if (this.initData != null && this.initData.data != null && this.initData.data.outstandingWarrants != null) {
        numWarrants = this.initData.data.outstandingWarrants.length;
      }
      return numWarrants
    },
    getWarrantDetails() {
      let details = [];
      if (this.initData != null && this.initData.data != null && this.initData.data.outstandingWarrants != null) {
        return this.initData.data.outstandingWarrants;
      }
      return details;
    },
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
}
</style>