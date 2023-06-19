import Vue from 'vue'
import { defineStore } from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'
import { async_getUserDefaultLocation, async_getUserLocations, getPOList, lookupGender, lookupIdTypes, lookupCityCodes, lookupProvinceCodes, lookupAddressTypes } from "@/components/form.api";
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
        supportedFormTypes: useLocalStorage('supportedFormTypes', []),
        genderCodes: useLocalStorage('genderCodes', []),
        idTypeCodes: useLocalStorage('idTypeCodes', []),
        cityCodes: useLocalStorage('cityCodes', []),
        provinceCodes: useLocalStorage('provinceCodes', []),
        addressTypeCodes: useLocalStorage('addressTypeCodes', []),
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
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_RESEARCHER)) {
                    this.loginUserGroup = Vue.prototype.$USER_GROUP_RESEARCHER
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_ITRP)) {
                    this.loginUserGroup = Vue.prototype.$USER_GROUP_ITRP
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_PO)) {
                    this.loginUserGroup = Vue.prototype.$USER_GROUP_PO;
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_SUPERVISOR)) {
                    this.loginUserGroup = Vue.prototype.$USER_GROUP_SUPERVISOR
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_ADMIN)) {
                    this.loginUserGroup = Vue.prototype.$USER_GROUP_ADMIN
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
        isShowSMOForms() {
            if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_SMO_FORMS)) {
                return true;
            }
            return false;
        },
        isShowTrendAnalysis() {
            if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_SHOW_TREND)) {
                return true;
            }
            return false;
        },
        getSupportedFormTypes() {
            if (this.supportedFormTypes.length > 0) {
                return this.supportedFormTypes;
            }
            // default the supportedFormTypes to include smo formtypes
            this.supportedFormTypes = [
                { value: "ALL", key: "ALL"},
                { value: "Acute", key: "ACUTE" },
                { value: "CRNA", key: "CRNA" },
                { value: "CRNA-SARA", key: "SARA" },
                { value: "SMO-Overall-CMP", key: "SO_OVERALL-CMP" },
                { value: "Stable", key: "STABLE" },
                { value: "Static-99R", key: "STAT99R" }
              ];

            // if login user doesn't have smo_forms role, only include CRNA and CRNA-SARA
            if (!Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_SMO_FORMS)) {
                this.supportedFormTypes = [
                    { value: "ALL", key: "ALL"},
                    { value: "CRNA", key: "CRNA" },
                    { value: "CRNA-SARA", key: "SARA" }
                  ]
            }
            return this.supportedFormTypes;
        },
        clearAll() {
            this.clearCachedUserLocations();
            this.clearCachedLocation();
            this.clearCachedUserInfo();
            this.clearCachedPOList();
            this.clearCachedSupportedFormTypes();
            this.clearCachedClientSearchLookupCodes();
        },
        clearCachedClientSearchLookupCodes() {
            this.genderCodes = [];
            this.idTypeCodes = [];
            this.cityCodes = [];
            this.provinceCodes = [];
            this.addressTypeCodes = [];
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
        clearCachedSupportedFormTypes() {
            this.supportedFormTypes = [];
        },
        async lookupAddressTypeCodes() {
            if (this.addressTypeCodes == null || this.addressTypeCodes.length == 0) {
                const [error, response] = await lookupAddressTypes();
                if (error) {
                    console.error(error);
                    return [error];
                } else {
                    if (response != null && response.items != null) {
                        this.addressTypeCodes = response.items;
                    }
                    return [null, this.addressTypeCodes];
                }
            }
            return [null, this.addressTypeCodes];
        },
        async lookupGenderCodes() {
            if (this.genderCodes == null || this.genderCodes.length == 0) {
                const [error, response] = await lookupGender();
                if (error) {
                    console.error(error);
                    return [error];
                } else {
                    if (response != null && response.items != null) {
                        this.genderCodes = response.items;
                    }
                    return [null, this.genderCodes];
                }
            }
            return [null, this.genderCodes];
        },
        async lookupIdTypeCodes() {
            if (this.idTypeCodes == null || this.idTypeCodes.length == 0) {
                const [error, response] = await lookupIdTypes();
                if (error) {
                    console.error(error);
                    return [error];
                } else {
                    if (response != null && response.items != null) {
                        this.idTypeCodes = response.items;
                    }
                    return [null, this.idTypeCodes];
                }
            }
            return [null, this.idTypeCodes];
        },
        async lookupCityCodes() {
            if (this.cityCodes == null || this.cityCodes.length == 0) {
                const [error, response] = await lookupCityCodes();
                if (error) {
                    console.error(error);
                    return [error];
                } else {
                    if (response != null && response.items != null) {
                        this.cityCodes = response.items;
                    }
                    return [null, this.cityCodes];
                }
            }
            return [null, this.cityCodes];
        },
        async getProvinceCodes() {
            if (this.provinceCodes == null || this.provinceCodes.length == 0) {
                const [error, response] = await lookupProvinceCodes();
                if (error) {
                    console.error(error);
                    return [error];
                } else {
                    if (response != null && response.items != null) {
                        this.provinceCodes = response.items;
                    }
                    return [null, this.provinceCodes];
                }
            }
            return [null, this.provinceCodes];
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