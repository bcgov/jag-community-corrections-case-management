<template>
  <div>
    <br>
    <v-row :key="theKey" align="center" justify="space-around">
      <div class="sectionTitleClass">Client Profile</div>
      <div :class="['fas fa-exclamation-triangle warrants', showWarrantDetails ? '' : 'center']" v-if="getNumOfWarrants !== 0" @click="showHideMoreWarrants">
        Client has {{getNumOfWarrants}} Outstanding Warrants 
        <div id="id_warrantDetails" :class="[showWarrantDetails ? 'show' : 'hide', '']">
          <div v-for="(item, index) in getWarrantDetails" :key="index">
            {{item.date}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.details}} <br>
          </div>
        </div>
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
    <v-row>
      <Form :form="formJSON" :submission="initData"/>
    </v-row>
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
            "key_fullName": "Ross, Bob",
            "key_csNumber": "123456780",
            "key_photo": "https://www.w3schools.com/images/lamp.jpg",
            "clientAge": 44,
            "key_datePhotoTaken": "2022-10-10",
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
            "key_designation": "GEN, IPV, SMO",
            "key_supervisionLevel": "High",
            "key_birthDate": "1979-12-03",
            "key_communityLocation": "Victoria",
            "key_communityStatus": "Active",
            "key_primaryCaseManager": "Smith, Bob",
            "key_secondaryCaseManagers": "Doe, Jane",
            "key_orders": "None",
            "key_nextConditionDueDate": "2022-03-04",
            "key_orderEffectiveDate": "2022-03-04",
            "key_finalOrderExpiryDate": "2022-03-05",
            "key_institution": "0543- Sunshine Coast Health Centre",
            "key_probDischargeDate": "2022-04-03",
            "key_institutionStatus": "Inactive",
            "key_custodyType": "Warrant of commital",
            "key_inOutCustody": "In (parole)",
            "key_paroleDate": "2022-03-04",
            "key_internalLocation": "0543 - Sunshine Coast Health Centre",
            "key_outLocation": "0543 - Sunshine Coast Health Centre",
            "key_federalParoleOffice": "0101 - Victoria Corrections",
            "key_outReason": "Sentence ended",
            "key_finalWarrantExpDate": "2022-05-04",
            "key_biometric": "No",
            "key_eReporting": "No",
            "key_biometricStatus": "Inactive",
            "key_eServices": "No",
            "fullAddress": "123 Hello St, Victoria BC", 
            "addressType": "Work",
            "expired": "No",
            "recordSealed": "Yes",
            "gender": "Male",
            "currentName": "Bob Ross",
            "location": "VICTORIA",
            "pcm": "Gillis, Mike",
            "photoURL": "https://www.w3schools.com/images/lamp.jpg", 
            "datePhotoTaken": "2022-03-02",
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
        // for (let i = 0; i < this.initData.data.outstandingWarrants.length; i++) {
        //   details += this.initData.data.outstandingWarrants[i].date;
        //   details += "       "
        //   details += this.initData.data.outstandingWarrants[i].details;
        //   details += "&#10;";
        // }
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
        // for (let i = 0; i < this.initData.data.communityAlerts.length; i++) {
        //   details += this.initData.data.communityAlerts[i].date;
        //   details += "&nbsp;&nbsp;&nbsp;&nbsp;"
        //   details += this.initData.data.communityAlerts[i].details;
        //   details += "&#10;";
        // }
      }
      return details;
    }
  }
}
</script>

<style >
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