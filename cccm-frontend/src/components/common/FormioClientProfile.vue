<template>
  <Form :form="formJSON" :submission="initData"/>
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
    }
  },
  components: {
    Form
  },
  mounted(){
    this.clientProfileSearch(this.csNum);
  },
  methods: {
    async clientProfileSearch(csNum) {
      const [error, response] = await clientProfileSearch(csNum);
      //this.initData = response.data;
      this.initData = {"data": {}};
      this.initData.data = 
        {
            "id": "1",
            "key_fullName": "Ross, Bob",
            "key_csNumber": "123456780",
            "clientAge": 44,
            "key_datePhotoTaken": "2022-10-10",
            "key_numOfWarrants": 3,
            "key_numOfAlerts": 3,
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
  }
}
</script>