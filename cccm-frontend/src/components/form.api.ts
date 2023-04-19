import { identifier } from '@babel/types';
import 'izitoast/dist/css/iziToast.min.css';
import iZtoast from 'izitoast';
import axios from 'axios';

import { ScriptableLineSegmentContext } from 'chart.js';
import { AxiosError } from 'axios';

const axiosClient = axios.create({
    baseURL: config.VUE_APP_CCCM_API_ENDPOINT,
    timeout: config.VUE_APP_TIMEOUT
});

//------------------------------------------
// USER APIs
//------------------------------------------
// function to async fetch the location info
export async function async_getUserDefaultLocation() {
    try {
        //console.log("VUE_APP_CCCM_API_ENDPOINT: ", config.VUE_APP_CCCM_API_ENDPOINT);
        const { data } = await axiosClient.get('/user/location');
        return [null, data];
    }catch (error) {
        handleError(error);
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
        handleError(error);
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
        handleError(error);
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
        handleError(error);
        return [error];
    }
}

//------------------------------------------
// Lookup APIs
//------------------------------------------
// function to search for form types
export async function lookupFormTypes() {
    try {
        const { data } = await axiosClient.get('/lookup/formtypes');
        return [null, data];
    }catch (error) {
        handleError(error);
        return [error];
    }
}


//------------------------------------------
// Client Form APIs
//------------------------------------------
// get data for summary view
export async function getDataForSummaryView(clientNum: string, formId: number, includeLinkedForm: boolean) {
    try {
        const { data } = await axiosClient.get(`/forms/client/summary/answers/${clientNum}/${formId}`,{
            params: {
                includeLinkedForm: includeLinkedForm,
            }
        });
        return [null, data];
    }catch (error) {
        handleError(error);
        return [error];
    }
}


// function to fetch the form.io template
export async function getFormioTemplate(clientNum: String, formId: number) {
    //console.log("getFormioTemplate: ");
    try {
        const { data } = await axiosClient.get(`/forms/client/json/${clientNum}/${formId}`, {
            params: {
                includeOptionValues: true
            }
        });
        //console.log("getFormioTemplate: ", data);
        return [null, data];
    }catch (error) {
        handleError(error);
        return [error];
    }
}

