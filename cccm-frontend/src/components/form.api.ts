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
export async function getFormSummary(clientNum: string, formId: number) {
    try {
        const { data } = await axiosClient.get(`/forms/client/summary/answers/${clientNum}/${formId}`);
        return [null, data];
    }catch (error) {
        return [error];
    }
}


// function to fetch the form details
export async function getFormDetails(clientNum: String, formId: number) {
    // try {
    //     const { data } = await axiosClient.get(`/forms/client/json/${clientNum}/${formId}`, {
    //         params: {
    //             includeOptionValues: true
    //         }
    //     });
    //     return [null, data];
    // }catch (error) {
    //     return [error];
    // }
    let tmpJson = {
        "components": [
            {
                "tag": "h1",
                "type": "htmlelement",
                "key": "S01_header",
                "content": "Rating Information"
            },
            {
                "input": false,
                "components": [
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11114,
                                "shortcut": "L",
                                "label": "Low",
                                "value": "L"
                            },
                            {
                                "answerId": 11115,
                                "shortcut": "M",
                                "label": "Medium",
                                "value": "M"
                            },
                            {
                                "answerId": 11116,
                                "shortcut": "H",
                                "label": "High",
                                "value": "H"
                            }
                        ],
                        "tooltip": "A custom tooltip",
                        "customClass": "test-green",
                        "id": "S01Q01",
                        "label": " 1. Overall Rating",
                        "type": "radio",
                        "key": "S01Q01"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  1. Overall Rating",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S01Q01_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S01Q01_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S01Q01_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S01Q01_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S01Q01_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S01Q01_intervention_desc": "",
                                        "S01Q01_intervention_specify": "",
                                        "S01Q01_intervention_type": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S01Q01_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S01Q01_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11118,
                                "label": "Wide",
                                "value": "ww"
                            },
                            {
                                "answerId": 11119,
                                "shortcut": "H",
                                "label": "High",
                                "value": "H"
                            },
                            {
                                "answerId": 11120,
                                "shortcut": "L",
                                "label": "Low",
                                "value": "L"
                            },
                            {
                                "answerId": 11121,
                                "shortcut": "M",
                                "label": "Medium/Moderate",
                                "value": "M"
                            }
                        ],
                        "tooltip": "S01Q02",
                        "customClass": "col-form-label",
                        "id": "S01Q02",
                        "label": " 2. Needs Assessment Ratings",
                        "type": "radio",
                        "key": "S01Q02"
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S01Q02_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S01Q02_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S01Q02_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S01Q02_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S01Q02_intervention_desc": "",
                                        "S01Q02_intervention_type": "",
                                        "addIntervention": false,
                                        "S01Q02_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S01Q02_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S01Q02_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11109,
                                "shortcut": "X",
                                "label": "Maybe",
                                "value": "X"
                            },
                            {
                                "answerId": 11110,
                                "label": "bbbb",
                                "value": "bbb"
                            },
                            {
                                "answerId": 11111,
                                "shortcut": "L",
                                "label": "Low",
                                "value": "L"
                            },
                            {
                                "answerId": 11112,
                                "shortcut": "M",
                                "label": "Medium",
                                "value": "M"
                            },
                            {
                                "answerId": 11113,
                                "shortcut": "H",
                                "label": "High",
                                "value": "H"
                            }
                        ],
                        "tooltip": "S01Q03",
                        "customClass": "col-form-label",
                        "id": "S01Q03",
                        "label": " 3. Risk  Assessment - Criminal History Rating",
                        "type": "radio",
                        "key": "S01Q03"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  3. Risk  Assessment - Criminal History Rating",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S01Q03_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S01Q03_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S01Q03_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S01Q03_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S01Q03_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S01Q03_intervention_specify": "",
                                        "addIntervention": false,
                                        "S01Q03_intervention_type": "",
                                        "S01Q03_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S01Q03_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S01Q03_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "widget": "choicesjs",
                        "multiple": true,
                        "tooltip": "S01Q04",
                        "customClass": "col-form-label",
                        "placeholder": "Select value",
                        "id": "S01Q04",
                        "label": " 4. tttttttttttttttttt",
                        "type": "select",
                        "key": "S01Q04"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  4. tttttttttttttttttt",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S01Q04_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S01Q04_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S01Q04_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S01Q04_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S01Q04_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S01Q04_intervention_type": "",
                                        "addIntervention": false,
                                        "S01Q04_intervention_desc": "",
                                        "S01Q04_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S01Q04_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S01Q04_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "tooltip": "S01Q05",
                        "customClass": "col-form-label",
                        "id": "S01Q05",
                        "label": " 5. With rating information",
                        "type": "textfield",
                        "key": "S01Q05"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  5. With rating information",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S01Q05_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S01Q05_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S01Q05_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S01Q05_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S01Q05_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S01Q05_intervention_specify": "",
                                        "addIntervention": false,
                                        "S01Q05_intervention_desc": "",
                                        "S01Q05_intervention_type": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S01Q05_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S01Q05_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "tooltip": "S01Q06",
                        "customClass": "col-form-label",
                        "id": "S01Q06",
                        "label": " 6. with rating info",
                        "type": "textfield",
                        "key": "S01Q06"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  6. with rating info",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S01Q06_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S01Q06_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S01Q06_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S01Q06_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S01Q06_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S01Q06_intervention_type": "",
                                        "S01Q06_intervention_specify": "",
                                        "S01Q06_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S01Q06_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S01Q06_intervention_datagrid"
                    }
                ],
                "theme": "primary",
                "title": "Rating Information",
                "type": "well",
                "collapsible": true,
                "key": "S01"
            },
            {
                "tag": "h1",
                "type": "htmlelement",
                "key": "S02_header",
                "content": "Needs Assessment"
            },
            {
                "input": false,
                "components": [
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10909,
                                "label": "Pattern of stable and supportive relationships",
                                "value": "AA"
                            },
                            {
                                "answerId": 10910,
                                "label": "No recent difficulties",
                                "value": "bb"
                            },
                            {
                                "answerId": 10911,
                                "shortcut": "C",
                                "label": "Occasional instability in relationships",
                                "value": "C"
                            },
                            {
                                "answerId": 10912,
                                "shortcut": "D",
                                "label": "Very unstable pattern of relationships",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q01",
                        "customClass": "col-form-label",
                        "id": "S02Q01",
                        "label": " 1. Family Relationships",
                        "type": "radio",
                        "key": "S02Q01"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  1. Family Relationships",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q01_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q01_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q01_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q01_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q01_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S02Q01_intervention_desc": "",
                                        "addIntervention": false,
                                        "S02Q01_intervention_specify": "",
                                        "S02Q01_intervention_type": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q01_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q01_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10916,
                                "shortcut": "A",
                                "label": "Pattern of satisfactory living arrrangements",
                                "value": "A"
                            },
                            {
                                "answerId": 10917,
                                "shortcut": "B",
                                "label": "No recent difficulties",
                                "value": "B"
                            },
                            {
                                "answerId": 10918,
                                "shortcut": "C",
                                "label": "Occasional changes in residence or temporarily situated",
                                "value": "C"
                            },
                            {
                                "answerId": 10919,
                                "shortcut": "D",
                                "label": "Frequent changes in residence, or no permanent address",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q02",
                        "customClass": "col-form-label",
                        "id": "S02Q02",
                        "label": " 2. Living Arrangement",
                        "type": "radio",
                        "key": "S02Q02"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  2. Living Arrangement",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q02_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q02_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q02_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q02_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q02_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S02Q02_intervention_desc": "",
                                        "S02Q02_intervention_type": "",
                                        "S02Q02_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q02_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q02_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10920,
                                "shortcut": "A",
                                "label": "Pattern of non-criminal and/or positive associations",
                                "value": "A"
                            },
                            {
                                "answerId": 10921,
                                "shortcut": "B",
                                "label": "Mostly non-criminal and/or positive associations",
                                "value": "B"
                            },
                            {
                                "answerId": 10922,
                                "shortcut": "C",
                                "label": "Some criminal and/or negative associations",
                                "value": "C"
                            },
                            {
                                "answerId": 10923,
                                "shortcut": "D",
                                "label": "Mostly criminal and/or negative associations",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q03",
                        "customClass": "col-form-label",
                        "id": "S02Q03",
                        "label": " 3. Companions/Significant Others",
                        "type": "radio",
                        "key": "S02Q03"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  3. Companions/Significant Others",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q03_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q03_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q03_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q03_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q03_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S02Q03_intervention_specify": "",
                                        "S02Q03_intervention_desc": "",
                                        "S02Q03_intervention_type": "",
                                        "addIntervention": false
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q03_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q03_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10924,
                                "shortcut": "B",
                                "label": "No recent difficulties",
                                "value": "B"
                            },
                            {
                                "answerId": 10925,
                                "shortcut": "C",
                                "label": "Level of skills causing minor interference",
                                "value": "C"
                            },
                            {
                                "answerId": 10926,
                                "shortcut": "D",
                                "label": "Level of skills causing serious interference",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q04",
                        "customClass": "col-form-label",
                        "id": "S02Q04",
                        "label": " 4. Academic/Vocational Skill",
                        "type": "radio",
                        "key": "S02Q04"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  4. Academic/Vocational Skill",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q04_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q04_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q04_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q04_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q04_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S02Q04_intervention_specify": "",
                                        "S02Q04_intervention_type": "",
                                        "S02Q04_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q04_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q04_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "tooltip": "S02Q05",
                        "customClass": "col-form-label",
                        "id": "S02Q05",
                        "label": " 5. With Raging INformaition",
                        "type": "textfield",
                        "key": "S02Q05"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  5. With Raging INformaition",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q05_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q05_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q05_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q05_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q05_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S02Q05_intervention_type": "",
                                        "S02Q05_intervention_specify": "",
                                        "addIntervention": false,
                                        "S02Q05_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q05_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q05_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10934,
                                "shortcut": "e",
                                "label": "Every TIme",
                                "value": "e"
                            },
                            {
                                "answerId": 10935,
                                "shortcut": "A",
                                "label": "Pattern of effective managment",
                                "value": "A"
                            },
                            {
                                "answerId": 10936,
                                "shortcut": "B",
                                "label": "No recent difficulties",
                                "value": "B"
                            },
                            {
                                "answerId": 10937,
                                "shortcut": "C",
                                "label": "Situational or minor difficulties",
                                "value": "C"
                            },
                            {
                                "answerId": 10938,
                                "shortcut": "D",
                                "label": "Severe difficulties",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q06",
                        "customClass": "col-form-label",
                        "id": "S02Q06",
                        "label": " 6. Financial Management",
                        "type": "radio",
                        "key": "S02Q06"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  6. Financial Management",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q06_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q06_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q06_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q06_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q06_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S02Q06_intervention_desc": "",
                                        "S02Q06_intervention_type": "",
                                        "S02Q06_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q06_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q06_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10939,
                                "shortcut": "B",
                                "label": "No recent difficulties",
                                "value": "B"
                            },
                            {
                                "answerId": 10940,
                                "shortcut": "C",
                                "label": "Behavioural/emotional problems that indicate some need for assistance",
                                "value": "C"
                            },
                            {
                                "answerId": 10941,
                                "shortcut": "D",
                                "label": "Severe behavioural/emotional problems that indicate significant need for assistance",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q07",
                        "customClass": "col-form-label",
                        "id": "S02Q07",
                        "label": " 7. Behavioural/Emotional Stability",
                        "type": "radio",
                        "key": "S02Q07"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  7. Behavioural/Emotional Stability",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q07_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q07_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q07_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q07_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q07_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S02Q07_intervention_specify": "",
                                        "S02Q07_intervention_type": "",
                                        "S02Q07_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q07_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q07_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10942,
                                "shortcut": "B",
                                "label": "No recent usage or difficulties",
                                "value": "B"
                            },
                            {
                                "answerId": 10943,
                                "shortcut": "C",
                                "label": "Some usage causing moderate adjustment problems",
                                "value": "C"
                            },
                            {
                                "answerId": 10944,
                                "shortcut": "D",
                                "label": "Frequent or uncontrolled usage causing serious adjustment problems",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q08",
                        "customClass": "col-form-label",
                        "id": "S02Q08",
                        "label": " 8. Substance Abuse",
                        "type": "radio",
                        "key": "S02Q08"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  8. Substance Abuse",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q08_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q08_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q08_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q08_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q08_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S02Q08_intervention_specify": "",
                                        "S02Q08_intervention_type": "",
                                        "S02Q08_intervention_desc": "",
                                        "addIntervention": false
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q08_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q08_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10945,
                                "shortcut": "A",
                                "label": "Actively involved and responding consistently well to assistance",
                                "value": "A"
                            },
                            {
                                "answerId": 10946,
                                "shortcut": "B",
                                "label": "Motivated to change, receptive to assistance",
                                "value": "B"
                            },
                            {
                                "answerId": 10947,
                                "shortcut": "C",
                                "label": "Recognizes problem areas but not receptive to assistance",
                                "value": "C"
                            },
                            {
                                "answerId": 10948,
                                "shortcut": "D",
                                "label": "Unable to recognize problem areas and not receptive to assistance",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q09",
                        "customClass": "col-form-label",
                        "id": "S02Q09",
                        "label": " 9. Attitude",
                        "type": "radio",
                        "key": "S02Q09"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  9. Attitude",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q09_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q09_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q09_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q09_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q09_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S02Q09_intervention_type": "",
                                        "S02Q09_intervention_specify": "",
                                        "S02Q09_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q09_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q09_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10913,
                                "shortcut": "B",
                                "label": "No recent difficulties",
                                "value": "B"
                            },
                            {
                                "answerId": 10914,
                                "shortcut": "C",
                                "label": "Parent(s) having some difficulty with supervision and behaviour control",
                                "value": "C"
                            },
                            {
                                "answerId": 10915,
                                "shortcut": "D",
                                "label": "Parent(s) having major difficulty with supervision and behaviour control",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q10",
                        "customClass": "col-form-label",
                        "id": "S02Q10",
                        "label": " 10. Effectiveness of Youth's Parent(s)",
                        "type": "radio",
                        "key": "S02Q10"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  10. Effectiveness of Youth's Parent(s)",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q10_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q10_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q10_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q10_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q10_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S02Q10_intervention_specify": "",
                                        "S02Q10_intervention_type": "",
                                        "S02Q10_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q10_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q10_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10927,
                                "shortcut": "B",
                                "label": "No recent difficulties",
                                "value": "B"
                            },
                            {
                                "answerId": 10928,
                                "shortcut": "C",
                                "label": "Level/nature of involvement causing minor problems",
                                "value": "C"
                            },
                            {
                                "answerId": 10929,
                                "shortcut": "D",
                                "label": "Level/nature of involvement causing serious problems",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q11",
                        "customClass": "col-form-label",
                        "id": "S02Q11",
                        "label": " 11. Youth's Educational Involvement",
                        "type": "radio",
                        "key": "S02Q11"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  11. Youth's Educational Involvement",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q11_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q11_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q11_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q11_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q11_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S02Q11_intervention_desc": "",
                                        "S02Q11_intervention_type": "",
                                        "S02Q11_intervention_specify": "",
                                        "addIntervention": false
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q11_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q11_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "tooltip": "S02Q12",
                        "customClass": "col-form-label",
                        "id": "S02Q12",
                        "label": " 12. Needs Remarks",
                        "type": "textfield",
                        "key": "S02Q12"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  12. Needs Remarks",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q12_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q12_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q12_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q12_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q12_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S02Q12_intervention_desc": "",
                                        "S02Q12_intervention_type": "",
                                        "S02Q12_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q12_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q12_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10930,
                                "shortcut": "A",
                                "label": "Stable pattern of employment",
                                "value": "A"
                            },
                            {
                                "answerId": 10931,
                                "label": "No recent difficulties",
                                "value": "bb"
                            },
                            {
                                "answerId": 10932,
                                "shortcut": "C",
                                "label": "Employment situation causing minor adjustment problems",
                                "value": "C"
                            },
                            {
                                "answerId": 10933,
                                "shortcut": "D",
                                "label": "Employment situation causing major adjustment problems",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S02Q13",
                        "customClass": "col-form-label",
                        "id": "S02Q13",
                        "label": " 13. Employment Pattern",
                        "type": "radio",
                        "key": "S02Q13"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  13. Employment Pattern",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S02Q13_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S02Q13_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S02Q13_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S02Q13_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S02Q13_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S02Q13_intervention_specify": "",
                                        "S02Q13_intervention_type": "",
                                        "S02Q13_intervention_desc": "",
                                        "addIntervention": false
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S02Q13_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S02Q13_intervention_datagrid"
                    }
                ],
                "theme": "primary",
                "title": "Needs Assessment",
                "type": "well",
                "collapsible": true,
                "key": "S02"
            },
            {
                "tag": "h1",
                "type": "htmlelement",
                "key": "S03_header",
                "content": "Risk  Assessment - Criminal History"
            },
            {
                "input": false,
                "components": [
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10949,
                                "shortcut": "A",
                                "label": "None",
                                "value": "A"
                            },
                            {
                                "answerId": 10950,
                                "shortcut": "B",
                                "label": "One",
                                "value": "B"
                            },
                            {
                                "answerId": 10951,
                                "shortcut": "C",
                                "label": "Two",
                                "value": "C"
                            },
                            {
                                "answerId": 10952,
                                "shortcut": "D",
                                "label": "Three or more",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S03Q01",
                        "customClass": "col-form-label",
                        "id": "S03Q01",
                        "label": " 1. # of Current Convictions",
                        "type": "radio",
                        "key": "S03Q01"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  1. # of Current Convictions",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S03Q01_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S03Q01_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S03Q01_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S03Q01_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S03Q01_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S03Q01_intervention_specify": "",
                                        "addIntervention": false,
                                        "S03Q01_intervention_desc": "",
                                        "S03Q01_intervention_type": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S03Q01_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S03Q01_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10953,
                                "shortcut": "A",
                                "label": "None",
                                "value": "A"
                            },
                            {
                                "answerId": 10954,
                                "shortcut": "B",
                                "label": "One - Three",
                                "value": "B"
                            },
                            {
                                "answerId": 10955,
                                "shortcut": "C",
                                "label": "Four - Six",
                                "value": "C"
                            },
                            {
                                "answerId": 10956,
                                "shortcut": "D",
                                "label": "Seven or More",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S03Q02",
                        "customClass": "col-form-label",
                        "id": "S03Q02",
                        "label": " 2. # of Prior Convictions",
                        "type": "radio",
                        "key": "S03Q02"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  2. # of Prior Convictions",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S03Q02_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S03Q02_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S03Q02_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S03Q02_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S03Q02_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S03Q02_intervention_desc": "",
                                        "addIntervention": false,
                                        "S03Q02_intervention_specify": "",
                                        "S03Q02_intervention_type": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S03Q02_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S03Q02_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10957,
                                "shortcut": "A",
                                "label": "None",
                                "value": "A"
                            },
                            {
                                "answerId": 10958,
                                "shortcut": "B",
                                "label": "One",
                                "value": "B"
                            },
                            {
                                "answerId": 10959,
                                "shortcut": "C",
                                "label": "Two",
                                "value": "C"
                            },
                            {
                                "answerId": 10960,
                                "shortcut": "D",
                                "label": "Three or More",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S03Q03",
                        "customClass": "col-form-label",
                        "id": "S03Q03",
                        "label": " 3. # of Prior Probation/Parole Periods",
                        "type": "radio",
                        "key": "S03Q03"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  3. # of Prior Probation/Parole Periods",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S03Q03_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S03Q03_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S03Q03_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S03Q03_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S03Q03_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S03Q03_intervention_specify": "",
                                        "addIntervention": false,
                                        "S03Q03_intervention_type": "",
                                        "S03Q03_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S03Q03_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S03Q03_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10961,
                                "shortcut": "A",
                                "label": "None",
                                "value": "A"
                            },
                            {
                                "answerId": 10962,
                                "shortcut": "B",
                                "label": "One",
                                "value": "B"
                            },
                            {
                                "answerId": 10963,
                                "shortcut": "C",
                                "label": "Two",
                                "value": "C"
                            },
                            {
                                "answerId": 10964,
                                "shortcut": "D",
                                "label": "Three or More",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S03Q04",
                        "customClass": "col-form-label",
                        "id": "S03Q04",
                        "label": " 4. # of Prior Failures to Comply",
                        "type": "radio",
                        "key": "S03Q04"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  4. # of Prior Failures to Comply",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S03Q04_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S03Q04_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S03Q04_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S03Q04_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S03Q04_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S03Q04_intervention_desc": "",
                                        "S03Q04_intervention_type": "",
                                        "addIntervention": false,
                                        "S03Q04_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S03Q04_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S03Q04_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "widget": "choicesjs",
                        "data": {
                            "values": [
                                {
                                    "answerId": 10987,
                                    "shortcut": "1",
                                    "label": "1",
                                    "value": "1"
                                },
                                {
                                    "answerId": 11019,
                                    "shortcut": "2",
                                    "label": "2",
                                    "value": "2"
                                },
                                {
                                    "answerId": 11030,
                                    "shortcut": "3",
                                    "label": "3",
                                    "value": "3"
                                },
                                {
                                    "answerId": 11041,
                                    "shortcut": "4",
                                    "label": "4",
                                    "value": "4"
                                },
                                {
                                    "answerId": 11052,
                                    "shortcut": "5",
                                    "label": "5",
                                    "value": "5"
                                },
                                {
                                    "answerId": 11063,
                                    "shortcut": "6",
                                    "label": "6",
                                    "value": "6"
                                },
                                {
                                    "answerId": 11074,
                                    "shortcut": "7",
                                    "label": "7",
                                    "value": "7"
                                },
                                {
                                    "answerId": 10965,
                                    "shortcut": "8",
                                    "label": "8",
                                    "value": "8"
                                },
                                {
                                    "answerId": 10976,
                                    "shortcut": "9",
                                    "label": "9",
                                    "value": "9"
                                },
                                {
                                    "answerId": 10988,
                                    "label": "10",
                                    "value": "10"
                                },
                                {
                                    "answerId": 10999,
                                    "label": "11",
                                    "value": "11"
                                },
                                {
                                    "answerId": 11010,
                                    "label": "12",
                                    "value": "12"
                                },
                                {
                                    "answerId": 11012,
                                    "label": "13",
                                    "value": "13"
                                },
                                {
                                    "answerId": 11013,
                                    "label": "14",
                                    "value": "14"
                                },
                                {
                                    "answerId": 11014,
                                    "label": "15",
                                    "value": "15"
                                },
                                {
                                    "answerId": 11015,
                                    "label": "16",
                                    "value": "16"
                                },
                                {
                                    "answerId": 11016,
                                    "label": "17",
                                    "value": "17"
                                },
                                {
                                    "answerId": 11017,
                                    "label": "18",
                                    "value": "18"
                                },
                                {
                                    "answerId": 11018,
                                    "label": "19",
                                    "value": "19"
                                },
                                {
                                    "answerId": 11020,
                                    "label": "20",
                                    "value": "20"
                                },
                                {
                                    "answerId": 11021,
                                    "label": "21",
                                    "value": "21"
                                },
                                {
                                    "answerId": 11022,
                                    "label": "22",
                                    "value": "22"
                                },
                                {
                                    "answerId": 11023,
                                    "label": "23",
                                    "value": "23"
                                },
                                {
                                    "answerId": 11024,
                                    "label": "24",
                                    "value": "24"
                                },
                                {
                                    "answerId": 11025,
                                    "label": "25",
                                    "value": "25"
                                },
                                {
                                    "answerId": 11026,
                                    "label": "26",
                                    "value": "26"
                                },
                                {
                                    "answerId": 11027,
                                    "label": "27",
                                    "value": "27"
                                },
                                {
                                    "answerId": 11028,
                                    "label": "28",
                                    "value": "28"
                                },
                                {
                                    "answerId": 11029,
                                    "label": "29",
                                    "value": "29"
                                },
                                {
                                    "answerId": 11031,
                                    "label": "30",
                                    "value": "30"
                                },
                                {
                                    "answerId": 11032,
                                    "label": "31",
                                    "value": "31"
                                },
                                {
                                    "answerId": 11033,
                                    "label": "32",
                                    "value": "32"
                                },
                                {
                                    "answerId": 11034,
                                    "label": "33",
                                    "value": "33"
                                },
                                {
                                    "answerId": 11035,
                                    "label": "34",
                                    "value": "34"
                                },
                                {
                                    "answerId": 11036,
                                    "label": "35",
                                    "value": "35"
                                },
                                {
                                    "answerId": 11037,
                                    "label": "36",
                                    "value": "36"
                                },
                                {
                                    "answerId": 11038,
                                    "label": "37",
                                    "value": "37"
                                },
                                {
                                    "answerId": 11039,
                                    "label": "38",
                                    "value": "38"
                                },
                                {
                                    "answerId": 11040,
                                    "label": "39",
                                    "value": "39"
                                },
                                {
                                    "answerId": 11042,
                                    "label": "40",
                                    "value": "40"
                                },
                                {
                                    "answerId": 11043,
                                    "label": "41",
                                    "value": "41"
                                },
                                {
                                    "answerId": 11044,
                                    "label": "42",
                                    "value": "42"
                                },
                                {
                                    "answerId": 11045,
                                    "label": "43",
                                    "value": "43"
                                },
                                {
                                    "answerId": 11046,
                                    "label": "44",
                                    "value": "44"
                                },
                                {
                                    "answerId": 11047,
                                    "label": "45",
                                    "value": "45"
                                },
                                {
                                    "answerId": 11048,
                                    "label": "46",
                                    "value": "46"
                                },
                                {
                                    "answerId": 11049,
                                    "label": "47",
                                    "value": "47"
                                },
                                {
                                    "answerId": 11050,
                                    "label": "48",
                                    "value": "48"
                                },
                                {
                                    "answerId": 11051,
                                    "label": "49",
                                    "value": "49"
                                },
                                {
                                    "answerId": 11053,
                                    "label": "50",
                                    "value": "50"
                                },
                                {
                                    "answerId": 11054,
                                    "label": "51",
                                    "value": "51"
                                },
                                {
                                    "answerId": 11055,
                                    "label": "52",
                                    "value": "52"
                                },
                                {
                                    "answerId": 11056,
                                    "label": "53",
                                    "value": "53"
                                },
                                {
                                    "answerId": 11057,
                                    "label": "54",
                                    "value": "54"
                                },
                                {
                                    "answerId": 11058,
                                    "label": "55",
                                    "value": "55"
                                },
                                {
                                    "answerId": 11059,
                                    "label": "56",
                                    "value": "56"
                                },
                                {
                                    "answerId": 11060,
                                    "label": "57",
                                    "value": "57"
                                },
                                {
                                    "answerId": 11061,
                                    "label": "58",
                                    "value": "58"
                                },
                                {
                                    "answerId": 11062,
                                    "label": "59",
                                    "value": "59"
                                },
                                {
                                    "answerId": 11064,
                                    "label": "60",
                                    "value": "60"
                                },
                                {
                                    "answerId": 11065,
                                    "label": "61",
                                    "value": "61"
                                },
                                {
                                    "answerId": 11066,
                                    "label": "62",
                                    "value": "62"
                                },
                                {
                                    "answerId": 11067,
                                    "label": "63",
                                    "value": "63"
                                },
                                {
                                    "answerId": 11068,
                                    "label": "64",
                                    "value": "64"
                                },
                                {
                                    "answerId": 11069,
                                    "label": "65",
                                    "value": "65"
                                },
                                {
                                    "answerId": 11070,
                                    "label": "66",
                                    "value": "66"
                                },
                                {
                                    "answerId": 11071,
                                    "label": "67",
                                    "value": "67"
                                },
                                {
                                    "answerId": 11072,
                                    "label": "68",
                                    "value": "68"
                                },
                                {
                                    "answerId": 11073,
                                    "label": "69",
                                    "value": "69"
                                },
                                {
                                    "answerId": 11075,
                                    "label": "70",
                                    "value": "70"
                                },
                                {
                                    "answerId": 11076,
                                    "label": "71",
                                    "value": "71"
                                },
                                {
                                    "answerId": 11077,
                                    "label": "72",
                                    "value": "72"
                                },
                                {
                                    "answerId": 11078,
                                    "label": "73",
                                    "value": "73"
                                },
                                {
                                    "answerId": 11079,
                                    "label": "74",
                                    "value": "74"
                                },
                                {
                                    "answerId": 11080,
                                    "label": "75",
                                    "value": "75"
                                },
                                {
                                    "answerId": 11081,
                                    "label": "76",
                                    "value": "76"
                                },
                                {
                                    "answerId": 11082,
                                    "label": "77",
                                    "value": "77"
                                },
                                {
                                    "answerId": 11083,
                                    "label": "78",
                                    "value": "78"
                                },
                                {
                                    "answerId": 11084,
                                    "label": "79",
                                    "value": "79"
                                },
                                {
                                    "answerId": 10966,
                                    "label": "80",
                                    "value": "80"
                                },
                                {
                                    "answerId": 10967,
                                    "label": "81",
                                    "value": "81"
                                },
                                {
                                    "answerId": 10968,
                                    "label": "82",
                                    "value": "82"
                                },
                                {
                                    "answerId": 10969,
                                    "label": "83",
                                    "value": "83"
                                },
                                {
                                    "answerId": 10970,
                                    "label": "84",
                                    "value": "84"
                                },
                                {
                                    "answerId": 10971,
                                    "label": "85",
                                    "value": "85"
                                },
                                {
                                    "answerId": 10972,
                                    "label": "86",
                                    "value": "86"
                                },
                                {
                                    "answerId": 10973,
                                    "label": "87",
                                    "value": "87"
                                },
                                {
                                    "answerId": 10974,
                                    "label": "88",
                                    "value": "88"
                                },
                                {
                                    "answerId": 10975,
                                    "label": "89",
                                    "value": "89"
                                },
                                {
                                    "answerId": 10977,
                                    "label": "90",
                                    "value": "90"
                                },
                                {
                                    "answerId": 10978,
                                    "label": "91",
                                    "value": "91"
                                },
                                {
                                    "answerId": 10979,
                                    "label": "92",
                                    "value": "92"
                                },
                                {
                                    "answerId": 10980,
                                    "label": "93",
                                    "value": "93"
                                },
                                {
                                    "answerId": 10981,
                                    "label": "94",
                                    "value": "94"
                                },
                                {
                                    "answerId": 10982,
                                    "label": "95",
                                    "value": "95"
                                },
                                {
                                    "answerId": 10983,
                                    "label": "96",
                                    "value": "96"
                                },
                                {
                                    "answerId": 10984,
                                    "label": "97",
                                    "value": "97"
                                },
                                {
                                    "answerId": 10985,
                                    "label": "98",
                                    "value": "98"
                                },
                                {
                                    "answerId": 10986,
                                    "label": "99",
                                    "value": "99"
                                },
                                {
                                    "answerId": 10989,
                                    "label": "100",
                                    "value": "100"
                                },
                                {
                                    "answerId": 10990,
                                    "label": "101",
                                    "value": "101"
                                },
                                {
                                    "answerId": 10991,
                                    "label": "102",
                                    "value": "102"
                                },
                                {
                                    "answerId": 10992,
                                    "label": "103",
                                    "value": "103"
                                },
                                {
                                    "answerId": 10993,
                                    "label": "104",
                                    "value": "104"
                                },
                                {
                                    "answerId": 10994,
                                    "label": "105",
                                    "value": "105"
                                },
                                {
                                    "answerId": 10995,
                                    "label": "106",
                                    "value": "106"
                                },
                                {
                                    "answerId": 10996,
                                    "label": "107",
                                    "value": "107"
                                },
                                {
                                    "answerId": 10997,
                                    "label": "108",
                                    "value": "108"
                                },
                                {
                                    "answerId": 10998,
                                    "label": "109",
                                    "value": "109"
                                },
                                {
                                    "answerId": 11000,
                                    "label": "110",
                                    "value": "110"
                                },
                                {
                                    "answerId": 11001,
                                    "label": "111",
                                    "value": "111"
                                },
                                {
                                    "answerId": 11002,
                                    "label": "112",
                                    "value": "112"
                                },
                                {
                                    "answerId": 11003,
                                    "label": "113",
                                    "value": "113"
                                },
                                {
                                    "answerId": 11004,
                                    "label": "114",
                                    "value": "114"
                                },
                                {
                                    "answerId": 11005,
                                    "label": "115",
                                    "value": "115"
                                },
                                {
                                    "answerId": 11006,
                                    "label": "116",
                                    "value": "116"
                                },
                                {
                                    "answerId": 11007,
                                    "label": "117",
                                    "value": "117"
                                },
                                {
                                    "answerId": 11008,
                                    "label": "118",
                                    "value": "118"
                                },
                                {
                                    "answerId": 11009,
                                    "label": "119",
                                    "value": "119"
                                },
                                {
                                    "answerId": 11011,
                                    "label": "120",
                                    "value": "120"
                                }
                            ]
                        },
                        "tooltip": "S03Q05",
                        "customClass": "col-form-label",
                        "placeholder": "Select value",
                        "id": "S03Q05",
                        "label": " 5. Age at First Conviction",
                        "type": "select",
                        "key": "S03Q05"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  5. Age at First Conviction",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S03Q05_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S03Q05_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S03Q05_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S03Q05_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S03Q05_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S03Q05_intervention_type": "",
                                        "S03Q05_intervention_desc": "",
                                        "S03Q05_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S03Q05_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S03Q05_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11085,
                                "shortcut": "A",
                                "label": "No",
                                "value": "A"
                            },
                            {
                                "answerId": 11086,
                                "shortcut": "B",
                                "label": "Yes",
                                "value": "B"
                            }
                        ],
                        "tooltip": "S03Q06",
                        "customClass": "col-form-label",
                        "id": "S03Q06",
                        "label": " 6. Ever Imprisoned After a Conviction",
                        "type": "radio",
                        "key": "S03Q06"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  6. Ever Imprisoned After a Conviction",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S03Q06_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S03Q06_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S03Q06_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S03Q06_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S03Q06_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S03Q06_intervention_type": "",
                                        "addIntervention": false,
                                        "S03Q06_intervention_desc": "",
                                        "S03Q06_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S03Q06_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S03Q06_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11087,
                                "shortcut": "A",
                                "label": "None",
                                "value": "A"
                            },
                            {
                                "answerId": 11088,
                                "shortcut": "B",
                                "label": "Low",
                                "value": "B"
                            },
                            {
                                "answerId": 11089,
                                "shortcut": "C",
                                "label": "Medium",
                                "value": "C"
                            },
                            {
                                "answerId": 11090,
                                "shortcut": "D",
                                "label": "High",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S03Q07",
                        "customClass": "col-form-label",
                        "id": "S03Q07",
                        "label": " 7. Seriousness of Instit. Escape History",
                        "type": "radio",
                        "key": "S03Q07"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  7. Seriousness of Instit. Escape History",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S03Q07_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S03Q07_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S03Q07_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S03Q07_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S03Q07_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S03Q07_intervention_desc": "",
                                        "S03Q07_intervention_type": "",
                                        "S03Q07_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S03Q07_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S03Q07_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11091,
                                "shortcut": "A",
                                "label": "No",
                                "value": "A"
                            },
                            {
                                "answerId": 11092,
                                "shortcut": "B",
                                "label": "Yes",
                                "value": "B"
                            }
                        ],
                        "tooltip": "S03Q08",
                        "customClass": "col-form-label",
                        "id": "S03Q08",
                        "label": " 8. History of Weapon Use",
                        "type": "radio",
                        "key": "S03Q08"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  8. History of Weapon Use",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S03Q08_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S03Q08_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S03Q08_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S03Q08_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S03Q08_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S03Q08_intervention_specify": "",
                                        "addIntervention": false,
                                        "S03Q08_intervention_type": "",
                                        "S03Q08_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S03Q08_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S03Q08_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11093,
                                "shortcut": "A",
                                "label": "None",
                                "value": "A"
                            },
                            {
                                "answerId": 11094,
                                "shortcut": "B",
                                "label": "Low",
                                "value": "B"
                            },
                            {
                                "answerId": 11095,
                                "shortcut": "C",
                                "label": "Medium",
                                "value": "C"
                            },
                            {
                                "answerId": 11096,
                                "shortcut": "D",
                                "label": "High",
                                "value": "D"
                            }
                        ],
                        "tooltip": "S03Q09",
                        "customClass": "col-form-label",
                        "id": "S03Q09",
                        "label": " 9. Frequency/Severity of Violence in History",
                        "type": "radio",
                        "key": "S03Q09"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  9. Frequency/Severity of Violence in History",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S03Q09_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S03Q09_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S03Q09_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S03Q09_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S03Q09_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S03Q09_intervention_type": "",
                                        "S03Q09_intervention_desc": "",
                                        "addIntervention": false,
                                        "S03Q09_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S03Q09_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S03Q09_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "tooltip": "S03Q10",
                        "customClass": "col-form-label",
                        "id": "S03Q10",
                        "label": " 10. Risks Remarks",
                        "type": "textfield",
                        "key": "S03Q10"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  10. Risks Remarks",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S03Q10_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S03Q10_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S03Q10_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S03Q10_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S03Q10_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S03Q10_intervention_type": "",
                                        "S03Q10_intervention_desc": "",
                                        "S03Q10_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S03Q10_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S03Q10_intervention_datagrid"
                    }
                ],
                "theme": "primary",
                "title": "Risk  Assessment - Criminal History",
                "type": "well",
                "collapsible": true,
                "key": "S03"
            },
            {
                "tag": "h1",
                "type": "htmlelement",
                "key": "S04_header",
                "content": "Special Factors"
            },
            {
                "input": false,
                "components": [
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11097,
                                "shortcut": "N",
                                "label": "No",
                                "value": "N"
                            },
                            {
                                "answerId": 11098,
                                "shortcut": "Y",
                                "label": "Yes",
                                "value": "Y"
                            },
                            {
                                "answerId": 11099,
                                "shortcut": "S",
                                "label": "Sometimes",
                                "value": "S"
                            }
                        ],
                        "tooltip": "S04Q01",
                        "customClass": "col-form-label",
                        "id": "S04Q01",
                        "label": " 1. Sex Offender?",
                        "type": "radio",
                        "key": "S04Q01"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  1. Sex Offender?",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S04Q01_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S04Q01_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S04Q01_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S04Q01_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S04Q01_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S04Q01_intervention_specify": "",
                                        "addIntervention": false,
                                        "S04Q01_intervention_type": "",
                                        "S04Q01_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S04Q01_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S04Q01_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11100,
                                "shortcut": "M",
                                "label": "Maybe",
                                "value": "M"
                            },
                            {
                                "answerId": 11101,
                                "shortcut": "N",
                                "label": "No",
                                "value": "N"
                            },
                            {
                                "answerId": 11102,
                                "shortcut": "Y",
                                "label": "Yes",
                                "value": "Y"
                            }
                        ],
                        "tooltip": "S04Q02",
                        "customClass": "col-form-label",
                        "id": "S04Q02",
                        "label": " 2. Violent Offender?",
                        "type": "radio",
                        "key": "S04Q02"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  2. Violent Offender?",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S04Q02_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S04Q02_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S04Q02_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S04Q02_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S04Q02_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S04Q02_intervention_specify": "",
                                        "S04Q02_intervention_desc": "",
                                        "S04Q02_intervention_type": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S04Q02_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S04Q02_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11103,
                                "shortcut": "N",
                                "label": "No",
                                "value": "N"
                            },
                            {
                                "answerId": 11104,
                                "shortcut": "Y",
                                "label": "Yes",
                                "value": "Y"
                            }
                        ],
                        "tooltip": "S04Q03",
                        "customClass": "col-form-label",
                        "id": "S04Q03",
                        "label": " 3. Mentally Disordered?",
                        "type": "radio",
                        "key": "S04Q03"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  3. Mentally Disordered?",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S04Q03_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S04Q03_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S04Q03_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S04Q03_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S04Q03_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S04Q03_intervention_specify": "",
                                        "S04Q03_intervention_desc": "",
                                        "addIntervention": false,
                                        "S04Q03_intervention_type": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S04Q03_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S04Q03_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 11105,
                                "shortcut": "S",
                                "label": "Sometimes",
                                "value": "S"
                            },
                            {
                                "answerId": 11106,
                                "shortcut": "M",
                                "label": "Maybe",
                                "value": "M"
                            },
                            {
                                "answerId": 11107,
                                "shortcut": "N",
                                "label": "No",
                                "value": "N"
                            },
                            {
                                "answerId": 11108,
                                "shortcut": "Y",
                                "label": "Yes",
                                "value": "Y"
                            }
                        ],
                        "tooltip": "S04Q04",
                        "customClass": "col-form-label",
                        "id": "S04Q04",
                        "label": " 4. Domestic Violence?",
                        "type": "radio",
                        "key": "S04Q04"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  4. Domestic Violence?",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S04Q04_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S04Q04_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S04Q04_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S04Q04_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S04Q04_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S04Q04_intervention_specify": "",
                                        "S04Q04_intervention_type": "",
                                        "S04Q04_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S04Q04_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S04Q04_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "tooltip": "S04Q05",
                        "customClass": "col-form-label",
                        "id": "S04Q05",
                        "label": " 5. Add. Assessmts indicated or Other Factors?",
                        "type": "textfield",
                        "key": "S04Q05"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  5. Add. Assessmts indicated or Other Factors?",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S04Q05_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S04Q05_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S04Q05_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S04Q05_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S04Q05_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S04Q05_intervention_desc": "",
                                        "S04Q05_intervention_type": "",
                                        "addIntervention": false,
                                        "S04Q05_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S04Q05_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S04Q05_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "widget": {
                            "mode": "single",
                            "enableTime": false,
                            "format": "YYYY-MM-dd",
                            "type": "calendar",
                            "locale": "en"
                        },
                        "enableTime": false,
                        "tooltip": "S04Q07",
                        "tableView": false,
                        "datePicker": {
                            "disableWeekDays": false,
                            "disableWeekends": false
                        },
                        "customClass": "col-form-label",
                        "id": "S04Q07",
                        "label": " 7. q 777",
                        "type": "datetime",
                        "key": "S04Q07"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  7. q 777",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S04Q07_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S04Q07_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S04Q07_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S04Q07_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S04Q07_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S04Q07_intervention_type": "",
                                        "addIntervention": false,
                                        "S04Q07_intervention_desc": "",
                                        "S04Q07_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S04Q07_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S04Q07_intervention_datagrid"
                    }
                ],
                "theme": "primary",
                "title": "Special Factors",
                "type": "well",
                "collapsible": true,
                "key": "S04"
            },
            {
                "tag": "h1",
                "type": "htmlelement",
                "key": "S05_header",
                "content": "asdjklfasdfkljdfsa"
            },
            {
                "input": false,
                "components": [
                    {
                        "input": true,
                        "widget": {
                            "mode": "single",
                            "enableTime": false,
                            "format": "YYYY-MM-dd",
                            "type": "calendar",
                            "locale": "en"
                        },
                        "enableTime": false,
                        "tooltip": "S05Q07",
                        "tableView": false,
                        "datePicker": {
                            "disableWeekDays": false,
                            "disableWeekends": false
                        },
                        "customClass": "col-form-label",
                        "id": "S05Q07",
                        "label": " 7. 999",
                        "type": "datetime",
                        "key": "S05Q07"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  7. 999",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S05Q07_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S05Q07_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S05Q07_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S05Q07_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S05Q07_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S05Q07_intervention_type": "",
                                        "S05Q07_intervention_specify": "",
                                        "S05Q07_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S05Q07_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S05Q07_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "tooltip": "S05Q08",
                        "customClass": "col-form-label",
                        "id": "S05Q08",
                        "label": " 8. 888",
                        "type": "number",
                        "inputFormat": "plain",
                        "key": "S05Q08"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  8. 888",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S05Q08_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S05Q08_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S05Q08_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S05Q08_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S05Q08_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S05Q08_intervention_specify": "",
                                        "S05Q08_intervention_type": "",
                                        "addIntervention": false,
                                        "S05Q08_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S05Q08_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S05Q08_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "widget": {
                            "mode": "single",
                            "enableTime": false,
                            "format": "YYYY-MM-dd",
                            "type": "calendar",
                            "locale": "en"
                        },
                        "enableTime": false,
                        "tooltip": "S05Q34",
                        "tableView": false,
                        "datePicker": {
                            "disableWeekDays": false,
                            "disableWeekends": false
                        },
                        "customClass": "col-form-label",
                        "id": "S05Q34",
                        "label": " 34. sadfasdf",
                        "type": "datetime",
                        "key": "S05Q34"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  34. sadfasdf",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S05Q34_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S05Q34_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S05Q34_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S05Q34_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S05Q34_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S05Q34_intervention_specify": "",
                                        "S05Q34_intervention_type": "",
                                        "S05Q34_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S05Q34_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S05Q34_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "widget": {
                            "mode": "single",
                            "enableTime": false,
                            "format": "YYYY-MM-dd",
                            "type": "calendar",
                            "locale": "en"
                        },
                        "enableTime": false,
                        "tooltip": "S05Q1234",
                        "tableView": false,
                        "datePicker": {
                            "disableWeekDays": false,
                            "disableWeekends": false
                        },
                        "customClass": "col-form-label",
                        "id": "S05Q1234",
                        "label": " 1234. test 1234",
                        "type": "datetime",
                        "key": "S05Q1234"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  1234. test 1234",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S05Q1234_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S05Q1234_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S05Q1234_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S05Q1234_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S05Q1234_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S05Q1234_intervention_type": "",
                                        "S05Q1234_intervention_specify": "",
                                        "addIntervention": false,
                                        "S05Q1234_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S05Q1234_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S05Q1234_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "tooltip": "S05Q5555",
                        "customClass": "col-form-label",
                        "id": "S05Q5555",
                        "label": " 5555. five",
                        "type": "textfield",
                        "key": "S05Q5555"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  5555. five",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S05Q5555_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S05Q5555_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S05Q5555_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S05Q5555_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S05Q5555_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S05Q5555_intervention_specify": "",
                                        "S05Q5555_intervention_type": "",
                                        "addIntervention": false,
                                        "S05Q5555_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S05Q5555_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S05Q5555_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "widget": {
                            "mode": "single",
                            "enableTime": false,
                            "format": "YYYY-MM-dd",
                            "type": "calendar",
                            "locale": "en"
                        },
                        "enableTime": false,
                        "tooltip": "S05Q6666",
                        "tableView": false,
                        "datePicker": {
                            "disableWeekDays": false,
                            "disableWeekends": false
                        },
                        "customClass": "col-form-label",
                        "id": "S05Q6666",
                        "label": " 6666. on number 6",
                        "type": "datetime",
                        "key": "S05Q6666"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  6666. on number 6",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S05Q6666_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S05Q6666_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S05Q6666_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S05Q6666_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S05Q6666_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S05Q6666_intervention_desc": "",
                                        "addIntervention": false,
                                        "S05Q6666_intervention_type": "",
                                        "S05Q6666_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S05Q6666_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S05Q6666_intervention_datagrid"
                    }
                ],
                "theme": "primary",
                "title": "asdjklfasdfkljdfsa",
                "type": "well",
                "collapsible": true,
                "key": "S05"
            },
            {
                "tag": "h1",
                "type": "htmlelement",
                "key": "S06_header",
                "content": "Number seven"
            },
            {
                "input": false,
                "components": [
                    {
                        "input": true,
                        "widget": "choicesjs",
                        "multiple": true,
                        "tooltip": "S06Q01",
                        "customClass": "col-form-label",
                        "placeholder": "Select value",
                        "id": "S06Q01",
                        "label": " 1. q7-1",
                        "type": "select",
                        "key": "S06Q01"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  1. q7-1",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S06Q01_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S06Q01_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S06Q01_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S06Q01_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S06Q01_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S06Q01_intervention_specify": "",
                                        "addIntervention": false,
                                        "S06Q01_intervention_desc": "",
                                        "S06Q01_intervention_type": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S06Q01_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S06Q01_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "tooltip": "S06Q03",
                        "customClass": "col-form-label",
                        "id": "S06Q03",
                        "label": " 3. qq",
                        "type": "number",
                        "inputFormat": "plain",
                        "key": "S06Q03"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  3. qq",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S06Q03_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S06Q03_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S06Q03_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S06Q03_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S06Q03_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S06Q03_intervention_desc": "",
                                        "S06Q03_intervention_specify": "",
                                        "S06Q03_intervention_type": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S06Q03_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S06Q03_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "tooltip": "S06Q06",
                        "customClass": "col-form-label",
                        "id": "S06Q06",
                        "label": " 6. q7-2",
                        "type": "textfield",
                        "key": "S06Q06"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  6. q7-2",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S06Q06_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S06Q06_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S06Q06_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S06Q06_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S06Q06_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S06Q06_intervention_specify": "",
                                        "addIntervention": false,
                                        "S06Q06_intervention_type": "",
                                        "S06Q06_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S06Q06_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S06Q06_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10902,
                                "label": "aaa",
                                "value": "aa"
                            }
                        ],
                        "tooltip": "S06Q07",
                        "customClass": "col-form-label",
                        "id": "S06Q07",
                        "label": " 7. iiiii",
                        "type": "radio",
                        "key": "S06Q07"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  7. iiiii",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S06Q07_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S06Q07_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S06Q07_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S06Q07_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S06Q07_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S06Q07_intervention_desc": "",
                                        "S06Q07_intervention_type": "",
                                        "addIntervention": false,
                                        "S06Q07_intervention_specify": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S06Q07_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S06Q07_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10903,
                                "shortcut": "a",
                                "label": "aaa",
                                "value": "a"
                            }
                        ],
                        "tooltip": "S06Q11",
                        "customClass": "col-form-label",
                        "id": "S06Q11",
                        "label": " 11. 111",
                        "type": "radio",
                        "key": "S06Q11"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  11. 111",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S06Q11_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S06Q11_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S06Q11_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S06Q11_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S06Q11_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S06Q11_intervention_specify": "",
                                        "addIntervention": false,
                                        "S06Q11_intervention_type": "",
                                        "S06Q11_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S06Q11_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S06Q11_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10907,
                                "label": "88888",
                                "value": "88"
                            }
                        ],
                        "tooltip": "S06Q22",
                        "customClass": "col-form-label",
                        "id": "S06Q22",
                        "label": " 22. 222",
                        "type": "radio",
                        "key": "S06Q22"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  22. 222",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S06Q22_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S06Q22_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S06Q22_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S06Q22_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S06Q22_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S06Q22_intervention_specify": "",
                                        "S06Q22_intervention_type": "",
                                        "S06Q22_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S06Q22_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S06Q22_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10904,
                                "shortcut": "a",
                                "label": "aaaa",
                                "value": "a"
                            }
                        ],
                        "tooltip": "S06Q999",
                        "customClass": "col-form-label",
                        "id": "S06Q999",
                        "label": " 999. iiii",
                        "type": "radio",
                        "key": "S06Q999"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  999. iiii",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S06Q999_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S06Q999_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S06Q999_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S06Q999_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S06Q999_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "S06Q999_intervention_specify": "",
                                        "S06Q999_intervention_type": "",
                                        "addIntervention": false,
                                        "S06Q999_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S06Q999_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S06Q999_intervention_datagrid"
                    },
                    {
                        "input": true,
                        "optionsLabelPosition": "right",
                        "values": [
                            {
                                "answerId": 10908,
                                "shortcut": "a",
                                "label": "aaaa",
                                "value": "a"
                            }
                        ],
                        "tooltip": "S06Q9999",
                        "customClass": "col-form-label",
                        "id": "S06Q9999",
                        "label": " 9999. kkkkk",
                        "type": "radio",
                        "key": "S06Q9999"
                    },
                    {
                        "input": true,
                        "tableView": true,
                        "label": "Comments",
                        "placeholder": "Comment for:  9999. kkkkk",
                        "type": "textarea",
                        "showCharCount": true,
                        "autoExpand": false,
                        "key": "S06Q9999_COMMENT",
                        "validate": {
                            "maxLength": 2000
                        }
                    },
                    {
                        "input": true,
                        "defaultValue": false,
                        "tableView": false,
                        "label": "Intervention Needed",
                        "type": "checkbox",
                        "key": "S06Q9999_intervention_checkbox"
                    },
                    {
                        "components": [
                            {
                                "input": false,
                                "components": [
                                    {
                                        "input": false,
                                        "columns": [
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "widget": "choicesjs",
                                                        "data": {
                                                            "values": [
                                                                {
                                                                    "label": "Academic / Vocational Skills",
                                                                    "value": "ACVS"
                                                                },
                                                                {
                                                                    "label": "Attitude Assessment",
                                                                    "value": "ATAS"
                                                                },
                                                                {
                                                                    "label": "Behavioural / Emotional Stability (Personality)",
                                                                    "value": "BEES"
                                                                },
                                                                {
                                                                    "label": "Companions / Significant Others",
                                                                    "value": "COSO"
                                                                },
                                                                {
                                                                    "label": "Employment Pattern",
                                                                    "value": "EMPA"
                                                                },
                                                                {
                                                                    "label": "Family Relationships",
                                                                    "value": "FARE"
                                                                },
                                                                {
                                                                    "label": "Financial Management",
                                                                    "value": "FIMA"
                                                                },
                                                                {
                                                                    "label": "Living Arrangements",
                                                                    "value": "LIAR"
                                                                },
                                                                {
                                                                    "label": "Other Intervention",
                                                                    "value": "OTHR"
                                                                },
                                                                {
                                                                    "label": "Pro-criminal Attitudes",
                                                                    "value": "PRCA"
                                                                },
                                                                {
                                                                    "label": "Substance Misuse",
                                                                    "value": "SUMI"
                                                                }
                                                            ]
                                                        },
                                                        "tableView": true,
                                                        "label": "Select Intervention Type",
                                                        "type": "select",
                                                        "key": "S06Q9999_intervention_type"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            },
                                            {
                                                "pull": 0,
                                                "components": [
                                                    {
                                                        "input": true,
                                                        "tableView": true,
                                                        "label": "Specify",
                                                        "type": "textfield",
                                                        "key": "S06Q9999_intervention_specify"
                                                    }
                                                ],
                                                "offset": 0,
                                                "size": "md",
                                                "currentWidth": 6,
                                                "width": 6,
                                                "push": 0
                                            }
                                        ],
                                        "tableView": false,
                                        "label": "Columns",
                                        "type": "columns",
                                        "key": "columns"
                                    },
                                    {
                                        "input": true,
                                        "tableView": true,
                                        "label": "Description",
                                        "type": "textarea",
                                        "autoExpand": false,
                                        "key": "S06Q9999_intervention_desc"
                                    }
                                ],
                                "defaultValue": [
                                    {
                                        "addIntervention": false,
                                        "S06Q9999_intervention_specify": "",
                                        "S06Q9999_intervention_type": "",
                                        "S06Q9999_intervention_desc": ""
                                    }
                                ],
                                "dataGridLabel": true,
                                "tableView": false,
                                "label": "Panel",
                                "type": "panel",
                                "collapsible": false,
                                "key": "panel",
                                "hideLabel": true
                            }
                        ],
                        "addAnother": "Add Intervention",
                        "enableRowGroups": false,
                        "conditional": {
                            "show": true,
                            "eq": "true",
                            "when": "S06Q9999_intervention_checkbox"
                        },
                        "tableView": false,
                        "reorder": false,
                        "label": "Interventions",
                        "layoutFixed": false,
                        "type": "datagrid",
                        "initEmpty": false,
                        "input": true,
                        "addAnotherPosition": "bottom",
                        "key": "S06Q9999_intervention_datagrid"
                    }
                ],
                "theme": "primary",
                "title": "Number seven",
                "type": "well",
                "collapsible": true,
                "key": "S06"
            },
            {
                "input": false,
                "theme": "primary",
                "title": "Summary",
                "type": "well",
                "collapsible": true,
                "key": "S07",
                "properties": {
                    "summary": true
                }
            }
        ],
        "display": "form"
    };
    return [null, JSON.stringify(tmpJson)];
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
    // try {
    //     const { data } = await axiosClient.get('/forms/client/answers/' + csNumber + '/' + clientFormId);
    //     return [null, data];
    // } catch (error) {
    //     return [error];
    // }
    let tmpJson = {
        "data": {
            "S01Q01": "H",
            "S02Q02": "B",
            "S03Q04": "B",
            "S01Q02": "L",
            "S02Q03": "D",
            "S03Q03": "B",
            "S04Q04": "N",
            "S03Q02": "C",
            "S04Q03": "Y",
            "S02Q01": "C",
            "S01Q01_COMMENT": "a new comment! updated again",
            "S01Q03_intervention_datagrid": [
                {
                    "S01Q03_intervention_type": "ACVS",
                    "S01Q03_intervention_desc": "asdasd"
                }
            ],
            "S01Q03_intervention_checkbox": true,
            "S01Q01_intervention_checkbox": true,
            "S01Q01_intervention_datagrid": [
                {
                    "S01Q01_intervention_desc": "asdas sdf sdf sdf sdf ",
                    "S01Q01_intervention_type": "ACVS"
                },
                {
                    "S01Q01_intervention_desc": "sadadasd",
                    "S01Q01_intervention_type": "LIAR"
                },
                {
                    "S01Q01_intervention_desc": "drugs",
                    "S01Q01_intervention_type": "SUMI"
                }
            ],
            "S03Q09": "C",
            "S02Q06": "B",
            "S03Q08": "A",
            "S02Q07": "D",
            "S01Q03": "bbb",
            "S02Q04": "C",
            "S03Q06": "A",
            "S06Q07": "aa"
        },
        "clientFormId": 389760
    };
    return [null, JSON.stringify(tmpJson)];
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
        //console.log("ClientProfileSearch clientNum", clientNum);
        const { data } = await axiosClient.get('/clients', {
                params: {
                    clientNum: clientNum
                }
            });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to search for RNA list
export async function formSearch(clientNum: String, formType: String, supervisionPeriod: boolean) {
    try{
        console.log("formSearch for RNA List, clientNum: {}, formType: {}, supervisionPeriod: {}", clientNum, formType, supervisionPeriod);
        const { data } = await axiosClient.get('/forms/client/search/' + clientNum, {
                params: {
                    formTypeCd: formType,
                    currentPeriod: supervisionPeriod
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


//-------------------------------------
// Trend analysis
//-------------------------------------
export async function getClientFormFactors( clientNumber:number, reportType: string) {
    try {
        const { data} = await axiosClient.get('/trend/client/' + clientNumber + '/' + reportType + '/factors');
        return [null,data];
    }catch (error) {
        return [error];
    }
}

//-------------------------------------
// Comments
//-------------------------------------
export async function getClientFormComments( clientNumber:number, payload: object) {
    try {
        const { data} = await axiosClient.post('/forms/client/comments/' + clientNumber, payload );
        return [null,data];
    }catch (error) {
        return [error];
    }
}

export default axiosClient;
