import { identifier } from '@babel/types';
import axios from 'axios';
import { ScriptableLineSegmentContext } from 'chart.js';
//import { config } from 'process';

const axiosClient = axios.create({
    baseURL: config.VUE_APP_CCCM_API_ENDPOINT,
    //baseURL: "http://localhost:8080"
});

// function to async fetch the location info
export async function async_getUserDefaultLocation() {
    try {
        //console.log("VUE_APP_CCCM_API_ENDPOINT: ", config.VUE_APP_CCCM_API_ENDPOINT);
        const { data } = await axiosClient.get('/user/location');
        return [null, data];
    }catch (error) {
        return [error];
    }
}

// function to sync fetch the location info
export function getUserDefaultLocation() {
    try {
        //console.log("VUE_APP_CCCM_API_ENDPOINT: ", config.VUE_APP_CCCM_API_ENDPOINT);
        const data = axiosClient.get('/user/location');
        return [null, data];
    }catch (error) {
        return [error];
    }
}

// function to async fetch the locations info
export async function async_getUserLocations() {
    try {
        //console.log("VUE_APP_CCCM_API_ENDPOINT: ", config.VUE_APP_CCCM_API_ENDPOINT);
        const { data } = await axiosClient.get('/user/locations');
        return [null, data];
    }catch (error) {
        return [error];
    }
}

// function to sync fetch the locations info
export function getUserLocations() {
    try {
        //console.log("VUE_APP_CCCM_API_ENDPOINT: ", config.VUE_APP_CCCM_API_ENDPOINT);
        const data = axiosClient.get('/user/locations');
        return [null, data];
    }catch (error) {
        return [error];
    }
}


/**
 * Get the available forms for a client (CRNA/SARA) etc
 * @param clientNum
 * @param formTypeCd
 * @param currentPeriod
 *
 */
