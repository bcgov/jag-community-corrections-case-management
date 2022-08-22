import {defineStore} from 'pinia';
import { getLocationInfo } from "@/components/form.api";

export const useStore = defineStore('main', { 
    // state
    state: () =>({
        // locationCD: useLocalStorage('locationCD', 'notset'),
        // locationDescription: useLocalStorage('locationDescription', 'notset'),
        locationCD: 'notset',
        locationDescription: 'notset',
    }),

    // actions
    actions: {
        getLocation() {
            if (this.locationCD == 'notset') {
                console.info("HEADER Location fetched in store ");
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