import { defineStore } from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'

export const trendStore = defineStore('trend', {
    // state
    state: () => ({
        startDate: null,
        endDate: null,
        factors: [],
        period: 'allPeriods',
        chartType: 'line',
        userStartDate: null,
        userEndDate: null,
        data: null,
        minStartDate: null,
        maxEndDate: null,
        interventionCount: 0,
        commentCount: 0,
        dataLoaded: false,
        advancedFilter: null,
        pointDateSelected: null,
        advancedFilterOptions: []
    }),


})