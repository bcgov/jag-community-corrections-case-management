/// <reference types="vite/client" />

import type { KeycloakInstance } from 'keycloak-js';

type AppGlobals = typeof import('./src/constants/appGlobals').APP_GLOBALS;

declare module 'vue' {
	interface ComponentCustomProperties extends AppGlobals {
		$keycloak: KeycloakInstance;
	}
}

declare const config: {
	VUE_APP_CCCM_API_ENDPOINT: string;
	VUE_APP_KEYCLOAK_URL: string;
	VUE_APP_KEYCLOAK_REALM: string;
	VUE_APP_KEYCLOAK_CLIENT_ID: string;
	VUE_APP_KC_IDP_HINT: string;
	VUE_APP_TIMEOUT: string;
};

declare module '@formio/vue';

declare module '@/stores/store' {
	export const useStore: any;
}

declare module '@/stores/autoSaveStore' {
	export const useAutosaveStore: any;
}

declare module '@/plugins/authentication' {
	const plugin: any;
	export const keycloak: any;
	export default plugin;
}
