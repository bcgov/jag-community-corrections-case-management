<template>
  <div id="app">
    <!--Header section-->
    <HeaderComponent :key="key_header" :locationInfo="$locationDescrption" />

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
  data() {
    return {
      key_header: 0
    }
  },
  watch: {
    $route() {
      updateToken();
    },
  },
  mounted () {
    if (this.$locationCD == 'notset') {
      this.getLocation();
    }
  },
  methods: {
    async getLocation() {
      const [error, response] = await getLocationInfo();
      if (error) {
        console.error(error);
      } else {
        if (response != null && response.items != null && response.items.length > 0) {
            this.$locationDescrption = response.items[0].locationDescription;
            this.$locationCD = response.items[0].locationCd;
        }
        //console.info("Location fetched: ", response.items);
      }
      // to be removed
      this.$locationDescrption = "Victoria Probation Office";
      this.$locationCD = "victoria";
      this.key_header++;
    }
  }
});
</script>
