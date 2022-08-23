import {defineStore} from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'
import { getLocationInfo } from "@/components/form.api";

export const useStore = defineStore('main', { 
    // state
    state: () =>({
        locationCD: useLocalStorage('locationCD', ''),
        locationDescription: useLocalStorage('locationDescription', ''),
        // locationCD: 'notset',
        // locationDescription: 'notset',
    }),

    // actions
    actions: {
        clearCachedLocation() {
            //console.info("Clear cached location.");
            this.locationCD = '';
            this.locationDescription = '';
        },
        getLocation() {
            //console.info("Attempt to fetch getLocation.");
            if (this.locationCD == '') {
                console.info("Fetching location ...");
                const [error, response] = getLocationInfo();
                if (error) {
                    console.error(error);
                } else {
                    if (response != null && response.items != null && response.items.length > 0) {
                        this.locationDescription = response.items[0].locationDescription;
                        this.locationCD = response.items[0].locationCd;
                    }
                    //console.info("Location fetched: ", response.items);
                }
                // to be removed
                this.locationDescription = "Victoria Probation Office";
                this.locationCD = "victoria";
            }
        }
    }
    // getters
})