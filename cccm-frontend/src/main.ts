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
Vue.prototype.$CONST_FORMTYPE_ACUTE = 'ACUTE'
Vue.prototype.$CONST_FORMTYPE_RNA = 'ALL'
Vue.prototype.$CONST_DATATABLE_ITEMS_PER_PAGE = 15
Vue.prototype.$CONST_DATATABLE_PAGE_FILTERLSIT = [1, 2, 5, 10, 15, 20, 30, 50]
Vue.prototype.$CONST_FORM_TYPES = [
  { value: "ALL", key: "ALL"},
  { value: "ACUTE", key: "ACUTE" },
  { value: "CRNA", key: "CRNA" },
  { value: "CRNA-SARA", key: "SARA" }
],
Vue.prototype.$USER_GROUP_PO = 'po'
Vue.prototype.$USER_GROUP_SUPERVISOR = 'supervisor'
Vue.prototype.$USER_GROUP_ADMIN = 'admin'

Vue.prototype.$FORM_STATUS_INCOMPLETE='Incomplete'
Vue.prototype.$FORM_STATUS_COMPLETE='Complete'

Vue.prototype.$FORM_TYPE_INITIAL='Initial'
Vue.prototype.$FORM_TYPE_REASSESSMENT='Reassessment'

Vue.prototype.$ROUTER_NAME_CMPFORM='cmpform'
Vue.prototype.$ROUTER_NAME_DASHBOARDPO='dashboardpo'
Vue.prototype.$ROUTER_NAME_CLIENTRECORD='clientrecord'
Vue.prototype.$ROUTER_NAME_CLIENTSEARCH='clientsearch'

Vue.prototype.$FORM_INFO = [
  { formType: 'CRNA', formTitle: 'Community Risk Needs Assessment Form (CRNA-CMP)', assessmentStatusRequired: true, formTypeLabel: 'CRNA-CMP Type' },
  { formType: 'SARA', formTitle: 'SARA (SARA-CMP)', assessmentStatusRequired: true, formTypeLabel: 'SARA-CMP Type' },
  { formType: 'ACUTE', formTitle: 'ACUTE', assessmentStatusRequired: false, formTypeLabel: '' }
],

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
