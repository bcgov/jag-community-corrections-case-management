<template>
  <div id="app">
    <!--Header section-->
    <HeaderComponent v-if="!isPrint" />

    <!--Body section-->
    <router-view />

    <!--Footer section-->
    <FooterComponent v-if="!isPrint" />
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
  data() {
    return {
      isPrint: false
    }
  },
  mounted(){
    this.isPrint = this.$route.name === 'printView';
  },
  watch: {
    $route() {
      updateToken();
    },
  }
});
</script>
