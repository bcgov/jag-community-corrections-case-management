import axios from 'axios';
//import { config } from 'process';

const axiosClient = axios.create({
    baseURL: config.VUE_APP_CCCM_API_ENDPOINT,
    // baseURL: "http://localhost:8080"
});

// function to fetch the location info
export async function getLocationInfo() {
    try {
        //console.log("VUE_APP_CCCM_API_ENDPOINT: ", config.VUE_APP_CCCM_API_ENDPOINT);
        const { data } = await axiosClient.get('/locations');
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

// function to save form data
export async function saveFormData(formData: object) {
    try{
        console.log("SaveData payload", formData);
        const { data } = await axiosClient.post('/forms', formData);
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to search client
export async function clientSearch(formData: object) {
    try{
        //console.log("ClientSearch payload", formData);
        const { data } = await axiosClient.get('/clients');
        return [null, data];
    } catch (error) {
        return [error];
    }
}

export default axiosClient;
