import Vue from 'vue'
import { defineStore } from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'
import { async_getUserDefaultLocation, async_getUserLocations } from "@/components/form.api";

export const useStore = defineStore('main', { 
    // state
    state: () =>({
        locationCD: useLocalStorage('locationCD', ''),
        locationDescription: useLocalStorage('locationDescription', ''),
        locations: useLocalStorage('locations', []),
        loginUserGroup: useLocalStorage('loginUserGroup', null),
        loginUserName: useLocalStorage('loginUserName', null),
    }),

    // actions
    actions: {
        // List of realm roles:
        // client-search	
        // client-view	
        // data-view	
        // default-roles-cccm	
        // form-add
        // form-delete	
        // form-update	
        // form-view
        // po-manage
        getLoginUserGroup() {
            if (this.loginUserGroup == null) {
                this.loginUserGroup = ''
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_PO)) {
                    this.loginUserGroup = Vue.prototype.$USER_GROUP_PO;
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_SUPERVISOR)) {
                    this.loginUserGroup = Vue.prototype.$USER_GROUP_SUPERVISOR
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_ADMIN)) {
                    this.loginUserGroup = Vue.prototype.$USER_GROUP_ADMIN
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_ITRP)) {
                    this.loginUserGroup = Vue.prototype.$USER_GROUP_ITRP
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_RESEARCHER)) {
                    this.loginUserGroup = Vue.prototype.$USER_GROUP_RESEARCHER
                }
            }
        },
        getLoginUserName() {
            if (this.loginUserName == null) {
                this.loginUserName = Vue.$keycloak.tokenParsed.family_name + ', ' + Vue.$keycloak.tokenParsed.given_name;
            }
        },
        isHideDashboard() {
            if (this.loginUserGroup == '' ||
                this.loginUserGroup == Vue.prototype.$USER_GROUP_ITRP || 
                this.loginUserGroup == Vue.prototype.$USER_GROUP_RESEARCHER ) {
                return true;
            }
            return false;
        },
        isAllowFormWrite() {
            // Researcher can only view 
            if (this.loginUserGroup == '' ||
                this.loginUserGroup == Vue.prototype.$USER_GROUP_RESEARCHER ) {
                return false;
            }
            return true;
        },
        clearAll() {
            this.locationCD = '';
            this.locationDescription = '';
            this.locations = [];
            this.loginUserGroup = null;
            this.loginUserName = null;
        },
        clearCachedUserLocations() {
            //console.info("Clear cached user locations.");
            this.locations = [];
        },
        clearCachedLocation() {
            //console.info("Clear cached location.");
            this.locationCD = '';
            this.locationDescription = '';
        },
        async getUserLocations() {
            if (this.locations == null || this.locations.length == 0) {
                //console.info("Fetching user locations ...");
                const [error, response] = await async_getUserLocations();
                if (error) {
                    console.error(error);
                    return [error];
                } else {
                    //console.info("Locations fetched: ", response);
                    if (response != null && response.items != null) {
                        this.locations = response.items;
                    }
                    return [null, this.locations];
                }
            }
            return [null, this.locations];
        },
        async getUserDefaultLocation() {
            //console.info("Attempt to fetch async_getUserDefaultLocation.");
            if (this.locationCD == '') {
                //console.info("Fetching default location ...");
                const [error, response] = await async_getUserDefaultLocation();
                if (error) {
                    console.error(error);
                    return [error];
                } else {
                    //console.info("Location fetched: ", response);
                    if (response != null) {
                        this.locationDescription = response.value;
                        this.locationCD = response.key;
                    }
                    return [null, response];
                }
            }
            return [null, {}];
        }
    }
    // getters
})