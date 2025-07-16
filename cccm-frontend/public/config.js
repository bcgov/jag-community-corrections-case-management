const config = (() => {
    return {
      "VUE_APP_CCCM_API_ENDPOINT": 'http://localhost:8080',
      "VUE_APP_KEYCLOAK_URL": 'https://dev.jag.gov.bc.ca/cornet-keycloak/',
      "VUE_APP_KEYCLOAK_REALM": 'Cornet',
      "VUE_APP_KEYCLOAK_CLIENT_ID": 'cccm-frontend',
      "VUE_APP_KC_IDP_HINT": '',
      "VUE_APP_TIMEOUT": '120000' 
    };
  })();