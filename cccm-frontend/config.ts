const config = (() => {
    return {
      "VUE_APP_CCCM_API_ENDPOINT": 'http://localhost:8080',
      "VUE_APP_KEYCLOAK_URL": 'https://cccm-keycloak-abb712-dev.apps.gold.devops.gov.bc.ca/',
      "VUE_APP_KEYCLOAK_REALM": 'cccm',
      "VUE_APP_KEYCLOAK_CLIENT_ID": 'cccm-frontend',
      "VUE_APP_KC_IDP_HINT": 'idir',
      "VUE_APP_TIMEOUT": '120000'
    };
  })();