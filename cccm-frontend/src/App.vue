<template>
  <div id="app">
    <!--Header section-->
    <HeaderComponent/>

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
  }
});
</script>
