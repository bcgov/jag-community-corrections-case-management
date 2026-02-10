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
// import '@formio/js/dist/formio.full.min.css'
// import '@/assets/css/fa-all.min.css'
// import '@/assets/css/bootstrap.css'
// import '@/assets/css/noto-sans.css'
// import '@/assets/css/style_header.css'
// import '@/assets/css/style_footer.css'
// import '@/assets/css/layout.css'
// import '@/assets/css/styles.css'
// import '@/assets/css/materialdesignicons.min.css'
// import '@/assets/css/overrides.css'
// import '@/assets/css/print.css'

library.add(faBarChart, faComments, faWarning, fas, far, fab)

const pinia = createPinia()
setActivePinia(pinia)
setupInterceptors(pinia);

console.log("Keycloak instance created, initializing authentication...: ", keycloak);
keycloak
  .init({ onLoad: 'login-required', checkLoginIframe: false })
  .then((authenticated: boolean) => {
    console.log("Authentication initialized. Authenticated: ", authenticated);
    const app = createApp(App);
    app.use(authentication);
    app.use(pinia);
    app.use(router);
    app.use(vuetify);
    app.use(Vidle);
    installGlobals(app);
    app.component('font-awesome-icon', FontAwesomeIcon);

    if (!authenticated) {
      const loginAttemptKey = 'kc-login-attempt';
      if (!sessionStorage.getItem(loginAttemptKey)) {
        sessionStorage.setItem(loginAttemptKey, '1');
        keycloak.login({ redirectUri: window.location.href });
        return;
      }
    } else {
      sessionStorage.removeItem('kc-login-attempt');
    }

    app.mount('#app');

    window.onfocus = () => {
      updateToken();
    };
  })
