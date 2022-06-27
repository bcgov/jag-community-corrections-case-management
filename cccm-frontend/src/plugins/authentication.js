import Vue from 'vue'
import Keycloak from 'keycloak-js'

const options = {
  url: config.VUE_APP_KEYCLOAK_URL,
  realm: config.VUE_APP_KEYCLOAK_REALM,
  clientId: config.VUE_APP_KEYCLOAK_CLIENT_ID
}

const _keycloak = new Keycloak(options)

const Plugin = {
  install(Vue) {
    Vue.$keycloak = _keycloak
  }
}

Plugin.install = Vue => {
  Vue.$keycloak = _keycloak
  Object.defineProperties(Vue.prototype, {
    $keycloak: {
      get() {
        return _keycloak
      }
    }
  })
}

Vue.use(Plugin)

export default Plugin
