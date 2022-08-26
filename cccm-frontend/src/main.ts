import Vue from 'vue'
import VueCompositionAPI, { createApp, h } from '@vue/composition-api'
import { createPinia, PiniaVuePlugin } from 'pinia'
import App from './App.vue'
import router from './router'
import authentication from '@/plugins/authentication';
import updateToken from '@/middleware/update-token';
import setupInterceptors from '@/services/setupAxioInterceptors';
import vuetify from '@/plugins/vuetify'
import {useStore} from "@/stores/store";
import {mapStores} from 'pinia';

Vue.use(VueCompositionAPI)
Vue.use(authentication)
Vue.use(PiniaVuePlugin)
const pinia = createPinia()
Vue.use(pinia)

setupInterceptors();

// When keycloak token expired, cleanup the cached info
const store = useStore();
Vue.$keycloak.onTokenExpired = function() { 
  // Clear the cached location info
  store.clearCachedLocation();
  // Logout
  Vue.$keycloak.logout({ redirectUri: window.location.origin  + import.meta.env.BASE_URL });
}

Vue.$keycloak
  .init({ onLoad: 'login-required', checkLoginIframe: false })
  .then((authenticated: boolean) => {
    if (!authenticated) {
      window.location.reload();
    } else {
      //console.log("Authenticated");
      new Vue({
        vuetify,
        router,
        pinia,
        render: h => h(App)
      }).$mount('#app');
  
      window.onfocus = () => {
        updateToken();
      };
    }
    
  })
