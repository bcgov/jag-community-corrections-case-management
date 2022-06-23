import Vue from 'vue'
import VueCompositionAPI, { createApp, h } from '@vue/composition-api'

import App from './App.vue'
import router from './router'
import authentication from "@/plugins/authentication"

Vue.use(VueCompositionAPI)
Vue.use(authentication)

Vue.$keycloak
  .init({ onLoad: 'login-required', checkLoginIframe: false })
  .then(() => {
    new Vue({
      router,
      render: h => h(App)
    }).$mount('#app')
  })

// const app = createApp({
//   router,
//   render: () => h(App)
// })

// app.mount('#app')