export async function getClientForms(clientNum: String,  currentPeriod: boolean, formTypeCd: String) {
    try{
        const { data } = await axiosClient.get(`/forms/client/${clientNum}`,{
            params: {
                formTypeCd: formTypeCd,
                currentPeriod: currentPeriod,
            }
        });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// get summary display of form
export async function getFormSummary(clientNum: string, formId: number, includeLinkedForm: boolean) {
    try {
        const { data } = await axiosClient.get(`/forms/client/summary/answers/${clientNum}/${formId}`,{
            params: {
                includeLinkedForm: includeLinkedForm,
            }
        });
        return [null, data];
    }catch (error) {
        return [error];
    }
}


// function to fetch the form.io template
export async function getFormDetails(clientNum: String, formId: number) {
    console.log("getFormdetails: ");
    try {
        const { data } = await axiosClient.get(`/forms/client/json/${clientNum}/${formId}`, {
            params: {
                includeOptionValues: true
            }
        });
        console.log("getFormdetails: ", data);
        return [null, data];
    }catch (error) {
        return [error];
    }
    // let tmpData = {
    //     "components": [
    //         {
    //             "input": false,
    //             "components": [
    //                 {
    //                     "input": false,
    //                     "tableView": false,
    //                     "tag": "p",
    //                     "id": "S01Q00",
    //                     "type": "htmlelement",
    //                     "content": "<span class=\"sectionTitleClass\">Needs Assessment</span></br>Dynamic Factors"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19654,
    //                                     "shortcut": "A",
    //                                     "label": "A. Pattern of stable and supportive relationships",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19655,
    //                                     "shortcut": "B",
    //                                     "label": "B. No current difficulties",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19656,
    //                                     "shortcut": "C",
    //                                     "label": "C. Occasional instability in relationships",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19657,
    //                                     "shortcut": "D",
    //                                     "label": "D. Very unstable pattern of relationships",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 1. Family Relationships",
    //                             "type": "radio",
    //                             "key": "S02Q01"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S02Q01_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         },
    //                         {
    //                             "input": true,
    //                             "defaultValue": false,
    //                             "tableView": false,
    //                             "label": "Intervention Needed",
    //                             "type": "checkbox",
    //                             "key": "S02Q01_intervention_checkbox"
    //                         },
    //                         {
    //                             "components": [
    //                                 {
    //                                     "input": false,
    //                                     "components": [
    //                                         {
    //                                             "input": false,
    //                                             "columns": [
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "widget": "choicesjs",
    //                                                             "data": {
    //                                                                 "values": [
    //                                                                     {
    //                                                                         "label": "Academic / Vocational Skills",
    //                                                                         "value": "ACVS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Attitude Assessment",
    //                                                                         "value": "ATAS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Behavioural / Emotional Stability (Personality)",
    //                                                                         "value": "BEES"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Companions / Significant Others",
    //                                                                         "value": "COSO"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Employment Pattern",
    //                                                                         "value": "EMPA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Family Relationships",
    //                                                                         "value": "FARE"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Financial Management",
    //                                                                         "value": "FIMA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Living Arrangements",
    //                                                                         "value": "LIAR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Other Intervention",
    //                                                                         "value": "OTHR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Pro-criminal Attitudes",
    //                                                                         "value": "PRCA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Substance Misuse",
    //                                                                         "value": "SUMI"
    //                                                                     }
    //                                                                 ]
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Select Intervention Type",
    //                                                             "type": "select",
    //                                                             "key": "S02Q01_intervention_type"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "conditional": {
    //                                                                 "show": true,
    //                                                                 "eq": "OTHR",
    //                                                                 "when": "S02Q01_intervention_type"
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Specify",
    //                                                             "type": "textfield",
    //                                                             "key": "S02Q01_intervention_specify"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 }
    //                                             ],
    //                                             "tableView": false,
    //                                             "label": "Columns",
    //                                             "type": "columns",
    //                                             "key": "columns"
    //                                         },
    //                                         {
    //                                             "input": true,
    //                                             "tableView": true,
    //                                             "label": "Description",
    //                                             "type": "textarea",
    //                                             "autoExpand": false,
    //                                             "key": "S02Q01_intervention_desc"
    //                                         }
    //                                     ],
    //                                     "defaultValue": [
    //                                         {
    //                                             "S02Q01_intervention_desc": "",
    //                                             "addIntervention": false,
    //                                             "S02Q01_intervention_specify": "",
    //                                             "S02Q01_intervention_type": ""
    //                                         }
    //                                     ],
    //                                     "dataGridLabel": true,
    //                                     "tableView": false,
    //                                     "label": "Panel",
    //                                     "type": "panel",
    //                                     "collapsible": false,
    //                                     "key": "panel",
    //                                     "hideLabel": true
    //                                 }
    //                             ],
    //                             "addAnother": "Add Intervention",
    //                             "enableRowGroups": false,
    //                             "conditional": {
    //                                 "show": true,
    //                                 "eq": "true",
    //                                 "when": "S02Q01_intervention_checkbox"
    //                             },
    //                             "tableView": false,
    //                             "reorder": false,
    //                             "label": "Interventions",
    //                             "layoutFixed": false,
    //                             "type": "datagrid",
    //                             "initEmpty": false,
    //                             "input": true,
    //                             "addAnotherPosition": "bottom",
    //                             "key": "S02Q01_intervention_datagrid"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q01",
    //                     "navText": "Family Relations.",
    //                     "id": "S01Q01",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19658,
    //                                     "shortcut": "A",
    //                                     "label": "A. Pattern of satisfactory living arrangements",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19659,
    //                                     "shortcut": "B",
    //                                     "label": "B. No current difficulties",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19660,
    //                                     "shortcut": "C",
    //                                     "label": "C. Occasional changes in residence or temporarily situated",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19661,
    //                                     "shortcut": "D",
    //                                     "label": "D. Frequent changes in residence and no permanent address",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 2. Living Arrangements",
    //                             "type": "radio",
    //                             "key": "S02Q02"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S02Q02_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         },
    //                         {
    //                             "input": true,
    //                             "defaultValue": false,
    //                             "tableView": false,
    //                             "label": "Intervention Needed",
    //                             "type": "checkbox",
    //                             "key": "S02Q02_intervention_checkbox"
    //                         },
    //                         {
    //                             "components": [
    //                                 {
    //                                     "input": false,
    //                                     "components": [
    //                                         {
    //                                             "input": false,
    //                                             "columns": [
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "widget": "choicesjs",
    //                                                             "data": {
    //                                                                 "values": [
    //                                                                     {
    //                                                                         "label": "Academic / Vocational Skills",
    //                                                                         "value": "ACVS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Attitude Assessment",
    //                                                                         "value": "ATAS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Behavioural / Emotional Stability (Personality)",
    //                                                                         "value": "BEES"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Companions / Significant Others",
    //                                                                         "value": "COSO"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Employment Pattern",
    //                                                                         "value": "EMPA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Family Relationships",
    //                                                                         "value": "FARE"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Financial Management",
    //                                                                         "value": "FIMA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Living Arrangements",
    //                                                                         "value": "LIAR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Other Intervention",
    //                                                                         "value": "OTHR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Pro-criminal Attitudes",
    //                                                                         "value": "PRCA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Substance Misuse",
    //                                                                         "value": "SUMI"
    //                                                                     }
    //                                                                 ]
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Select Intervention Type",
    //                                                             "type": "select",
    //                                                             "key": "S02Q02_intervention_type"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "conditional": {
    //                                                                 "show": true,
    //                                                                 "eq": "OTHR",
    //                                                                 "when": "S02Q02_intervention_type"
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Specify",
    //                                                             "type": "textfield",
    //                                                             "key": "S02Q02_intervention_specify"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 }
    //                                             ],
    //                                             "tableView": false,
    //                                             "label": "Columns",
    //                                             "type": "columns",
    //                                             "key": "columns"
    //                                         },
    //                                         {
    //                                             "input": true,
    //                                             "tableView": true,
    //                                             "label": "Description",
    //                                             "type": "textarea",
    //                                             "autoExpand": false,
    //                                             "key": "S02Q02_intervention_desc"
    //                                         }
    //                                     ],
    //                                     "defaultValue": [
    //                                         {
    //                                             "addIntervention": false,
    //                                             "S02Q02_intervention_desc": "",
    //                                             "S02Q02_intervention_type": "",
    //                                             "S02Q02_intervention_specify": ""
    //                                         }
    //                                     ],
    //                                     "dataGridLabel": true,
    //                                     "tableView": false,
    //                                     "label": "Panel",
    //                                     "type": "panel",
    //                                     "collapsible": false,
    //                                     "key": "panel",
    //                                     "hideLabel": true
    //                                 }
    //                             ],
    //                             "addAnother": "Add Intervention",
    //                             "enableRowGroups": false,
    //                             "conditional": {
    //                                 "show": true,
    //                                 "eq": "true",
    //                                 "when": "S02Q02_intervention_checkbox"
    //                             },
    //                             "tableView": false,
    //                             "reorder": false,
    //                             "label": "Interventions",
    //                             "layoutFixed": false,
    //                             "type": "datagrid",
    //                             "initEmpty": false,
    //                             "input": true,
    //                             "addAnotherPosition": "bottom",
    //                             "key": "S02Q02_intervention_datagrid"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q02",
    //                     "navText": "Living Arrange.",
    //                     "id": "S01Q02",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19662,
    //                                     "shortcut": "A",
    //                                     "label": "A. Pattern of non-criminal and/or positive associations",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19663,
    //                                     "shortcut": "B",
    //                                     "label": "B. Mostly non-criminal and/or positive associations",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19664,
    //                                     "shortcut": "C",
    //                                     "label": "C. Some criminal and/or negative associations",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19665,
    //                                     "shortcut": "D",
    //                                     "label": "D. Mostly criminal and/or negative associations",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 3. Companion/Significant Others",
    //                             "type": "radio",
    //                             "key": "S02Q03"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S02Q03_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         },
    //                         {
    //                             "input": true,
    //                             "defaultValue": false,
    //                             "tableView": false,
    //                             "label": "Intervention Needed",
    //                             "type": "checkbox",
    //                             "key": "S02Q03_intervention_checkbox"
    //                         },
    //                         {
    //                             "components": [
    //                                 {
    //                                     "input": false,
    //                                     "components": [
    //                                         {
    //                                             "input": false,
    //                                             "columns": [
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "widget": "choicesjs",
    //                                                             "data": {
    //                                                                 "values": [
    //                                                                     {
    //                                                                         "label": "Academic / Vocational Skills",
    //                                                                         "value": "ACVS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Attitude Assessment",
    //                                                                         "value": "ATAS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Behavioural / Emotional Stability (Personality)",
    //                                                                         "value": "BEES"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Companions / Significant Others",
    //                                                                         "value": "COSO"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Employment Pattern",
    //                                                                         "value": "EMPA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Family Relationships",
    //                                                                         "value": "FARE"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Financial Management",
    //                                                                         "value": "FIMA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Living Arrangements",
    //                                                                         "value": "LIAR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Other Intervention",
    //                                                                         "value": "OTHR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Pro-criminal Attitudes",
    //                                                                         "value": "PRCA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Substance Misuse",
    //                                                                         "value": "SUMI"
    //                                                                     }
    //                                                                 ]
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Select Intervention Type",
    //                                                             "type": "select",
    //                                                             "key": "S02Q03_intervention_type"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "conditional": {
    //                                                                 "show": true,
    //                                                                 "eq": "OTHR",
    //                                                                 "when": "S02Q03_intervention_type"
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Specify",
    //                                                             "type": "textfield",
    //                                                             "key": "S02Q03_intervention_specify"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 }
    //                                             ],
    //                                             "tableView": false,
    //                                             "label": "Columns",
    //                                             "type": "columns",
    //                                             "key": "columns"
    //                                         },
    //                                         {
    //                                             "input": true,
    //                                             "tableView": true,
    //                                             "label": "Description",
    //                                             "type": "textarea",
    //                                             "autoExpand": false,
    //                                             "key": "S02Q03_intervention_desc"
    //                                         }
    //                                     ],
    //                                     "defaultValue": [
    //                                         {
    //                                             "S02Q03_intervention_specify": "",
    //                                             "S02Q03_intervention_desc": "",
    //                                             "S02Q03_intervention_type": "",
    //                                             "addIntervention": false
    //                                         }
    //                                     ],
    //                                     "dataGridLabel": true,
    //                                     "tableView": false,
    //                                     "label": "Panel",
    //                                     "type": "panel",
    //                                     "collapsible": false,
    //                                     "key": "panel",
    //                                     "hideLabel": true
    //                                 }
    //                             ],
    //                             "addAnother": "Add Intervention",
    //                             "enableRowGroups": false,
    //                             "conditional": {
    //                                 "show": true,
    //                                 "eq": "true",
    //                                 "when": "S02Q03_intervention_checkbox"
    //                             },
    //                             "tableView": false,
    //                             "reorder": false,
    //                             "label": "Interventions",
    //                             "layoutFixed": false,
    //                             "type": "datagrid",
    //                             "initEmpty": false,
    //                             "input": true,
    //                             "addAnotherPosition": "bottom",
    //                             "key": "S02Q03_intervention_datagrid"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q03",
    //                     "navText": "Comp./ Sig. Others",
    //                     "id": "S01Q03",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19666,
    //                                     "shortcut": "B",
    //                                     "label": "B. No current difficulties",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19667,
    //                                     "shortcut": "C",
    //                                     "label": "C. Level of skills causing minor interference",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19668,
    //                                     "shortcut": "D",
    //                                     "label": "D. Level of skills causing serious interference",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 4. Academic/Vocational Skills",
    //                             "type": "radio",
    //                             "key": "S02Q04"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S02Q04_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         },
    //                         {
    //                             "input": true,
    //                             "defaultValue": false,
    //                             "tableView": false,
    //                             "label": "Intervention Needed",
    //                             "type": "checkbox",
    //                             "key": "S02Q04_intervention_checkbox"
    //                         },
    //                         {
    //                             "components": [
    //                                 {
    //                                     "input": false,
    //                                     "components": [
    //                                         {
    //                                             "input": false,
    //                                             "columns": [
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "widget": "choicesjs",
    //                                                             "data": {
    //                                                                 "values": [
    //                                                                     {
    //                                                                         "label": "Academic / Vocational Skills",
    //                                                                         "value": "ACVS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Attitude Assessment",
    //                                                                         "value": "ATAS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Behavioural / Emotional Stability (Personality)",
    //                                                                         "value": "BEES"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Companions / Significant Others",
    //                                                                         "value": "COSO"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Employment Pattern",
    //                                                                         "value": "EMPA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Family Relationships",
    //                                                                         "value": "FARE"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Financial Management",
    //                                                                         "value": "FIMA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Living Arrangements",
    //                                                                         "value": "LIAR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Other Intervention",
    //                                                                         "value": "OTHR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Pro-criminal Attitudes",
    //                                                                         "value": "PRCA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Substance Misuse",
    //                                                                         "value": "SUMI"
    //                                                                     }
    //                                                                 ]
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Select Intervention Type",
    //                                                             "type": "select",
    //                                                             "key": "S02Q04_intervention_type"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "conditional": {
    //                                                                 "show": true,
    //                                                                 "eq": "OTHR",
    //                                                                 "when": "S02Q04_intervention_type"
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Specify",
    //                                                             "type": "textfield",
    //                                                             "key": "S02Q04_intervention_specify"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 }
    //                                             ],
    //                                             "tableView": false,
    //                                             "label": "Columns",
    //                                             "type": "columns",
    //                                             "key": "columns"
    //                                         },
    //                                         {
    //                                             "input": true,
    //                                             "tableView": true,
    //                                             "label": "Description",
    //                                             "type": "textarea",
    //                                             "autoExpand": false,
    //                                             "key": "S02Q04_intervention_desc"
    //                                         }
    //                                     ],
    //                                     "defaultValue": [
    //                                         {
    //                                             "addIntervention": false,
    //                                             "S02Q04_intervention_specify": "",
    //                                             "S02Q04_intervention_type": "",
    //                                             "S02Q04_intervention_desc": ""
    //                                         }
    //                                     ],
    //                                     "dataGridLabel": true,
    //                                     "tableView": false,
    //                                     "label": "Panel",
    //                                     "type": "panel",
    //                                     "collapsible": false,
    //                                     "key": "panel",
    //                                     "hideLabel": true
    //                                 }
    //                             ],
    //                             "addAnother": "Add Intervention",
    //                             "enableRowGroups": false,
    //                             "conditional": {
    //                                 "show": true,
    //                                 "eq": "true",
    //                                 "when": "S02Q04_intervention_checkbox"
    //                             },
    //                             "tableView": false,
    //                             "reorder": false,
    //                             "label": "Interventions",
    //                             "layoutFixed": false,
    //                             "type": "datagrid",
    //                             "initEmpty": false,
    //                             "input": true,
    //                             "addAnotherPosition": "bottom",
    //                             "key": "S02Q04_intervention_datagrid"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q04",
    //                     "navText": "Acad. / Voca. Skills",
    //                     "id": "S01Q04",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19669,
    //                                     "shortcut": "A",
    //                                     "label": "A. Stable pattern of employment",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19670,
    //                                     "shortcut": "B",
    //                                     "label": "B. No current difficulties",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19671,
    //                                     "shortcut": "C",
    //                                     "label": "C. Employment situation causing minor adjustment problems",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19672,
    //                                     "shortcut": "D",
    //                                     "label": "D. Employment situation causing major adjustment problems",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 5. Employment Pattern",
    //                             "type": "radio",
    //                             "key": "S02Q05"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S02Q05_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         },
    //                         {
    //                             "input": true,
    //                             "defaultValue": false,
    //                             "tableView": false,
    //                             "label": "Intervention Needed",
    //                             "type": "checkbox",
    //                             "key": "S02Q05_intervention_checkbox"
    //                         },
    //                         {
    //                             "components": [
    //                                 {
    //                                     "input": false,
    //                                     "components": [
    //                                         {
    //                                             "input": false,
    //                                             "columns": [
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "widget": "choicesjs",
    //                                                             "data": {
    //                                                                 "values": [
    //                                                                     {
    //                                                                         "label": "Academic / Vocational Skills",
    //                                                                         "value": "ACVS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Attitude Assessment",
    //                                                                         "value": "ATAS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Behavioural / Emotional Stability (Personality)",
    //                                                                         "value": "BEES"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Companions / Significant Others",
    //                                                                         "value": "COSO"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Employment Pattern",
    //                                                                         "value": "EMPA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Family Relationships",
    //                                                                         "value": "FARE"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Financial Management",
    //                                                                         "value": "FIMA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Living Arrangements",
    //                                                                         "value": "LIAR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Other Intervention",
    //                                                                         "value": "OTHR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Pro-criminal Attitudes",
    //                                                                         "value": "PRCA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Substance Misuse",
    //                                                                         "value": "SUMI"
    //                                                                     }
    //                                                                 ]
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Select Intervention Type",
    //                                                             "type": "select",
    //                                                             "key": "S02Q05_intervention_type"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "conditional": {
    //                                                                 "show": true,
    //                                                                 "eq": "OTHR",
    //                                                                 "when": "S02Q05_intervention_type"
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Specify",
    //                                                             "type": "textfield",
    //                                                             "key": "S02Q05_intervention_specify"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 }
    //                                             ],
    //                                             "tableView": false,
    //                                             "label": "Columns",
    //                                             "type": "columns",
    //                                             "key": "columns"
    //                                         },
    //                                         {
    //                                             "input": true,
    //                                             "tableView": true,
    //                                             "label": "Description",
    //                                             "type": "textarea",
    //                                             "autoExpand": false,
    //                                             "key": "S02Q05_intervention_desc"
    //                                         }
    //                                     ],
    //                                     "defaultValue": [
    //                                         {
    //                                             "S02Q05_intervention_type": "",
    //                                             "S02Q05_intervention_specify": "",
    //                                             "addIntervention": false,
    //                                             "S02Q05_intervention_desc": ""
    //                                         }
    //                                     ],
    //                                     "dataGridLabel": true,
    //                                     "tableView": false,
    //                                     "label": "Panel",
    //                                     "type": "panel",
    //                                     "collapsible": false,
    //                                     "key": "panel",
    //                                     "hideLabel": true
    //                                 }
    //                             ],
    //                             "addAnother": "Add Intervention",
    //                             "enableRowGroups": false,
    //                             "conditional": {
    //                                 "show": true,
    //                                 "eq": "true",
    //                                 "when": "S02Q05_intervention_checkbox"
    //                             },
    //                             "tableView": false,
    //                             "reorder": false,
    //                             "label": "Interventions",
    //                             "layoutFixed": false,
    //                             "type": "datagrid",
    //                             "initEmpty": false,
    //                             "input": true,
    //                             "addAnotherPosition": "bottom",
    //                             "key": "S02Q05_intervention_datagrid"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q05",
    //                     "navText": "Empl. Pattern",
    //                     "id": "S01Q05",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19673,
    //                                     "shortcut": "A",
    //                                     "label": "A. Pattern of effective management",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19674,
    //                                     "shortcut": "B",
    //                                     "label": "B. No current difficulties",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19675,
    //                                     "shortcut": "C",
    //                                     "label": "C. Situational or minor difficulties",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19676,
    //                                     "shortcut": "D",
    //                                     "label": "D. Severe difficulties",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 6. Financial Management",
    //                             "type": "radio",
    //                             "key": "S02Q06"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S02Q06_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         },
    //                         {
    //                             "input": true,
    //                             "defaultValue": false,
    //                             "tableView": false,
    //                             "label": "Intervention Needed",
    //                             "type": "checkbox",
    //                             "key": "S02Q06_intervention_checkbox"
    //                         },
    //                         {
    //                             "components": [
    //                                 {
    //                                     "input": false,
    //                                     "components": [
    //                                         {
    //                                             "input": false,
    //                                             "columns": [
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "widget": "choicesjs",
    //                                                             "data": {
    //                                                                 "values": [
    //                                                                     {
    //                                                                         "label": "Academic / Vocational Skills",
    //                                                                         "value": "ACVS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Attitude Assessment",
    //                                                                         "value": "ATAS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Behavioural / Emotional Stability (Personality)",
    //                                                                         "value": "BEES"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Companions / Significant Others",
    //                                                                         "value": "COSO"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Employment Pattern",
    //                                                                         "value": "EMPA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Family Relationships",
    //                                                                         "value": "FARE"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Financial Management",
    //                                                                         "value": "FIMA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Living Arrangements",
    //                                                                         "value": "LIAR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Other Intervention",
    //                                                                         "value": "OTHR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Pro-criminal Attitudes",
    //                                                                         "value": "PRCA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Substance Misuse",
    //                                                                         "value": "SUMI"
    //                                                                     }
    //                                                                 ]
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Select Intervention Type",
    //                                                             "type": "select",
    //                                                             "key": "S02Q06_intervention_type"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "conditional": {
    //                                                                 "show": true,
    //                                                                 "eq": "OTHR",
    //                                                                 "when": "S02Q06_intervention_type"
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Specify",
    //                                                             "type": "textfield",
    //                                                             "key": "S02Q06_intervention_specify"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 }
    //                                             ],
    //                                             "tableView": false,
    //                                             "label": "Columns",
    //                                             "type": "columns",
    //                                             "key": "columns"
    //                                         },
    //                                         {
    //                                             "input": true,
    //                                             "tableView": true,
    //                                             "label": "Description",
    //                                             "type": "textarea",
    //                                             "autoExpand": false,
    //                                             "key": "S02Q06_intervention_desc"
    //                                         }
    //                                     ],
    //                                     "defaultValue": [
    //                                         {
    //                                             "addIntervention": false,
    //                                             "S02Q06_intervention_desc": "",
    //                                             "S02Q06_intervention_type": "",
    //                                             "S02Q06_intervention_specify": ""
    //                                         }
    //                                     ],
    //                                     "dataGridLabel": true,
    //                                     "tableView": false,
    //                                     "label": "Panel",
    //                                     "type": "panel",
    //                                     "collapsible": false,
    //                                     "key": "panel",
    //                                     "hideLabel": true
    //                                 }
    //                             ],
    //                             "addAnother": "Add Intervention",
    //                             "enableRowGroups": false,
    //                             "conditional": {
    //                                 "show": true,
    //                                 "eq": "true",
    //                                 "when": "S02Q06_intervention_checkbox"
    //                             },
    //                             "tableView": false,
    //                             "reorder": false,
    //                             "label": "Interventions",
    //                             "layoutFixed": false,
    //                             "type": "datagrid",
    //                             "initEmpty": false,
    //                             "input": true,
    //                             "addAnotherPosition": "bottom",
    //                             "key": "S02Q06_intervention_datagrid"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q06",
    //                     "navText": "Finance Mgmt",
    //                     "id": "S01Q06",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19677,
    //                                     "shortcut": "D",
    //                                     "label": "D. Severe behavioural/emotional problems that indicate significant need for assistance",
    //                                     "value": "D"
    //                                 },
    //                                 {
    //                                     "answerId": 19678,
    //                                     "shortcut": "B",
    //                                     "label": "B. No current difficulties",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19679,
    //                                     "shortcut": "C",
    //                                     "label": "C. Behavioural/emotional problems that indicate some need for assistance",
    //                                     "value": "C"
    //                                 }
    //                             ],
    //                             "label": " 7. Behavioural/Emotional Stability",
    //                             "type": "radio",
    //                             "key": "S02Q07"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S02Q07_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         },
    //                         {
    //                             "input": true,
    //                             "defaultValue": false,
    //                             "tableView": false,
    //                             "label": "Intervention Needed",
    //                             "type": "checkbox",
    //                             "key": "S02Q07_intervention_checkbox"
    //                         },
    //                         {
    //                             "components": [
    //                                 {
    //                                     "input": false,
    //                                     "components": [
    //                                         {
    //                                             "input": false,
    //                                             "columns": [
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "widget": "choicesjs",
    //                                                             "data": {
    //                                                                 "values": [
    //                                                                     {
    //                                                                         "label": "Academic / Vocational Skills",
    //                                                                         "value": "ACVS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Attitude Assessment",
    //                                                                         "value": "ATAS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Behavioural / Emotional Stability (Personality)",
    //                                                                         "value": "BEES"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Companions / Significant Others",
    //                                                                         "value": "COSO"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Employment Pattern",
    //                                                                         "value": "EMPA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Family Relationships",
    //                                                                         "value": "FARE"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Financial Management",
    //                                                                         "value": "FIMA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Living Arrangements",
    //                                                                         "value": "LIAR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Other Intervention",
    //                                                                         "value": "OTHR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Pro-criminal Attitudes",
    //                                                                         "value": "PRCA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Substance Misuse",
    //                                                                         "value": "SUMI"
    //                                                                     }
    //                                                                 ]
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Select Intervention Type",
    //                                                             "type": "select",
    //                                                             "key": "S02Q07_intervention_type"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "conditional": {
    //                                                                 "show": true,
    //                                                                 "eq": "OTHR",
    //                                                                 "when": "S02Q07_intervention_type"
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Specify",
    //                                                             "type": "textfield",
    //                                                             "key": "S02Q07_intervention_specify"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 }
    //                                             ],
    //                                             "tableView": false,
    //                                             "label": "Columns",
    //                                             "type": "columns",
    //                                             "key": "columns"
    //                                         },
    //                                         {
    //                                             "input": true,
    //                                             "tableView": true,
    //                                             "label": "Description",
    //                                             "type": "textarea",
    //                                             "autoExpand": false,
    //                                             "key": "S02Q07_intervention_desc"
    //                                         }
    //                                     ],
    //                                     "defaultValue": [
    //                                         {
    //                                             "addIntervention": false,
    //                                             "S02Q07_intervention_specify": "",
    //                                             "S02Q07_intervention_type": "",
    //                                             "S02Q07_intervention_desc": ""
    //                                         }
    //                                     ],
    //                                     "dataGridLabel": true,
    //                                     "tableView": false,
    //                                     "label": "Panel",
    //                                     "type": "panel",
    //                                     "collapsible": false,
    //                                     "key": "panel",
    //                                     "hideLabel": true
    //                                 }
    //                             ],
    //                             "addAnother": "Add Intervention",
    //                             "enableRowGroups": false,
    //                             "conditional": {
    //                                 "show": true,
    //                                 "eq": "true",
    //                                 "when": "S02Q07_intervention_checkbox"
    //                             },
    //                             "tableView": false,
    //                             "reorder": false,
    //                             "label": "Interventions",
    //                             "layoutFixed": false,
    //                             "type": "datagrid",
    //                             "initEmpty": false,
    //                             "input": true,
    //                             "addAnotherPosition": "bottom",
    //                             "key": "S02Q07_intervention_datagrid"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q07",
    //                     "navText": "Beh./ Emo. Stability",
    //                     "id": "S01Q07",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19680,
    //                                     "shortcut": "B",
    //                                     "label": "B. No current usage or difficulties",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19681,
    //                                     "shortcut": "C",
    //                                     "label": "C. Some usage causing moderate adjustment problems",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19682,
    //                                     "shortcut": "D",
    //                                     "label": "D. Frequent or uncontrolled usage causing serious adjustment problems",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 8. Substance Misuse",
    //                             "type": "radio",
    //                             "key": "S02Q08"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S02Q08_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         },
    //                         {
    //                             "input": true,
    //                             "defaultValue": false,
    //                             "tableView": false,
    //                             "label": "Intervention Needed",
    //                             "type": "checkbox",
    //                             "key": "S02Q08_intervention_checkbox"
    //                         },
    //                         {
    //                             "components": [
    //                                 {
    //                                     "input": false,
    //                                     "components": [
    //                                         {
    //                                             "input": false,
    //                                             "columns": [
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "widget": "choicesjs",
    //                                                             "data": {
    //                                                                 "values": [
    //                                                                     {
    //                                                                         "label": "Academic / Vocational Skills",
    //                                                                         "value": "ACVS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Attitude Assessment",
    //                                                                         "value": "ATAS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Behavioural / Emotional Stability (Personality)",
    //                                                                         "value": "BEES"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Companions / Significant Others",
    //                                                                         "value": "COSO"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Employment Pattern",
    //                                                                         "value": "EMPA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Family Relationships",
    //                                                                         "value": "FARE"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Financial Management",
    //                                                                         "value": "FIMA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Living Arrangements",
    //                                                                         "value": "LIAR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Other Intervention",
    //                                                                         "value": "OTHR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Pro-criminal Attitudes",
    //                                                                         "value": "PRCA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Substance Misuse",
    //                                                                         "value": "SUMI"
    //                                                                     }
    //                                                                 ]
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Select Intervention Type",
    //                                                             "type": "select",
    //                                                             "key": "S02Q08_intervention_type"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "conditional": {
    //                                                                 "show": true,
    //                                                                 "eq": "OTHR",
    //                                                                 "when": "S02Q08_intervention_type"
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Specify",
    //                                                             "type": "textfield",
    //                                                             "key": "S02Q08_intervention_specify"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 }
    //                                             ],
    //                                             "tableView": false,
    //                                             "label": "Columns",
    //                                             "type": "columns",
    //                                             "key": "columns"
    //                                         },
    //                                         {
    //                                             "input": true,
    //                                             "tableView": true,
    //                                             "label": "Description",
    //                                             "type": "textarea",
    //                                             "autoExpand": false,
    //                                             "key": "S02Q08_intervention_desc"
    //                                         }
    //                                     ],
    //                                     "defaultValue": [
    //                                         {
    //                                             "S02Q08_intervention_specify": "",
    //                                             "S02Q08_intervention_type": "",
    //                                             "S02Q08_intervention_desc": "",
    //                                             "addIntervention": false
    //                                         }
    //                                     ],
    //                                     "dataGridLabel": true,
    //                                     "tableView": false,
    //                                     "label": "Panel",
    //                                     "type": "panel",
    //                                     "collapsible": false,
    //                                     "key": "panel",
    //                                     "hideLabel": true
    //                                 }
    //                             ],
    //                             "addAnother": "Add Intervention",
    //                             "enableRowGroups": false,
    //                             "conditional": {
    //                                 "show": true,
    //                                 "eq": "true",
    //                                 "when": "S02Q08_intervention_checkbox"
    //                             },
    //                             "tableView": false,
    //                             "reorder": false,
    //                             "label": "Interventions",
    //                             "layoutFixed": false,
    //                             "type": "datagrid",
    //                             "initEmpty": false,
    //                             "input": true,
    //                             "addAnotherPosition": "bottom",
    //                             "key": "S02Q08_intervention_datagrid"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q08",
    //                     "navText": "Sub. Misuse",
    //                     "id": "S01Q08",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19683,
    //                                     "shortcut": "A",
    //                                     "label": "A. Actively involved and responding consistently well to assistance",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19684,
    //                                     "shortcut": "B",
    //                                     "label": "B. Motivated to change, receptive to assistance",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19685,
    //                                     "shortcut": "C",
    //                                     "label": "C. Recognizes problem areas but not receptive to assistance",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19686,
    //                                     "shortcut": "D",
    //                                     "label": "D. Unable to recognize problem areas and not receptive to assistance",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 9. Attitude",
    //                             "type": "radio",
    //                             "key": "S02Q09"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S02Q09_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         },
    //                         {
    //                             "input": true,
    //                             "defaultValue": false,
    //                             "tableView": false,
    //                             "label": "Intervention Needed",
    //                             "type": "checkbox",
    //                             "key": "S02Q09_intervention_checkbox"
    //                         },
    //                         {
    //                             "components": [
    //                                 {
    //                                     "input": false,
    //                                     "components": [
    //                                         {
    //                                             "input": false,
    //                                             "columns": [
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "widget": "choicesjs",
    //                                                             "data": {
    //                                                                 "values": [
    //                                                                     {
    //                                                                         "label": "Academic / Vocational Skills",
    //                                                                         "value": "ACVS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Attitude Assessment",
    //                                                                         "value": "ATAS"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Behavioural / Emotional Stability (Personality)",
    //                                                                         "value": "BEES"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Companions / Significant Others",
    //                                                                         "value": "COSO"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Employment Pattern",
    //                                                                         "value": "EMPA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Family Relationships",
    //                                                                         "value": "FARE"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Financial Management",
    //                                                                         "value": "FIMA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Living Arrangements",
    //                                                                         "value": "LIAR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Other Intervention",
    //                                                                         "value": "OTHR"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Pro-criminal Attitudes",
    //                                                                         "value": "PRCA"
    //                                                                     },
    //                                                                     {
    //                                                                         "label": "Substance Misuse",
    //                                                                         "value": "SUMI"
    //                                                                     }
    //                                                                 ]
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Select Intervention Type",
    //                                                             "type": "select",
    //                                                             "key": "S02Q09_intervention_type"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "conditional": {
    //                                                                 "show": true,
    //                                                                 "eq": "OTHR",
    //                                                                 "when": "S02Q09_intervention_type"
    //                                                             },
    //                                                             "tableView": true,
    //                                                             "label": "Specify",
    //                                                             "type": "textfield",
    //                                                             "key": "S02Q09_intervention_specify"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 6,
    //                                                     "width": 6,
    //                                                     "push": 0
    //                                                 }
    //                                             ],
    //                                             "tableView": false,
    //                                             "label": "Columns",
    //                                             "type": "columns",
    //                                             "key": "columns"
    //                                         },
    //                                         {
    //                                             "input": true,
    //                                             "tableView": true,
    //                                             "label": "Description",
    //                                             "type": "textarea",
    //                                             "autoExpand": false,
    //                                             "key": "S02Q09_intervention_desc"
    //                                         }
    //                                     ],
    //                                     "defaultValue": [
    //                                         {
    //                                             "addIntervention": false,
    //                                             "S02Q09_intervention_type": "",
    //                                             "S02Q09_intervention_specify": "",
    //                                             "S02Q09_intervention_desc": ""
    //                                         }
    //                                     ],
    //                                     "dataGridLabel": true,
    //                                     "tableView": false,
    //                                     "label": "Panel",
    //                                     "type": "panel",
    //                                     "collapsible": false,
    //                                     "key": "panel",
    //                                     "hideLabel": true
    //                                 }
    //                             ],
    //                             "addAnother": "Add Intervention",
    //                             "enableRowGroups": false,
    //                             "conditional": {
    //                                 "show": true,
    //                                 "eq": "true",
    //                                 "when": "S02Q09_intervention_checkbox"
    //                             },
    //                             "tableView": false,
    //                             "reorder": false,
    //                             "label": "Interventions",
    //                             "layoutFixed": false,
    //                             "type": "datagrid",
    //                             "initEmpty": false,
    //                             "input": true,
    //                             "addAnotherPosition": "bottom",
    //                             "key": "S02Q09_intervention_datagrid"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q09",
    //                     "navText": "Attitude",
    //                     "id": "S01Q09",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19650,
    //                                     "shortcut": "L",
    //                                     "label": "Low",
    //                                     "value": "L"
    //                                 },
    //                                 {
    //                                     "answerId": 19649,
    //                                     "shortcut": "M",
    //                                     "label": "Medium",
    //                                     "value": "M"
    //                                 },
    //                                 {
    //                                     "answerId": 19648,
    //                                     "shortcut": "H",
    //                                     "label": "High",
    //                                     "value": "H"
    //                                 }
    //                             ],
    //                             "label": "Overall Needs Rating",
    //                             "type": "radio",
    //                             "key": "S01Q02"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S01Q02_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q10",
    //                     "navText": "Needs Rating",
    //                     "id": "S01Q10",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "label": "Additional Comments",
    //                             "type": "textarea",
    //                             "key": "S02Q10"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S01Q11",
    //                     "navText": " 10. Needs Remarks",
    //                     "id": "S01Q11",
    //                     "type": "well"
    //                 }
    //             ],
    //             "tableView": false,
    //             "custom_subNavOn": true,
    //             "label": "Needs Assessment",
    //             "id": "S01",
    //             "type": "well",
    //             "key": "S02"
    //         },
    //         {
    //             "input": false,
    //             "components": [
    //                 {
    //                     "input": false,
    //                     "tableView": false,
    //                     "tag": "p",
    //                     "id": "S02Q00",
    //                     "type": "htmlelement",
    //                     "content": "<span class=\"sectionTitleClass\">Risk  Assessment - Criminal History</span></br>Static Factors"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19687,
    //                                     "shortcut": "A",
    //                                     "label": "A. None",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19688,
    //                                     "shortcut": "B",
    //                                     "label": "B. One",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19689,
    //                                     "shortcut": "C",
    //                                     "label": "C. Two",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19690,
    //                                     "shortcut": "D",
    //                                     "label": "D. Three or more",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 1. # of Current Convictions/Peace Bonds",
    //                             "type": "radio",
    //                             "key": "S03Q01"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S03Q01_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q01",
    //                     "navText": "# Conv./ Peace Bonds",
    //                     "id": "S02Q01",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19695,
    //                                     "shortcut": "A",
    //                                     "label": "A. None",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19696,
    //                                     "shortcut": "B",
    //                                     "label": "B. One",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19697,
    //                                     "shortcut": "C",
    //                                     "label": "C. 2 to 4",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19698,
    //                                     "shortcut": "D",
    //                                     "label": "D. 5 or more",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 2. # of Prior Court Dispositions",
    //                             "type": "radio",
    //                             "key": "S03Q02"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S03Q02_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q02",
    //                     "navText": "# Prior Crt. Disp.",
    //                     "id": "S02Q02",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19699,
    //                                     "shortcut": "A",
    //                                     "label": "A. None",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19700,
    //                                     "shortcut": "B",
    //                                     "label": "B. One",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19701,
    //                                     "shortcut": "C",
    //                                     "label": "C. Two",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19702,
    //                                     "shortcut": "D",
    //                                     "label": "D. Three or More",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 3. # of Prior Supervision Periods",
    //                             "type": "radio",
    //                             "key": "S03Q03"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S03Q03_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q03",
    //                     "navText": "# Prior Sup. Periods",
    //                     "id": "S02Q03",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19703,
    //                                     "shortcut": "A",
    //                                     "label": "A. None",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19704,
    //                                     "shortcut": "B",
    //                                     "label": "B. One",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19705,
    //                                     "shortcut": "C",
    //                                     "label": "C. Two",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19706,
    //                                     "shortcut": "D",
    //                                     "label": "D. Three or More",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 4. # of Prior Failures to Comply",
    //                             "type": "radio",
    //                             "key": "S03Q04"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S03Q04_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q04",
    //                     "navText": "# Prior Fail. to Comply",
    //                     "id": "S02Q04",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19707,
    //                                     "shortcut": "A",
    //                                     "label": "A. 25+",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19708,
    //                                     "shortcut": "B",
    //                                     "label": "B. 18 to 24",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19709,
    //                                     "shortcut": "C",
    //                                     "label": "C. 14 to 17",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19710,
    //                                     "shortcut": "D",
    //                                     "label": "D. 13 & under",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 5. Age at First Arrest / Conviction",
    //                             "type": "radio",
    //                             "key": "S03Q05"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S03Q05_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q05",
    //                     "navText": "Age at First Arr./Conv.",
    //                     "id": "S02Q05",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19711,
    //                                     "shortcut": "A",
    //                                     "label": "A. No",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19712,
    //                                     "shortcut": "D",
    //                                     "label": "D. Yes",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 6. Ever Imprisoned After a Conviction",
    //                             "type": "radio",
    //                             "key": "S03Q06"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S03Q06_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q06",
    //                     "navText": "Imprisoned After Conv.",
    //                     "id": "S02Q06",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19713,
    //                                     "shortcut": "A",
    //                                     "label": "No",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19714,
    //                                     "shortcut": "D",
    //                                     "label": "Yes",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 7. Escape History",
    //                             "type": "radio",
    //                             "key": "S03Q07"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S03Q07_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q07",
    //                     "navText": "Esc. History",
    //                     "id": "S02Q07",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19715,
    //                                     "shortcut": "A",
    //                                     "label": "A. No",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19716,
    //                                     "shortcut": "D",
    //                                     "label": "D. Yes",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 8. Weapon Use / Threat",
    //                             "type": "radio",
    //                             "key": "S03Q08"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S03Q08_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q08",
    //                     "navText": "Weap. use/ Threats",
    //                     "id": "S02Q08",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19717,
    //                                     "shortcut": "A",
    //                                     "label": "A. None",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19718,
    //                                     "shortcut": "B",
    //                                     "label": "B. Low",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19719,
    //                                     "shortcut": "C",
    //                                     "label": "C. Medium",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19720,
    //                                     "shortcut": "D",
    //                                     "label": "D. High",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 9. Frequency of Violence",
    //                             "type": "radio",
    //                             "key": "S03Q09"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S03Q09_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q09",
    //                     "navText": "Freq. of Violence",
    //                     "id": "S02Q09",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19691,
    //                                     "shortcut": "A",
    //                                     "label": "A. None",
    //                                     "value": "A"
    //                                 },
    //                                 {
    //                                     "answerId": 19692,
    //                                     "shortcut": "B",
    //                                     "label": "B. Low",
    //                                     "value": "B"
    //                                 },
    //                                 {
    //                                     "answerId": 19693,
    //                                     "shortcut": "C",
    //                                     "label": "C. Medium",
    //                                     "value": "C"
    //                                 },
    //                                 {
    //                                     "answerId": 19694,
    //                                     "shortcut": "D",
    //                                     "label": "D. High",
    //                                     "value": "D"
    //                                 }
    //                             ],
    //                             "label": " 10. Severity of Violence",
    //                             "type": "radio",
    //                             "key": "S03Q10"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S03Q10_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q10",
    //                     "navText": "Sev. of Violence.",
    //                     "id": "S02Q10",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19653,
    //                                     "shortcut": "L",
    //                                     "label": "Low",
    //                                     "value": "L"
    //                                 },
    //                                 {
    //                                     "answerId": 19652,
    //                                     "shortcut": "M",
    //                                     "label": "Medium",
    //                                     "value": "M"
    //                                 },
    //                                 {
    //                                     "answerId": 19651,
    //                                     "shortcut": "H",
    //                                     "label": "High",
    //                                     "value": "H"
    //                                 }
    //                             ],
    //                             "label": "Overall Risk Rating",
    //                             "type": "radio",
    //                             "key": "S01Q03"
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S01Q03_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q11",
    //                     "navText": "Risk Rating",
    //                     "id": "S02Q11",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "label": "Additional Comments",
    //                             "type": "textarea",
    //                             "key": "S03Q11"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S02Q12",
    //                     "navText": "Comments",
    //                     "id": "S02Q12",
    //                     "type": "well"
    //                 }
    //             ],
    //             "tableView": false,
    //             "custom_subNavOn": true,
    //             "label": "Risk  Assessment - Criminal History",
    //             "id": "S02",
    //             "type": "well",
    //             "key": "S03"
    //         },
    //         {
    //             "input": false,
    //             "components": [
    //                 {
    //                     "input": false,
    //                     "tableView": false,
    //                     "tag": "p",
    //                     "id": "S03Q00",
    //                     "type": "htmlelement",
    //                     "content": "<span class=\"sectionTitleClass\">Supervision Rating</span></br>Dynamic Factors"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "refreshOnChange": true,
    //                             "tag": "div",
    //                             "label": "Overall Needs Rating",
    //                             "type": "htmlelement",
    //                             "content": "<div class='mb-3 mt-3'><Strong>Overall Needs Rating: </Strong>{{data.S01Q02}}</div>",
    //                             "key": "READONLY_S01Q02"
    //                         },
    //                         {
    //                             "refreshOnChange": true,
    //                             "tag": "div",
    //                             "type": "htmlelement",
    //                             "content": "<div class='mb-3 mt-3'><Strong>Comments: </Strong>{{data.S01Q02_COMMENT}}</div>"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S03Q01",
    //                     "navText": " 2. Overall Needs Assessment Rating",
    //                     "id": "S03Q01",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "refreshOnChange": true,
    //                             "tag": "div",
    //                             "label": "Overall Risk Rating",
    //                             "type": "htmlelement",
    //                             "content": "<div class='mb-3 mt-3'><Strong>Overall Risk Rating: </Strong>{{data.S01Q03}}</div>",
    //                             "key": "READONLY_S01Q03"
    //                         },
    //                         {
    //                             "refreshOnChange": true,
    //                             "tag": "div",
    //                             "type": "htmlelement",
    //                             "content": "<div class='mb-3 mt-3'><Strong>Comments: </Strong>{{data.S01Q03_COMMENT}}</div>"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S03Q02",
    //                     "navText": " 3. Risk  Assessment - Criminal History Rating",
    //                     "id": "S03Q02",
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "input": true,
    //                             "optionsLabelPosition": "right",
    //                             "values": [
    //                                 {
    //                                     "answerId": 19647,
    //                                     "shortcut": "L",
    //                                     "label": "Low",
    //                                     "value": "L"
    //                                 },
    //                                 {
    //                                     "answerId": 19646,
    //                                     "shortcut": "M",
    //                                     "label": "Medium",
    //                                     "value": "M"
    //                                 },
    //                                 {
    //                                     "answerId": 19645,
    //                                     "shortcut": "H",
    //                                     "label": "High",
    //                                     "value": "H"
    //                                 }
    //                             ],
    //                             "label": "Overall Supervision Rating",
    //                             "type": "radio",
    //                             "key": "S01Q01",
    //                             "validate": {
    //                                 "required": true
    //                             }
    //                         },
    //                         {
    //                             "input": true,
    //                             "tableView": true,
    //                             "label": "Comments",
    //                             "type": "textarea",
    //                             "showCharCount": true,
    //                             "autoExpand": false,
    //                             "key": "S01Q01_COMMENT",
    //                             "validate": {
    //                                 "maxLength": 2000
    //                             }
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "customClass": "question_panel_S03Q03",
    //                     "navText": " 1. Overall Supervision Level",
    //                     "id": "S03Q03",
    //                     "type": "well"
    //                 }
    //             ],
    //             "tableView": false,
    //             "custom_subNavOn": false,
    //             "label": "Supervision Rating",
    //             "id": "S03",
    //             "type": "well",
    //             "key": "S01"
    //         },
    //         {
    //             "input": false,
    //             "components": [
    //                 {
    //                     "input": false,
    //                     "tableView": false,
    //                     "tag": "p",
    //                     "id": "S04Q00",
    //                     "type": "htmlelement",
    //                     "content": "<span class=\"sectionTitleClass\">Case Plan</span>"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "refreshOnChange": false,
    //                             "tag": "p",
    //                             "type": "htmlelement",
    //                             "content": "<span class=\"subSectionTitleClass\">Intervention Plan</span>"
    //                         },
    //                         {
    //                             "input": false,
    //                             "components": [
    //                                 {
    //                                     "input": false,
    //                                     "columns": [
    //                                         {
    //                                             "pull": 0,
    //                                             "components": [
    //                                                 {
    //                                                     "input": true,
    //                                                     "defaultValue": "",
    //                                                     "tableView": true,
    //                                                     "customClass": "hide-input",
    //                                                     "disabled": true,
    //                                                     "label": "Criminogenic Needs",
    //                                                     "type": "textfield"
    //                                                 }
    //                                             ],
    //                                             "offset": 0,
    //                                             "size": "md",
    //                                             "currentWidth": 2,
    //                                             "width": 2,
    //                                             "push": 0
    //                                         },
    //                                         {
    //                                             "pull": 0,
    //                                             "components": [
    //                                                 {
    //                                                     "input": true,
    //                                                     "defaultValue": "",
    //                                                     "tableView": true,
    //                                                     "customClass": "hide-input",
    //                                                     "disabled": true,
    //                                                     "label": "Specific Factor",
    //                                                     "type": "textfield"
    //                                                 }
    //                                             ],
    //                                             "size": "md",
    //                                             "offset": 0,
    //                                             "currentWidth": 2,
    //                                             "width": 2,
    //                                             "push": 0
    //                                         },
    //                                         {
    //                                             "pull": 0,
    //                                             "components": [
    //                                                 {
    //                                                     "input": true,
    //                                                     "defaultValue": "",
    //                                                     "tableView": true,
    //                                                     "customClass": "hide-input",
    //                                                     "disabled": true,
    //                                                     "label": "Intervention Type",
    //                                                     "type": "textfield"
    //                                                 }
    //                                             ],
    //                                             "size": "md",
    //                                             "offset": 0,
    //                                             "currentWidth": 2,
    //                                             "width": 2,
    //                                             "push": 0
    //                                         },
    //                                         {
    //                                             "pull": 0,
    //                                             "components": [
    //                                                 {
    //                                                     "input": true,
    //                                                     "defaultValue": "",
    //                                                     "tableView": true,
    //                                                     "customClass": "hide-input",
    //                                                     "disabled": true,
    //                                                     "label": "Intervention Description",
    //                                                     "type": "textfield"
    //                                                 }
    //                                             ],
    //                                             "size": "md",
    //                                             "offset": 0,
    //                                             "currentWidth": 3,
    //                                             "width": 3,
    //                                             "push": 0
    //                                         }
    //                                     ],
    //                                     "customClass": "editgrid-header",
    //                                     "tableView": false,
    //                                     "label": "Columns",
    //                                     "type": "columns",
    //                                     "key": "intervention_header"
    //                                 },
    //                                 {
    //                                     "input": "true",
    //                                     "components": [
    //                                         {
    //                                             "input": false,
    //                                             "columns": [
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "tableView": true,
    //                                                             "disabled": true,
    //                                                             "label": "Criminogenic Needs",
    //                                                             "type": "textarea",
    //                                                             "key": "questionLabel"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 3,
    //                                                     "width": 3,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "tableView": true,
    //                                                             "label": "Specific Factor",
    //                                                             "type": "textarea",
    //                                                             "key": "comments"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 3,
    //                                                     "width": 3,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "tableView": true,
    //                                                             "disabled": true,
    //                                                             "label": "Intervention Type",
    //                                                             "type": "textarea",
    //                                                             "key": "interventionType"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 3,
    //                                                     "width": 3,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "tableView": true,
    //                                                             "label": "Intervention Description",
    //                                                             "type": "textarea",
    //                                                             "key": "interventionDescription"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 3,
    //                                                     "width": 3,
    //                                                     "push": 0
    //                                                 },
    //                                                 {
    //                                                     "pull": 0,
    //                                                     "components": [
    //                                                         {
    //                                                             "input": true,
    //                                                             "tableView": true,
    //                                                             "label": "Quick link",
    //                                                             "type": "textarea",
    //                                                             "key": "navKey"
    //                                                         }
    //                                                     ],
    //                                                     "offset": 0,
    //                                                     "size": "md",
    //                                                     "currentWidth": 3,
    //                                                     "width": 3,
    //                                                     "push": 0
    //                                                 }
    //                                             ],
    //                                             "tableView": false,
    //                                             "label": "Columns",
    //                                             "type": "columns",
    //                                             "key": "columns"
    //                                         }
    //                                     ],
    //                                     "templates": {
    //                                         "footer": "",
    //                                         "header": "",
    //                                         "row": "<div class=\"row justify-content-around\">\n      {% util.eachComponent(components, function(component) { %}\n        {% if (component.key != 'navKey') { %}\n          <div class=\"col-sm-2\">\n            {{row[component.key]}}\n          </div>\n        {% } %}\n        {% if (component.key == 'navKey') { %}\n        <div class=\"col-sm-2 text-left\">\n        <div class=\"btn-group\">\n          <a class=\"btn btn-default btn-sm\" href=\"#{{row[component.key]}}\" id=\"editLink_{{row[component.key]}}\">\n            <i class=\"fa fa-edit\"></i>\n          </a>\n        </div>\n        </div>\n        {% } %}\n      {% }) %}\n      \n    </div>"
    //                                     },
    //                                     "label": "",
    //                                     "disableAddingRemovingRows": true,
    //                                     "type": "editgrid",
    //                                     "key": "interventions"
    //                                 }
    //                             ],
    //                             "tableView": false,
    //                             "label": "",
    //                             "type": "well",
    //                             "key": "intervention_panel"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "refreshOnChange": false,
    //                             "tag": "p",
    //                             "type": "htmlelement",
    //                             "content": "<span class=\"subSectionTitleClass\">Supervision Plan</span>"
    //                         },
    //                         {
    //                             "input": true,
    //                             "label": "Please input a Supervision Plan",
    //                             "type": "textarea",
    //                             "key": "PLAN_SUMMARY_TXT"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "refreshOnChange": false,
    //                             "tag": "p",
    //                             "type": "htmlelement",
    //                             "content": "<span class=\"subSectionTitleClass\">Reassessment Comments</span>"
    //                         },
    //                         {
    //                             "input": true,
    //                             "label": "Please input a Reassessment Summary",
    //                             "type": "textarea",
    //                             "key": "COMMENT_TXT"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "type": "well"
    //                 },
    //                 {
    //                     "input": false,
    //                     "components": [
    //                         {
    //                             "refreshOnChange": false,
    //                             "tag": "p",
    //                             "type": "htmlelement",
    //                             "content": "<span class=\"subSectionTitleClass\">Responsivity Factors</span><div>This section is intended to provide the case manager with space to identify circumstances and factors which could influence how supervision of the client is approached.</div> <br/><div>Please select which Specific Factors apply: </br></div>"
    //                         },
    //                         {
    //                             "input": true,
    //                             "components": [
    //                                 {
    //                                     "input": true,
    //                                     "tooltip": "Does the client identify as indigenous (including Metis)? If so, note any available information regarding the client's nation and band affiliation. Is the client connected to cultural supports in the community?",
    //                                     "tableView": false,
    //                                     "label": "Indigenous considerations",
    //                                     "type": "checkbox",
    //                                     "key": "INCO"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "conditional": {
    //                                         "show": true,
    //                                         "eq": "true",
    //                                         "when": "INCO"
    //                                     },
    //                                     "tableView": true,
    //                                     "label": "Please specify in the field below",
    //                                     "type": "textarea",
    //                                     "autoExpand": false,
    //                                     "key": "INCO_desc"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "tooltip": "This can include any suspected, self-identified, or confirmed  mental illness, brain injury, learning challenges, or other cognitive issues that could impact the client's functioning or require an adaptive approach to delivering meaningful interventions.",
    //                                     "tableView": false,
    //                                     "label": "Mental health and/or cognitive functioning concerns",
    //                                     "type": "checkbox",
    //                                     "key": "MHCF"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "conditional": {
    //                                         "show": true,
    //                                         "eq": "true",
    //                                         "when": "MHCF"
    //                                     },
    //                                     "tableView": true,
    //                                     "label": "Please specify in the field below",
    //                                     "type": "textarea",
    //                                     "autoExpand": false,
    //                                     "key": "MHCF_desc"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "tooltip": "Note the level of compliance and engagement during previous periods of supervision. How is the client's history of reporting? Have they completed any programs? What is their current level of motivation to engage in interventions?",
    //                                     "tableView": false,
    //                                     "label": "Previous responses to supervision and motivation to engage in interventions",
    //                                     "type": "checkbox",
    //                                     "key": "PRRE"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "conditional": {
    //                                         "show": true,
    //                                         "eq": "true",
    //                                         "when": "PRRE"
    //                                     },
    //                                     "tableView": true,
    //                                     "label": "Please specify in the field below",
    //                                     "type": "textarea",
    //                                     "autoExpand": false,
    //                                     "key": "PRRE_desc"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "tooltip": "Note the presence of any language or literacy barriers as well as any cognitive factors that could impact communication. If applicable, are you aware of any strategies to assist in communicating with the client",
    //                                     "tableView": false,
    //                                     "label": "Barriers to communication",
    //                                     "type": "checkbox",
    //                                     "key": "BACO"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "conditional": {
    //                                         "show": true,
    //                                         "eq": "true",
    //                                         "when": "BACO"
    //                                     },
    //                                     "tableView": true,
    //                                     "label": "Please specify in the field below",
    //                                     "type": "textarea",
    //                                     "autoExpand": false,
    //                                     "key": "BACO_desc"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "tooltip": "Are you aware of any past trauma that could be impacting the client's responsivity? Are there strategies or adaptations that can be employed to minimise the client's trauma response?",
    //                                     "tableView": false,
    //                                     "label": "Trauma informed approaches to supervision",
    //                                     "type": "checkbox",
    //                                     "key": "TIAS"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "conditional": {
    //                                         "show": true,
    //                                         "eq": "true",
    //                                         "when": "TIAS"
    //                                     },
    //                                     "tableView": true,
    //                                     "label": "Please specify in the field below",
    //                                     "type": "textarea",
    //                                     "autoExpand": false,
    //                                     "key": "TIAS_desc"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "tooltip": "Considerations include the client's gender identity, preferred pronouns, name, etc. Note any adaptations to consider in meeting the client's program needs. Is client connected to any community supports/allies?",
    //                                     "tableView": false,
    //                                     "label": "Gender based considerations for LGBTQ2+ clients",
    //                                     "type": "checkbox",
    //                                     "key": "GBCL"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "conditional": {
    //                                         "show": true,
    //                                         "eq": "true",
    //                                         "when": "GBCL"
    //                                     },
    //                                     "tableView": true,
    //                                     "label": "Please specify in the field below",
    //                                     "type": "textarea",
    //                                     "autoExpand": false,
    //                                     "key": "GBCL_desc"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "tooltip": "Outline strengths that can be engaged to maximise the potential for success. Examples can include resiliency, programming/treatment completed, learning style, strengths/hopes, etc. What are the client's goals? What motivates them?",
    //                                     "tableView": false,
    //                                     "label": "Strength based considerations",
    //                                     "type": "checkbox",
    //                                     "key": "STCO"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "conditional": {
    //                                         "show": true,
    //                                         "eq": "true",
    //                                         "when": "STCO"
    //                                     },
    //                                     "tableView": true,
    //                                     "label": "Please specify in the field below",
    //                                     "type": "textarea",
    //                                     "autoExpand": false,
    //                                     "key": "STCO_desc"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "tooltip": "",
    //                                     "tableView": false,
    //                                     "label": "Other",
    //                                     "type": "checkbox",
    //                                     "key": "OTHR"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "conditional": {
    //                                         "show": true,
    //                                         "eq": "true",
    //                                         "when": "OTHR"
    //                                     },
    //                                     "tableView": true,
    //                                     "label": "Specify",
    //                                     "type": "textfield",
    //                                     "key": "OTHR_specify"
    //                                 },
    //                                 {
    //                                     "input": true,
    //                                     "conditional": {
    //                                         "show": true,
    //                                         "eq": "true",
    //                                         "when": "OTHR"
    //                                     },
    //                                     "tableView": true,
    //                                     "label": "Please specify in the field below",
    //                                     "type": "textarea",
    //                                     "autoExpand": false,
    //                                     "key": "OTHR_desc"
    //                                 }
    //                             ],
    //                             "type": "container",
    //                             "key": "responsivity"
    //                         }
    //                     ],
    //                     "tableView": false,
    //                     "type": "well"
    //                 }
    //             ],
    //             "tableView": false,
    //             "custom_subNavOn": false,
    //             "label": "Case Plan",
    //             "id": "S04",
    //             "type": "well",
    //             "key": "S04",
    //             "properties": {
    //                 "Case Plan": true
    //             }
    //         },
    //         {
    //             "input": false,
    //             "tableView": false,
    //             "custom_subNavOn": false,
    //             "label": "Summary",
    //             "id": "S05",
    //             "type": "well",
    //             "key": "S05",
    //             "properties": {
    //                 "Summary": true
    //             }
    //         }
    //     ],
    //     "display": "form"
    // }
    // return [null, tmpData];
}

// function to clone form
export async function cloneForm(formId: number) {
    try {
        const { data } = await axiosClient.post(`/forms/client/clone/${formId}`);
        return [null, data];
    }catch (error) {
        return [error];
    }
}

// function to update form data
export async function updateForm( csNumber: number,clientFormId: number, formData: object) {
    try{
        console.log("Update form payload", formData);
        const { data } = await axiosClient.put('/forms/client/answers/' + csNumber + '/' + clientFormId, formData);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// delete all interventions except the ones listed (backwards I know!)
export async function deleteQuestionInterventionsExcept(csNumber: number,clientFormId: number, questionKey: string, remainingInterventionTypes: string[]) {
try{
    let formData = {
        "questionKey": questionKey,
        "action":"deleteExcept",
        "typeList": remainingInterventionTypes
    }
    console.log("Updating interventions %o for %s", remainingInterventionTypes, questionKey);
    return await axiosClient.put('/forms/client/answers/interventions/' + csNumber + '/' + clientFormId , formData);
} catch (error) {
    return [error];
}
}

// get form data for an entire section
export async function loadFormDataForSectionSeq(csNumber: number, clientFormId: number, sectionSeq: number) {
    try {
        const { data } = await axiosClient.get('/forms/client/answers/' + csNumber + '/' + clientFormId + '/' + sectionSeq);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// get form data for a single question
export async function loadFormDataForSectionAndQuestion(csNumber: number, clientFormId: number, sectionSeq: number, questionSeq: number) {
    try {
        const { data } = await axiosClient.get('/forms/client/answers/' + csNumber + '/' + clientFormId + '/' + sectionSeq + '/' + questionSeq);
        return [null, data];
    } catch (error) {
        return [error];
    }
}


// get form data (all data returned)
export async function loadFormData(csNumber: number, clientFormId: number) {
    console.log("load formdata");
    try {
        const { data } = await axiosClient.get('/forms/client/answers/' + csNumber + '/' + clientFormId);
        console.log("load formdata: ", data);
        return [null, data];
    } catch (error) {
        return [error];
    }
    // let tmpJson = {
    //     "data": {
    //         "S01Q01": "H",
    //         "S02Q02": "B",
    //         "S03Q04": "B",
    //         "S01Q02": "L",
    //         "S02Q03": "D",
    //         "S03Q03": "B",
    //         "S04Q04": "N",
    //         "S03Q02": "C",
    //         "S04Q03": "Y",
    //         "S02Q01": "C",
    //         "S01Q01_COMMENT": "a new comment! updated again",
    //         "S01Q03_intervention_datagrid": [
    //             {
    //                 "S01Q03_intervention_type": "ACVS",
    //                 "S01Q03_intervention_desc": "asdasd"
    //             }
    //         ],
    //         "S01Q03_intervention_checkbox": true,
    //         "S01Q01_intervention_checkbox": true,
    //         "S01Q01_intervention_datagrid": [
    //             {
    //                 "S01Q01_intervention_desc": "asdas sdf sdf sdf sdf ",
    //                 "S01Q01_intervention_type": "ACVS"
    //             },
    //             {
    //                 "S01Q01_intervention_desc": "sadadasd",
    //                 "S01Q01_intervention_type": "LIAR"
    //             },
    //             {
    //                 "S01Q01_intervention_desc": "drugs",
    //                 "S01Q01_intervention_type": "SUMI"
    //             }
    //         ],
    //         "S03Q09": "C",
    //         "S02Q06": "B",
    //         "S03Q08": "A",
    //         "S02Q07": "D",
    //         "S01Q03": "bbb",
    //         "S02Q04": "C",
    //         "S03Q06": "A",
    //         "S06Q07": "aa"
    //     },
    //     "clientFormId": 389760
    // };
    // return [null, JSON.stringify(tmpJson)];
}

// function to delete form
export async function deleteForm(formId: number) {

}

// function to create form
export async function createForm(formData: object) {
    try{
        const { data } = await axiosClient.post('/forms/client', formData);
        return [null, data];
    } catch (error) {
        console.error("Error creating form %o", error);
        return [error];
    }
}

// function to search client based on general info
export async function clientSearchByGeneralInfo(age: String, birthYear: String, gender: String,
    givenName: String, identifier: String, identifierType: String, lastName: String,
    limitToLocation: String, range: String, soundex: String) {
    try{
        console.log("ClientSearch by generalInfo: " + "age: " + age + "; " +
                "birthYear: " + birthYear + "; " +
                "gender: " + gender + "; " +
                "givenName: " + givenName + "; " +
                "identifier: " + identifier + "; " +
                "identifierType: " + identifierType + "; " +
                "lastName: " + lastName + "; " +
                "limitToLocation: " + limitToLocation + "; " +
                "range: " + range + "; " +
                "soundex: " + soundex);
        // Build url
        let url = "/clients?";
        url += age == '' ? '' : "age=" + age;
        url += birthYear == '' ? '' : "&birthYear=" + birthYear;
        url += gender == '' ? '' : "&gender=" + gender;
        url += givenName == '' ? '' : "&givenName=" + givenName;
        url += identifier == '' ? '' : "&identifier=" + identifier;
        url += identifierType == '' ? '' : "&identifierType=" + identifierType;
        url += lastName == '' ? '' : "&lastName=" + lastName;
        url += limitToLocation == '' ? '' : "&limitToLocation=" + limitToLocation;
        url += range == '' ? '' : "&range=" + range;
        url += soundex == null ? '' : "&soundex=" + soundex;
        // const { data } = await axiosClient.get('/clients', {
        //     params: {
        //         age: age,
        //         birthYear: birthYear,
        //         gender: gender,
        //         givenName: givenName,
        //         identifier: identifier,
        //         identifierType: identifierType,
        //         lastName: lastName,
        //         limitToLocation: limitToLocation,
        //         range: range,
        //         soundex: soundex
        //     }
        // });
        console.log("url: ", url);
        const { data } = await axiosClient.get(url);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to search client based on address info
export async function clientSearchByAddressInfo(address: String, addressType: String, city: String,
    expired: boolean, limitToLocation: boolean, postalCode: String, province: String) {
    try{
        console.log("ClientSearch by addressInfo: " + "address: " + address + "; " +
                "addressType: " + addressType + "; " +
                "city: " + city + "; " +
                "expired: " + expired + "; " +
                "limitToLocation: " + limitToLocation + "; " +
                "postalCode: " + postalCode + "; " +
                "province: " + province);
        const { data } = await axiosClient.get('/clients/addressSearch', {
            params: {
                address: address,
                addressType: addressType,
                city: city,
                expired: expired,
                limitToLocation: limitToLocation,
                postalCode: postalCode,
                province: province
            }
        });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to retrieve client details.
export async function getClientDetail(clientNum: String) {
    try{
        console.log("Get client detail by clientNum: ", clientNum);
        const { data } = await axiosClient.get(`/clients/${clientNum}/details`);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to retrieve client addresses.
export async function getClientAddresses(clientNum: String) {
    try{
        console.log("Get client addresses by clientNum: ", clientNum);
        const { data } = await axiosClient.get(`/clients/${clientNum}/address`);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to get client photo
export async function photoSearch(clientNum: String) {
    try{
        //console.log("Photo search by clientID: ", clientID);
        const { data } = await axiosClient.get(`/clients/${clientNum}/photo`);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// Supervisor dashboard search
export async function dashboardSupervisorSearch(supervisorID: String) {
    try{
        console.log("Officer search by supervisorID: ", supervisorID);
        const { data } = await axiosClient.get('/dashboards/supervisor', {
            params: {
                userId: supervisorID
            }
        });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// PO dashboard search
export async function dashboardPOSearch(poID: String) {
    try{
        //console.log("Officer search by supervisorID: ", supervisorID);
        const { data } = await axiosClient.get('/dashboards/po', {
            params: {
                userId: poID
            }
        });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to search client profile
export async function clientProfileSearch(clientNum: String) {
    try{
        console.log("ClientProfileSearch clientNum", clientNum);
        const { data } = await axiosClient.get(`/clients/${clientNum}`);
        console.log("ClientProfileSearch: ", data);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to search for RNA list
export async function formSearch(clientNum: String, formType: String, currentPeriod: boolean) {
    try{
        console.log("formSearch for RNA List, clientNum: {}, formType: {}, supervisionPeriod: {}", clientNum, formType, currentPeriod);
        const { data } = await axiosClient.get('/forms/client/search/' + clientNum, {
                params: {
                    formTypeCd: formType,
                    currentPeriod: currentPeriod
                }
            });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to search for form types
export async function async_lookupFormTypes() {
    try {
        const { data } = await axiosClient.get('/lookup/formtypes');
        return [null, data];
    }catch (error) {
        return [error];
    }
}

// function to search for form types
export function lookupFormTypes() {
    try {
        const data = axiosClient.get('/lookup/formtypes');
        return [null, data];
    }catch (error) {
        return [error];
    }
}

// function to get form id for latest form
export async function getFormSummaries( formType: String, latestOnly: boolean) {
    try{
        console.log("getFormSummaries, formType: {}, latestOnly: {}", formType, latestOnly);
        const { data } = await axiosClient.get('/forms/summaries', {
                params: {
                    module: formType,
                    latestOnly: latestOnly
                }
            });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// Get interventions for case plan
export async function getCasePlanIntervention(csNumber: String, clientFormId: number, includeLinkedForm: boolean) {
    try{
        console.log("getCasePlanIntervention, csNumber: {}, clientFormId: {}, includeLinkedForm: {}", csNumber, clientFormId, includeLinkedForm);
        const { data } = await axiosClient.get(`/forms/client/intervention/json/${csNumber}/${clientFormId}`, {
            params: {
                includeLinkedForm: includeLinkedForm
            }
        });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to update sources contacted
export async function updateSourcesContacted(csNumber: String, clientFormId: number, sourcesContacted: String) {
    try{
        console.log("Update source contacted, clientFormId: {}, sourcesContacted: {}", clientFormId, sourcesContacted);
        const { data } = await axiosClient.put(`/forms/client/sourcesContacted/${csNumber}/${clientFormId}`, {
            params: {
                sourcesContacted: sourcesContacted
            }
        });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// Get client form meta data
export async function getClientFormMetaData(csNumber: String, clientFormId: number) {
    try{
        console.log("getClientFormMetaData, csNumber: {}, clientFormId: {}", csNumber, clientFormId);
        const { data } = await axiosClient.get(`/forms/client/meta/${csNumber}/${clientFormId}`);
        console.log("getClientFormMetaData: ", data);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

//-------------------------------------
// Trend analysis
//-------------------------------------
export async function getFormFactors( reportType: string) {
    try {
        const { data} = await axiosClient.get('/trend/' + reportType + '/factors');
        return [null,data];
    }catch (error) {
        return [error];
    }
}

export async function getChartData(payload: Object) {
    try{
        const { data } = await axiosClient.post('/trend/client/data', payload);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

/**
 * Get available chart types
 */
 export async function getTrendChartTypes() {
    try{
        const { data } = await axiosClient.get('/trend/types');
        return [null, data];
    } catch (error) {
        return [error];
    }
}

//-------------------------------------
// Comments
//-------------------------------------
export async function searchClientFormComments( clientNumber:number, payload: object) {
    try {
        const { data} = await axiosClient.post('/forms/client/comments/' + clientNumber, payload );
        return [null,data];
    }catch (error) {
        return [error];
    }
}

export async function searchClientInterventions( payload: Object) {
    try {
        const { data} = await axiosClient.post('/forms/client/interventions', payload);
        return [null,data];
    }catch (error) {
        return [error];
    }
}

export async function searchClientResponsivities( payload: Object) {
    try {
        const { data} = await axiosClient.post('/forms/client/responsivities', payload);
        return [null,data];
    }catch (error) {
        return [error];
    }
}



export async function searchClientComments( payload: Object) {
    try {
        const { data} = await axiosClient.post('/forms/client/comments', payload);
        return [null,data];
    }catch (error) {
        return [error];
    }
}

export default axiosClient;
