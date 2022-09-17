import { identifier } from '@babel/types';
import axios from 'axios';
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

// function to fetch the form details
export async function getFormDetails(formId: number) {
    try {
        const { data } = await axiosClient.get(`/forms/${formId}`);
        return [null, data];
    }catch (error) {
        return [error];
    }
}

// function to clone form
export async function cloneForm(formId: number) {
    try {
        const { data } = await axiosClient.post(`/forms/${formId}`);
        return [null, data];
    }catch (error) {
        return [error];
    }
}

// function to update form data
export async function updateForm(formData: object) {
    try{
        console.log("Update form payload", formData);
        const { data } = await axiosClient.put('/forms', formData);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to create form
export async function createForm(formData: object) {
    try{
        console.log("Create form payload", formData);
        const { data } = await axiosClient.post('/forms', formData);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to search client based on general info 
export async function clientSearchByGeneralInfo(age: number, birthYear: number, gender: String, 
    givenName: String, identifier: String, identifierType: String, lastName: String,
    limitTolocation: boolean, range: number, soundex: boolean) {
    try{
        //console.log("ClientSearch payload");
        const { data } = await axiosClient.get('/clients', {
            params: {
                age: age,
                birthYear: birthYear,
                gender: gender,
                givenName: givenName,
                identifier: identifier,
                identifierType: identifierType,
                lastName: lastName,
                limitTolocation: limitTolocation,
                range: range,
                soundex: soundex
            }
        });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to search client based on address info 
export async function clientSearchByAddressInfo(address: String, addressType: String, city: String, 
    expired: boolean, limitToLocation: boolean, postalCode: String, province: String) {
    try{
        //console.log("ClientSearch payload");
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

// Supervisor dashboard search
export async function dashboardSupervisorSearch(supervisorID: String) {
    try{
        //console.log("Officer search by supervisorID: ", supervisorID);
        const { data } = await axiosClient.get(`/dashboard/supervisor/${supervisorID}`);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// PO dashboard search
export async function dashboardPOSearch(poID: String) {
    try{
        //console.log("Officer search by supervisorID: ", supervisorID);
        const { data } = await axiosClient.get(`/dashboard/supervisor/${poID}`);
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
        const { data } = await axiosClient.get('/forms/formSearch', {
                params: {
                    clientNum: clientNum,
                    formTypeCd: formType,
                    currentPeriod: supervisionPeriod
                }
            });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

export default axiosClient;
