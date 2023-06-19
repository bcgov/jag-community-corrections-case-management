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
import Vidle from 'v-idle'

Vue.use(Vidle)

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
Vue.prototype.$CONST_FORMTYPE_STAT99R = 'STAT99R'
Vue.prototype.$CONST_FORMTYPE_STABLE = 'STABLE'
Vue.prototype.$CONST_FORMTYPE_SO_OVERALL = 'SO_OVERALL'
Vue.prototype.$CONST_FORMTYPE_RNA = 'ALL'
Vue.prototype.$CONST_DATATABLE_ITEMS_PER_PAGE = 15
Vue.prototype.$CONST_DATATABLE_PAGE_FILTERLSIT = [1, 2, 5, 10, 15, 20, 30, 50]

Vue.prototype.$USER_GROUP_PO = 'po'
Vue.prototype.$USER_GROUP_SUPERVISOR = 'supervisor'
Vue.prototype.$USER_GROUP_ADMIN = 'admin'
Vue.prototype.$USER_GROUP_ITRP = 'itrp'
Vue.prototype.$USER_GROUP_RESEARCHER = 'researcher'

Vue.prototype.$FORM_STATUS_INCOMPLETE='Incomplete'
Vue.prototype.$FORM_STATUS_COMPLETE='Complete'

Vue.prototype.$FORM_TYPE_INITIAL='Initial'
Vue.prototype.$FORM_TYPE_REASSESSMENT='Reassessment'

Vue.prototype.$ROUTER_NAME_HOME='home'
Vue.prototype.$ROUTER_NAME_CMPFORM='cmpform'
Vue.prototype.$ROUTER_NAME_DASHBOARDPO='dashboardpo'
Vue.prototype.$ROUTER_NAME_DASHBOARDSUPERVISOR='dashboardsupervisor'
Vue.prototype.$ROUTER_NAME_CLIENTRECORD='clientrecord'
Vue.prototype.$ROUTER_NAME_CLIENTSEARCH='clientsearch'
Vue.prototype.$ROUTER_NAME_PRINT='printView'

Vue.prototype.$AUTH_ROLE_PO='auth-po'
Vue.prototype.$AUTH_ROLE_SUPERVISOR='auth-supervisor'
Vue.prototype.$AUTH_ROLE_ADMIN='auth-admin'
Vue.prototype.$AUTH_ROLE_ITRP='auth-itrp'
Vue.prototype.$AUTH_ROLE_RESEARCHER='auth-researcher'
Vue.prototype.$AUTH_SMO_FORMS='smo-forms'
Vue.prototype.$AUTH_SHOW_TREND='show-trend'

Vue.prototype.$FORM_INFO = [
  { formType: 'CRNA', formTypeDesc: 'CRNA', dataRefreshSectionIndex: '', formTitle: 'Community Risk Needs Assessment Form (CRNA-CMP)', assessmentStatusRequired: true, formTypeLabel: 'CRNA-CMP Type', showSourcesContacted: true },
  { formType: 'SARA', formTypeDesc: 'SARA', dataRefreshSectionIndex: '', formTitle: 'SARA', assessmentStatusRequired: true, formTypeLabel: 'SARA Type', showSourcesContacted: false },
  { formType: 'ACUTE', formTypeDesc: 'Acute', dataRefreshSectionIndex: '1', formTitle: 'Acute', assessmentStatusRequired: false, formTypeLabel: '', showSourcesContacted: true },
  { formType: 'STABLE', formTypeDesc: 'Stable', dataRefreshSectionIndex: '5', formTitle: 'Stable', assessmentStatusRequired: true, formTypeLabel: 'STABLE Type', showSourcesContacted: true },
  { formType: 'STAT99R', formTypeDesc: 'Static-99R', dataRefreshSectionIndex: '1', formTitle: 'Static-99R', assessmentStatusRequired: false, formTypeLabel: '', showSourcesContacted: true },
  { formType: 'SO_OVERALL', formTypeDesc: 'SMO-Overall-CMP', dataRefreshSectionIndex: '', formTitle: 'SMO-Overall-CMP', assessmentStatusRequired: false, formTypeLabel: '', showSourcesContacted: true }
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
