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

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

import {library} from '@fortawesome/fontawesome-svg-core';
import {faBarChart, faComments, faWarning} from '@fortawesome/free-solid-svg-icons'

library.add(faBarChart, faComments, faWarning);

Vue.use(VueCompositionAPI)
Vue.use(authentication)
Vue.use(PiniaVuePlugin)
Vue.component('font-awesome-icon', FontAwesomeIcon)

const pinia = createPinia()
Vue.use(pinia)

setupInterceptors();

Vue.prototype.$CONST_FORMTYPE_CRNA = 'CRNA'
Vue.prototype.$CONST_FORMTYPE_SARA = 'SARA'
Vue.prototype.$CONST_DATATABLE_ITEMS_PER_PAGE = 15
Vue.prototype.$CONST_DATATABLE_PAGE_FILTERLSIT = [1, 2, 5, 10, 15, 20, 30, 50]

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
