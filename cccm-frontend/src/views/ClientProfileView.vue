<template>
  <clientProfile :key="key_clientprofile" :clientID="$route.params.id" :initData="clientProfile"/>
</template>

<script lang="ts">
import clientProfile from "@/components/common/FormioClientProfile.vue";
import {clientProfileSearch} from "@/components/form.api";

export default {
  name: "Clientprofile",
  data() {
    return {
      key_clientprofile: 0,
      clientProfile: {}
    }
  },
  components: {
    clientProfile
  },
  watch: {
    $route(to, from) {
      // react to route changes...
      console.log("to, from: ", to, from);

      // fetch the client profile
      this.handleClientProfileSearch();

      //force re-render the clientProfile component
      this.key_clientprofile++;
    }
  },
  // mounted: {
  //   this.handleClientProfileSearch();
  // },
  methods: {
    async handleClientProfileSearch() {
      const [error, response] = await clientProfileSearch(this.clientID);
      this.clientProfile = 
          {
            "id": "1",
            "fullName": "Ross, Bob",
            "clientAge": 44,
            "birthDate": "1979-12-03",
            "fullAddress": "123 Hello St, Victoria BC", 
            "addressType": "Work",
            "expired": "No",
            "csNumber": "123456789",
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