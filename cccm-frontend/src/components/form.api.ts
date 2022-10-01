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
    try {
        debugger;
        const { data } = await axiosClient.get(`/forms/client/json/${clientNum}/${formId}`, {
            params: {
                includeOptionValues: true
            }
        });
        return [null, data];
    }catch (error) {
        return [error];
    }
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
    try {
        const { data } = await axiosClient.get('/forms/client/answers/' + csNumber + '/' + clientFormId);
        return [null, data];
    } catch (error) {
        return [error];
    }
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
export async function getClientFormComments( clientNumber:number, payload: object) {
    try {
        const { data} = await axiosClient.post('/forms/client/comments/' + clientNumber, payload );
        return [null,data];
    }catch (error) {
        return [error];
    }
}

export default axiosClient;
