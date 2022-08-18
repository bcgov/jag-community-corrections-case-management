import Vue from 'vue'
import VueCompositionAPI, { createApp, h } from '@vue/composition-api'

import App from './App.vue'
import router from './router'
import authentication from '@/plugins/authentication'
import updateToken from '@/middleware/update-token'
import setupInterceptors from '@/services/setupAxioInterceptors'
import vuetify from '@/plugins/vuetify'
import { getLocationInfo } from "@/components/form.api"

Vue.use(VueCompositionAPI)
Vue.use(authentication)

setupInterceptors();

Vue.$keycloak
  .init({ onLoad: 'login-required', checkLoginIframe: false })
  .then((authenticated: boolean) => {
    new Vue({
      vuetify,
      router,
      render: h => h(App),

      data() {
        return {
          locationCD: "notset",
          locationDescrption: "notset"
        }
      },
      created() {
        console.log("this.locationCD: ", this.locationCD);
        if (this.locationCD == "notset") {
          this.getLocation();
        }
      },
      methods: {
        getLocation() {
          const [error, response] = getLocationInfo();
          if (error) {
            console.error(error);
          } else {
            if (response != null && response.items != null && response.items.length > 0) {
                this.locationDescrption = response.items[0].locationDescription;
                this.locationCD = response.items[0].locationCd;
            }
          }
          // to be removed
          this.locationDescrption = "Victoria Probation Office";
          this.locationCD = "victoria";
        }
      }
    }).$mount('#app');

    window.onfocus = () => {
      updateToken();
    };
  })

  