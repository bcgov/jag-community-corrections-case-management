import axios from 'axios';
//import { config } from 'process';

const axiosClient = axios.create({
    baseURL: config.VUE_APP_CCCM_API_ENDPOINT,
    //baseURL: "http://localhost:8080"
});

// function to async fetch the location info
export async function async_getLocationInfo() {
    try {
        //console.log("VUE_APP_CCCM_API_ENDPOINT: ", config.VUE_APP_CCCM_API_ENDPOINT);
        const { data } = await axiosClient.get('/locations');
        return [null, data];
    }catch (error) {
        return [error];
    }
}

// function to sync fetch the location info
export function getLocationInfo() {
    try {
        //console.log("VUE_APP_CCCM_API_ENDPOINT: ", config.VUE_APP_CCCM_API_ENDPOINT);
        const data = axiosClient.get('/locations');
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

// function to search client which is used by PO client search and client search
export async function clientSearch(address: String, age: number, birthYear: number, clientNum: String, 
    gender: String, location: String, name: String, officer: String, soundex: boolean) {
    try{
        //console.log("ClientSearch payload");
        const { data } = await axiosClient.get('/clients', {
            params: {
                address: address,
                age: age,
                birthYear: birthYear,
                clientNum: clientNum,
                gender: gender,
                location: location,
                name: name,
                officer: officer,
                soundex: soundex
            }
        });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// Officer search used by supervisor dashboard
export async function officerSearch(supervisorID: String) {
    try{
        //console.log("Officer search by supervisorID: ", supervisorID);
        const { data } = await axiosClient.get(`/dashboard/supervisor/${supervisorID}`);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to get client photo
export async function photoSearch(clientID: String) {
    try{
        //console.log("Photo search by clientID: ", clientID);
        const { data } = await axiosClient.get(`/clients/${clientID}/photo`);
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
