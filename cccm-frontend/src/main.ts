import Vue from 'vue'
import VueCompositionAPI, { createApp, h } from '@vue/composition-api'
import { createPinia, PiniaVuePlugin } from 'pinia'
import App from './App.vue'
import router from './router'
import authentication from '@/plugins/authentication';
import updateToken from '@/middleware/update-token';
import setupInterceptors from '@/services/setupAxioInterceptors';
import vuetify from '@/plugins/vuetify'


Vue.use(VueCompositionAPI)
Vue.use(authentication)
Vue.use(PiniaVuePlugin)
const pinia = createPinia()
Vue.use(pinia)

setupInterceptors();

Vue.$keycloak
  .init({ onLoad: 'login-required', checkLoginIframe: false })
  .then((authenticated: boolean) => {
    new Vue({
      vuetify,
      router,
      // note the same `pinia` instance can be used across multiple Vue apps on the same page
      pinia,
      render: h => h(App)
    }).$mount('#app');

    window.onfocus = () => {
      updateToken();
    };
  })
