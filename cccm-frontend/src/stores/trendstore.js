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
        formType: '',
        userStartDate: null,
        userEndDate: null,
        data: null,
        filteredData: null,
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