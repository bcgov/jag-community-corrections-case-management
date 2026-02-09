import Keycloak from 'keycloak-js'

const options = {
  url: config.VUE_APP_KEYCLOAK_URL,
  realm: config.VUE_APP_KEYCLOAK_REALM,
  clientId: config.VUE_APP_KEYCLOAK_CLIENT_ID
}

const keycloak = new Keycloak(options)
const kcLogin = keycloak.login;
keycloak.login = (options) => {
  Object.assign(options, {idpHint: config.VUE_APP_KC_IDP_HINT});
  kcLogin(options);
};

const Plugin = {
  install(app) {
    app.config.globalProperties.$keycloak = keycloak;
  }
};

export { keycloak };
export default Plugin;
