import {defineStore} from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'
import { getUserDefaultLocation } from "@/components/form.api";

export const useStore = defineStore('main', { 
    // state
    state: () =>({
        locationCD: useLocalStorage('locationCD', ''),
        locationDescription: useLocalStorage('locationDescription', ''),
        //csNumber: useLocalStorage('csNumber', ''),
    }),

    // actions
    actions: {
        clearCachedInfo() {
            //console.info("Clear cached Information.");
            this.locationCD = '';
            this.locationDescription = '';
            //this.csNumber = '';
        },
        clearCachedLocation() {
            //console.info("Clear cached location.");
            this.locationCD = '';
            this.locationId = -1;
            this.locationDescription = '';
        },
        // clearCachedUserInfo() {
        //     //console.info("Clear cached user Info.");
        //     this.csNumber = '';
        // },
        getUserDefaultLocation() {
            //console.info("Attempt to fetch getUserDefaultLocation.");
            if (this.locationCD == '') {
                console.info("Fetching location ...");
                const [error, response] = getUserDefaultLocation();
                if (error) {
                    console.error(error);
                } else {
                    if (response != null && response.items != null && response.items.length > 0) {
                        
                        this.locationDescription = response.items[0].locationDescription;
                        this.locationCD = response.items[0].locationCd;
                        this.locationId = response.items[0].locationId;
                    }
                    //console.info("Location fetched: ", response.items);
                }
                // to be removed
                this.locationDescription = "Victoria Probation Office";
                this.locationCD = "victoria";
            }
        }, 
        // setCSNumber(csNumber) {
        //     this.csNumber = csNumber;
        // }
    }
    // getters
})