import {defineStore} from 'pinia';

export const useStore = defineStore({ 
    id: "store", 
    // state
    state: () =>({
        locationCD: 'notset',
        locationDescription: 'notset',
    }),

    // actions

    // getters
})