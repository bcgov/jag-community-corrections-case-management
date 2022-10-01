import {defineStore} from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'

export const trendStore = defineStore('trend', {
    // state
    state: () =>({
        startDate: null,
        endDate: null,
        userStartDate: null,
        userEndDate: null,
        minStartDate: null,
        chartType: null,
        maxEndDate: null,
        interventionCount: 0,
        commentCount: 0,
        clientId: 100,
        advancedFilter: null,
        pointDateSelected: null,
        period: 'allPeriods',
        factors: [],
        advancedFilterOptions: []
    }),

    // actions
    actions: {
        updateStartAndEndDate(state, dates) {
            console.log("Dates affected %s %s", dates[0], dates[1]);
            if (dates[0] !== state.startDate) {
                state.startDate = dates[0];
            }
            if (dates[1] !== state.endDate) {
                state.endDate = dates[1];
            }
        },
        updatePointDateSelected(state, date) {
            state.pointDateSelected = date;
        },
        updatePeriod(state, period) {
            console.log("Period updated %s", period);
            console.log('start %s', state.startDate);
            state.userStartDate = null;
            state.userEndDate = null;
            state.period = period;
        },

        updateStartDate(state, startDate) {
            if (state.startDate !== startDate) {
                state.startDate = startDate;
            }
        },
        updateEndDate(state, endDate) {
            if (state.endDate !== endDate) {
                state.endDate = endDate;
            }
        },
        userUpdateStartDate(state, date) {
            console.log("User updated start date %s %o", date, state);
            state.userStartDate = date;
        },
        userUpdateEndDate(state, date) {
            console.log("User updated end date %s %o", date, state);
            state.userEndDate = date;

        },
        updateStartAndEndDateLimits(state, dates) {
            if (!state.minStartDate) {
                state.minStartDate = dates[0];
                state.maxEndDate = dates[1];
            }
        },
        updateCommentCount(state, count) {
            state.commentCount = count;
        },
        updateInterventionCount(state, count) {
            state.interventionCount = count;
        },
        updateChartType(state, chartType) {
            state.chartType = chartType;
        },
        updateAdvancedFilter(state, advancedFilter) {
            state.advancedFilter = advancedFilter;
        },
        updateAdvancedFilterOptions(state, advancedFilterOptions) {
            state.advancedFilterOptions = advancedFilterOptions;
        },
        updateFactors(state, factors) {
            console.log("Updating filter to match state %o %o", state, factors);
            state.factors = factors;
        }
    }
    // getters
})