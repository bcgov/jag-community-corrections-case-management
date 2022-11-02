import {defineStore} from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'
import { async_getUserDefaultLocation, async_getUserLocations } from "@/components/form.api";

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
        async getUserLocations() {
            if (this.locations == null || this.locations.length == 0) {
                console.info("Fetching user locations ...");
                const [error, response] = await async_getUserLocations();
                if (error) {
                    console.error(error);
                    return [error];
                } else {
                    console.info("Locations fetched: ", response);
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
                console.info("Fetching default location ...");
                const [error, response] = await async_getUserDefaultLocation();
                if (error) {
                    console.error(error);
                    return [error];
                } else {
                    console.info("Location fetched: ", response);
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