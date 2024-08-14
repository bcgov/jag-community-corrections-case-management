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
        locationTypeCD: useLocalStorage('locationTypeCD', ''),
        locations: useLocalStorage('locations', []),
        loginUserGroups: useLocalStorage('loginUserGroup', null),
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
        getLoginUserGroups() {
            if (this.loginUserGroups == null) {
                this.loginUserGroups = ''
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_RESEARCHER)) {
                    this.loginUserGroups += Vue.prototype.$USER_GROUP_RESEARCHER + ',';
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_ADMIN_COMM)) {
                    this.loginUserGroups += Vue.prototype.$USER_GROUP_ADMIN_COMM + ',';
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_PO)) {
                    this.loginUserGroups += Vue.prototype.$USER_GROUP_PO + ',';
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_ITRP)) {
                    this.loginUserGroups += Vue.prototype.$USER_GROUP_ITRP + ',';
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_SUPERVISOR)) {
                    this.loginUserGroups += Vue.prototype.$USER_GROUP_SUPERVISOR + ',';
                }
                if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_ROLE_ADMIN)) {
                    this.loginUserGroups += Vue.prototype.$USER_GROUP_ADMIN + ',';
                }
            }
        },
        getLoginUserName() {
            if (this.loginUserName == null) {
                this.loginUserName = Vue.$keycloak.tokenParsed.family_name + ', ' + Vue.$keycloak.tokenParsed.given_name;
            }
        },
        hasSupervisorDash() {
            // User with po-manage role can access the supervisor dashboard
            if (Vue.$keycloak.hasRealmRole(Vue.prototype.$ROLE_PO_MANAGE)) {
                return true;
            }
            return false;
        },
        hasPODash() {
            if (this.loginUserGroups.includes(Vue.prototype.$USER_GROUP_SUPERVISOR) ||
                this.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ADMIN) ||
                this.loginUserGroups.includes(Vue.prototype.$USER_GROUP_PO)) {
              return true;
            }
            return false;
        },
        hasITRPDash() {
            if (this.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ITRP)) {
                return true;
            }
            return false;
        },
        isEventTriggerAutoCalcAllowed() {
            // When admin_comm user views forms (e.g., SMO_OVERALL) that requires data refresh before form load,
            // the data refresh shouldn't be allowed
            if (this.loginUserGroups == '' ||
                this.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ADMIN_COMM)) {
                return false;
            }
            return true;
        },
        isAllowFormWrite() {
            // Admin_comm user can only view 
            if (this.loginUserGroups == '' ||
                this.loginUserGroups.includes(Vue.prototype.$USER_GROUP_ADMIN_COMM)) {
                return false;
            }
            return true;
        },
        isShowTrendAnalysis() {
            if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_SHOW_TREND)) {
                return true;
            }
            return false;
        },
        // Temporary role to hide CMRP Form before release
        isShowCmrpForm() {
            if (Vue.$keycloak.hasRealmRole(Vue.prototype.$AUTH_SHOW_CMRP)) {
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
                { value: "SMO-Overall-CMP", key: "SMO_OVERALL" },
                { value: "Stable", key: "STABLE" },
                { value: "Static-99R", key: "STAT99R" },
                { value: "Custody-CMRP", key: "CMRP" }
              ];

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
            this.locationTypeCD = '';
        },
        clearCachedUserInfo() {
            //console.info("Clear cached user info.");
            this.loginUserGroups = null;
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
                        this.locationTypeCD = response.locationTypeCode;
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