// function to update form section question data
export async function updateForm( csNumber: number,clientFormId: number, formData: object) {
    try{
        //console.log("Update form payload", formData);
        const { data } = await axiosClient.put('/forms/client/answers/' + csNumber + '/' + clientFormId, formData);
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// get form data (all data returned)
export async function loadFormData(csNumber: number, clientFormId: number) {
    //console.log("load formdata");
    try {
        const { data } = await axiosClient.get('/forms/client/answers/' + csNumber + '/' + clientFormId);
        //console.log("load formdata: ", data);
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// function to clone form
export async function cloneForm(formData: object) {
    try {
        const { data } = await axiosClient.post('/forms/client/clone', formData);
        return [null, data];
    }catch (error) {
        handleError(error);
        return [error];
    }
}

// function to delete form
export async function deleteForm(clientFormId: number, clientNum: String) {
    try {
        const { data } = await axiosClient.delete(`/forms/${clientFormId}/client/${clientNum}/delete`);
        return [null, data];
    }catch (error) {
        handleError(error);
        return [error];
    }
}

// function to create a new form
export async function createForm(formType: String, formData: object) {
    let endpointURL = '/forms/client/' + formType;
    try{
        const { data } = await axiosClient.post(endpointURL, formData);
        return [null, data];
    } catch (error) {
        console.error("Error creating form %o", error);
        handleError(error);
        return [error];
    }
}

// function to validate CRNA form
export async function validateCRNAForm(formData: object) {
    try{
        const { data } = await axiosClient.post('/forms/client/validate/crna', formData);
        return [null, data];
    } catch (error) {
        console.error("Error validating crna form: %o", error);
        handleError(error);
        return [error];
    }
}

// function to validate SARA form
export async function validateSARAForm(formData: object) {
    try{
        const { data } = await axiosClient.post('/forms/client/validate/sara', formData);
        return [null, data];
    } catch (error) {
        console.error("Error validating sara form: %o", error);
        handleError(error);
        return [error];
    }
}

// function to complete a form
export async function completeForm(formData: object) {
    try{
        const { data } = await axiosClient.post('/forms/client/complete', formData);
        return [null, data];
    } catch (error) {
        console.error("Error completing a form: %o", error);
        handleError(error);
        return [error];
    }
}

// function to unlock a form
export async function unlockForm(formData: object) {
    try{
        const { data } = await axiosClient.put('/forms/client/edit', formData);
        return [null, data];
    } catch (error) {
        console.error("Error unlocking a form: %o", error);
        handleError(error);
        return [error];
    }
}

// Get interventions for case plan view
export async function getCasePlanIntervention(csNumber: String, clientFormId: number, includeLinkedForm: boolean) {
    try{
        //console.log("getCasePlanIntervention, csNumber: {}, clientFormId: {}, includeLinkedForm: {}", csNumber, clientFormId, includeLinkedForm);
        const { data } = await axiosClient.get(`/forms/client/intervention/json/${csNumber}/${clientFormId}`, {
            params: {
                includeLinkedForm: includeLinkedForm
            }
        });
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// function to update sources contacted for a client form
export async function updateSourcesContacted(clientFormId: number, sourcesContacted: string) {
    try{
        //console.log("Update source contacted, clientFormId: {}, sourcesContacted: {}", clientFormId, sourcesContacted);
        const { data } = await axiosClient.put(`/forms/client/sourcesContacted/${clientFormId}`,  sourcesContacted);
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// Get client form meta data
export async function getClientFormMetaData(csNumber: String, clientFormId: number) {
    try{
        //console.log("getClientFormMetaData, csNumber: {}, clientFormId: {}", csNumber, clientFormId);
        const { data } = await axiosClient.get(`/forms/client/meta/${csNumber}/${clientFormId}`);
        //console.log("getClientFormMetaData: ", data);
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// Get client form details, used in CmpForm.vue.
export async function getClientFormDetails(csNumber: String, clientFormId: number) {
    try{
        //console.log("getClientFormDetails, csNumber: {}, clientFormId: {}", csNumber, clientFormId);
        const { data } = await axiosClient.get(`/forms/client/summary/${csNumber}/${clientFormId}`);
        //console.log("getClientFormDetails: ", data);
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

//------------------------------------------
// Client Search lookup codes APIs
//------------------------------------------

// function to get addressTypeCodes
export async function lookupAddressTypes() {
    try{
        const { data } = await axiosClient.get('/lookup/address');
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// function to get gender codes
export async function lookupGender() {
    try{
        const { data } = await axiosClient.get('/lookup/genders');
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// function to get identifier type codes
export async function lookupIdTypes() {
    try{
        const { data } = await axiosClient.get('/lookup/identifiers');
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// function to get city codes
export async function lookupCityCodes() {
    try{
        const { data } = await axiosClient.get('/lookup/city');
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// function to get province codes
export async function lookupProvinceCodes() {
    try{
        const { data } = await axiosClient.get('/lookup/province');
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

//------------------------------------------
// Client APIs
//------------------------------------------

// function to search for RNA list
export async function formSearch(clientNum: String, formType: String, currentPeriod: boolean) {
    try{
        //console.log("formSearch for RNA List, clientNum: {}, formType: {}, supervisionPeriod: {}", clientNum, formType, currentPeriod);
        const { data } = await axiosClient.get('/forms/client/search/' + clientNum, {
                params: {
                    formTypeCd: formType,
                    currentPeriod: currentPeriod
                }
            });
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// function to search client based on general info
export async function clientSearchByGeneralInfo(age: number, birthYear: number, gender: String,
    givenName: String, identifier: String, identifierType: String, lastName: String,
    limitToLocation: boolean, range: number, soundex: boolean) {
    try{
        const { data } = await axiosClient.get('/clients', {
            params: {
                lastName: lastName,
                soundex: soundex,
                givenName: givenName,
                birthYear: birthYear,
                age: age,
                range: range,
                limitToLocation: limitToLocation,
                gender: gender,
                identifierType: identifierType,
                identifier: identifier                
            }
        });
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// function to search client based on address info
export async function clientSearchByAddressInfo(address: String, addressType: String, city: String,
    expired: boolean, limitToLocation: boolean, postalCode: String, province: String) {
    try{
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
        handleError(error);
        return [error];
    }
}

// function to retrieve client details.
export async function getClientDetail(clientNum: String) {
    try{
        //console.log("Get client detail by clientNum: ", clientNum);
        const { data } = await axiosClient.get(`/clients/${clientNum}/details`);
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// function to retrieve client addresses.
export async function getClientAddresses(clientNum: String) {
    try{
        //console.log("Get client addresses by clientNum: ", clientNum);
        const { data } = await axiosClient.get(`/clients/${clientNum}/address`);
        return [null, data];
    } catch (error) {
        handleError(error);
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
        handleError(error);
        return [error];
    }
}

//------------------------------------------
// Dashboard APIs
//------------------------------------------
// Supervisor dashboard search
export async function dashboardSupervisorSearch(locationId: number) {
    try{
        //console.log("Officer search by supervisorID: ", supervisorID);
        const { data } = await axiosClient.get('/dashboards/supervisor', {
            params: {
                locationId: locationId
            }
        });
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// Supervisor dashboard PO search
export async function dashboardPODetailsSearch(idirId: String, locationId: number) {
    try{
        //console.log("Officer search by supervisorID: ", supervisorID);
        const { data } = await axiosClient.get('/dashboards/supervisor/details', {
            params: {
                idirId: idirId,
                locationId: locationId
            }
        });
        return [null, data];
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// PO dashboard search
export async function dashboardPOSearch(userId: String, locationId: number) {
    try{
        if (userId) {
            const { data } = await axiosClient.get('/dashboards/po', {
                params: {
                    idirId: userId,
                    locationId: locationId
                }
            });
            return [null, data];
        } else {
            throw "Cannot perform PO dashboard search, the PO's idirId isn't synced up in the database";
        }
    } catch (error) {
        handleError(error);
        return [error];
    }
}

// Get list of PO for a given location, used in PO dashboard
export async function getPOList() {
    try {
        const {data} = await axiosClient.get('/user/PO');
        return [null,data];
    }catch (error) {
        handleError(error);
        return [error];
    }
}

// function to search client profile
export async function clientProfileSearch(clientNum: String) {
    try{
        //console.log("ClientProfileSearch clientNum", clientNum);
        const { data } = await axiosClient.get(`/clients/${clientNum}`);
        //console.log("ClientProfileSearch: ", data);
        return [null, data];
    } catch (error) {
        handleError(error);
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
        handleError(error);
        return [error];
    }
}

export async function getChartData(payload: Object) {
    try{
        const { data } = await axiosClient.post('/trend/client/data', payload);
        return [null, data];
    } catch (error) {
        handleError(error);
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
        handleError(error);
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
        handleError(error);
        return [error];
    }
}

export async function searchClientInterventions( payload: Object) {
    try {
        const { data} = await axiosClient.post('/forms/client/interventions', payload);
        return [null,data];
    }catch (error) {
        handleError(error);
        return [error];
    }
}

export async function searchClientResponsivities( payload: Object) {
    try {
        const { data} = await axiosClient.post('/forms/client/responsivities', payload);
        return [null,data];
    }catch (error) {
        handleError(error);
        return [error];
    }
}



export async function searchClientComments( payload: Object) {
    try {
        const { data} = await axiosClient.post('/forms/client/comments', payload);
        return [null,data];
    }catch (error) {
        handleError(error);
        return [error];
    }
}


//-------------------------------------
// API Error handling
//-------------------------------------
export default axiosClient;

function handleError(error: object) {
    console.log("error: ", error);
    if (error instanceof AxiosError) {
        if (error.response && error.response.data) {
            iZtoast.error({
              title: 'Error',
              message: error.response.data.errorMessage || error.message,
              position: 'center'
            });
        } else {
            iZtoast.error({
                title: 'Error',
                message: error.message,
                position: 'center'
            });
        }
    } else {
        iZtoast.error({
            title: 'Error',
            message: error,
            position: 'center'
        });
    }
}
