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
import HeaderComponent from '@/components/Header.vue'
import FooterComponent from '@/components/Footer.vue'
import { getLocationInfo } from "@/components/form.api.ts";

export default {
  name: 'app',
  components: {
    HeaderComponent,
    FooterComponent
  },
  data() {
    return {
        // default location
        locationVal: "Victoria Probation Office"
    }
  },
  mounted () {
    //this.getLocation();
  },
  methods: {
    async getLocation() {
      //this.locationVal = "Victoria Probation Office";
      const [error, response] = await getLocationInfo();
      if (error) {
        console.error(error);
      } else {
        //TODO: fetch location when it's suported in the datamodel
        this.locationVal = response.formType;

        console.info("Location fetched: " + this.locationVal);
      }
    }
  } 
}
</script>
