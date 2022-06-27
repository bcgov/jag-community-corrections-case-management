import Vue from 'vue'
import VueCompositionAPI, { createApp, h } from '@vue/composition-api'

import App from './App.vue'
import router from './router'
import authentication from "@/plugins/authentication"
import updateToken from '@/middleware/update-token';
import setupInterceptors from '@/services/setupAxioInterceptors';

Vue.use(VueCompositionAPI)
Vue.use(authentication)

setupInterceptors();

Vue.$keycloak
  .init({ onLoad: 'login-required', checkLoginIframe: false })
  .then((authenticated: boolean) => {
    new Vue({
      router,
      render: h => h(App)
    }).$mount('#app');

    window.onfocus = () => {
      updateToken();
    };
  })
