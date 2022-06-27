<template>
  <div id="app">
    <!--Header section-->
    <HeaderComponent :locationInfo="locationVal" />

    <!--Body section-->
    <router-view />

    <!--Footer section-->
    <FooterComponent />
  </div>
</template>

<script lang="ts">
import Vue from 'vue';
import HeaderComponent from '@/components/Header.vue'
import FooterComponent from '@/components/Footer.vue'
import { getLocationInfo } from "@/components/form.api";
import updateToken from '@/middleware/update-token';

export default Vue.extend({
  name: 'app',
  components: {
    HeaderComponent,
    FooterComponent
  },
  watch: {
    $route() {
      updateToken();
    },
  },
  data() {
    return {
        // default location
        locationVal: "Victoria Probation Office"
    }
  },
  mounted () {
    this.getLocation();
  },
  methods: {
    async getLocation() {
      //this.locationVal = "Victoria Probation Office";

      //Sample get /locations return:
      // {
      //   "items": [
      //     {
      //       "locationId": 1,
      //       "locationCd": "victoria",
      //       "locationDescription": "Victoria"
      //     },
      //     {
      //       "locationId": 10,
      //       "locationCd": "vancouver",
      //       "locationDescription": "Vancouver"
      //     }
      //   ]
      // }
      const [error, response] = await getLocationInfo();
      if (error) {
        console.error(error);
      } else {
        if (response != null && response.items != null && response.items.length > 0) {
            this.locationVal = response.items[0].locationDescription;
        }
        
        console.info("Location fetched: ", response.items);
      }
    }
  } 
});
</script>
