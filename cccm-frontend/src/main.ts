import { createApp } from 'vue'
import { createPinia, setActivePinia } from 'pinia'
import App from './App.vue'
import router from './router'
import authentication, { keycloak } from '@/plugins/authentication';
import updateToken from '@/middleware/update-token';
import setupInterceptors from '@/services/setupAxioInterceptors';
import vuetify from '@/plugins/vuetify'
import { installGlobals } from '@/constants/appGlobals'

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

import {library} from '@fortawesome/fontawesome-svg-core';
import {faBarChart, faComments, faWarning, fas} from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';

import Vidle from 'v-idle-3'
import '@formio/js/dist/formio.full.min.css'

library.add(faBarChart, faComments, faWarning, fas, far, fab)

const pinia = createPinia()
setActivePinia(pinia)
setupInterceptors(pinia);

keycloak
  .init({ onLoad: 'login-required', checkLoginIframe: false })
  .then((authenticated: boolean) => {
    if (!authenticated) {
      window.location.reload();
      return;
    }

    const app = createApp(App);
    app.use(authentication);
    app.use(pinia);
    app.use(router);
    app.use(vuetify);
    app.use(Vidle);
    installGlobals(app);
    app.component('font-awesome-icon', FontAwesomeIcon);
    app.mount('#app');

    window.onfocus = () => {
      updateToken();
    };
  })
