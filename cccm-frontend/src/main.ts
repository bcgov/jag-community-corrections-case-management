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
import {faBarChart, faComments, faWarning, fas} from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';

import Vidle from 'v-idle'

Vue.use(Vidle)

library.add(faBarChart, faComments, faWarning, fas, far, fab)

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
Vue.prototype.$CONST_FORMTYPE_SO_OVERALL = 'SMO_OVERALL'
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
  { formType: 'CRNA', autoCalcOnFormload: false, autoCalcOnFormComplete: false, autoCalcOnFormClose: false, cmp: true, formTypeDesc: 'CRNA', dataRefreshSectionIndex: '', formTitle: 'Community Risk Needs Assessment Form (CRNA-CMP)', assessmentStatusRequired: true, formTypeLabel: 'CRNA-CMP Type', showSourcesContacted: true },
  { formType: 'SARA', autoCalcOnFormload: false, autoCalcOnFormComplete: false, autoCalcOnFormClose: false, cmp: false, formTypeDesc: 'SARA', dataRefreshSectionIndex: '', formTitle: 'SARA', assessmentStatusRequired: true, formTypeLabel: 'SARA Type', showSourcesContacted: false },
  { formType: 'ACUTE', autoCalcOnFormload: false, autoCalcOnFormComplete: false, autoCalcOnFormClose: false, cmp: false, formTypeDesc: 'Acute', dataRefreshSectionIndex: '1', formTitle: 'Acute', assessmentStatusRequired: false, formTypeLabel: '', showSourcesContacted: true },
  { formType: 'STABLE', autoCalcOnFormload: false, autoCalcOnFormComplete: false, autoCalcOnFormClose: false, cmp: true, formTypeDesc: 'Stable', dataRefreshSectionIndex: '5', formTitle: 'Stable', assessmentStatusRequired: true, formTypeLabel: 'STABLE Type', showSourcesContacted: true },
  { formType: 'STAT99R', autoCalcOnFormload: false, autoCalcOnFormComplete: false, autoCalcOnFormClose: false, cmp: false, formTypeDesc: 'Static-99R', dataRefreshSectionIndex: '1', formTitle: 'Static-99R', assessmentStatusRequired: false, formTypeLabel: '', showSourcesContacted: true },
  { formType: 'SMO_OVERALL', autoCalcOnFormload: true, autoCalcOnFormComplete: true, autoCalcOnFormClose: true, cmp: false, formTypeDesc: 'SMO-Overall-CMP', dataRefreshSectionIndex: '', formTitle: 'SMO-Overall-CMP', assessmentStatusRequired: false, formTypeLabel: '', showSourcesContacted: true }
],

Vue.prototype.$BUTTON_TEXT_SUBMIT='Submit Form'
Vue.prototype.$BUTTON_TEXT_SAVE_CONTINUE='Save and Continue'

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
