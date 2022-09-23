import {defineStore} from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'
import { getUserDefaultLocation, getUserLocations } from "@/components/form.api";

export const useStore = defineStore('main', { 
    // state
    state: () =>({
        locationCD: useLocalStorage('locationCD', ''),
        locationDescription: useLocalStorage('locationDescription', ''),
        locations: useLocalStorage('locations', []),
    }),

    // actions
    actions: {
        clearAll() {
            this.locationCD = '';
            this.locationDescription = '';
            this.locations = [];
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
        getUserLocations() {
            if (this.locations == null || this.locations.length == 0) {
                console.info("Fetching user locations ...");
                const [error, response] = getUserLocations();
                if (error) {
                    console.error(error);
                } else {
                    if (response != null && response.items != null) {
                        this.locations = response.items;
                    }
                    //console.info("Location fetched: ", response.items);
                }
                // to be removed
                this.locations = [{value: "Victoria Probation Office", key: "victoria"}, {value: "Vancouver", key: "vancouver"}, {value: "Nanaimo", key: "nanaimo"}];
            }
        },
        getUserDefaultLocation() {
            //console.info("Attempt to fetch getUserDefaultLocation.");
            if (this.locationCD == '') {
                console.info("Fetching location ...");
                const [error, response] = getUserDefaultLocation();
                if (error) {
                    console.error(error);
                } else {
                    if (response != null) {
                        this.locationDescription = response.value;
                        this.locationCD = response.key;
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