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
import { defineComponent } from 'vue';
import HeaderComponent from '@/components/Header.vue'
import FooterComponent from '@/components/Footer.vue'
import updateToken from '@/middleware/update-token';

export default defineComponent({
  name: 'app',
  components: {
    HeaderComponent,
    FooterComponent
  },
  computed: {
    isPrint(): boolean {
      return this.$route?.name === 'printView';
    }
  },
  watch: {
    '$route': {
      handler() {
        updateToken();
      },
      deep: true
    }
  }
});
</script>
