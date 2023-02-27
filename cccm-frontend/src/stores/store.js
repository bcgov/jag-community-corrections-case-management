import Vue from 'vue'
import { defineStore } from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'
import { async_getUserDefaultLocation, async_getUserLocations, getPOList } from "@/components/form.api";
import { isNull } from 'url/util';

export const useStore = defineStore('main', { 
    // state
    state: () =>({
        locationCD: useLocalStorage('locationCD', ''),
        locationDescription: useLocalStorage('locationDescription', ''),
        locations: useLocalStorage('locations', []),
        loginUserGroup: useLocalStorage('loginUserGroup', null),
        loginUserName: useLocalStorage('loginUserName', null),
        poList: useLocalStorage('poList', new Map()),
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
            this.clearCachedUserLocations();
            this.clearCachedLocation();
            this.clearCachedUserInfo();
            this.clearCachedPOList();
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
        clearCachedUserInfo() {
            //console.info("Clear cached user info.");
            this.loginUserGroup = null;
            this.loginUserName = null;
        },
        clearCachedPOList() {
            //console.info("Clear cached poList.");
            this.poList = new Map();
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
                    return [null, this.locationCD];
                }
            }
            return [null, this.locationCD];
        },
        async getPOList(locationCD) {
            //console.log("get polist for: ", locationCD, this.poList, this.poList.get(locationCD));
            if (this.poList == null || this.poList.get(locationCD) == null) {
                if (this.poList == null) {
                    this.poList = new Map();
                }
                if (this.poList.get(locationCD) == null) {
                    // Obtain list of POs for the default location
                    const [error, response] = await getPOList();
                    if (error) {
                        console.error(error);
                    } else {
                        //console.log("PO list search: ", response);
                        if (response != null) {
                            this.poList.set(locationCD, response);
                        }
                        //console.log("this.poList: ", this.poList);
                        return [null, this.poList.get(locationCD)];
                    }
                }
            }
            return [null, this.poList.get(locationCD)];
        }
    }
    // getters
})