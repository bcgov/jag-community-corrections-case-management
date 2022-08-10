import axios from 'axios';
//import { config } from 'process';

const axiosClient = axios.create({
    baseURL: config.VUE_APP_CCCM_API_ENDPOINT,
    //baseURL: "http://localhost:8080"
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

// function to search clients assigned to the given probation office ID
export async function clientSearchByPO(POID: String) {
    try{
        //console.log("ClientSearch by PO id: ", POID);
        const { data } = await axiosClient.get('/clients/poid', {
            params: {
                poid: POID
            }
        });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to search client profile
export async function clientProfileSearch(clientID: number) {
    try{
        //console.log("ClientProfileSearch clientID", clientID);
        const { data } = await axiosClient.get('/clients', {
                params: {
                    clientID: clientID
                }
            });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

// function to search for RNA list
export async function formSearch(clientID: number, formType: string, supervisionPeriod: boolean) {
    try{
        console.log("formSearch for RNA List, clientID: {}, formType: {}, supervisionPeriod: {}", clientID, formType, supervisionPeriod);
        const { data } = await axiosClient.get('/formSearch', {
                params: {
                    clientID: clientID,
                    formType: formType,
                    supervisionPeriod: supervisionPeriod
                }
            });
        return [null, data];
    } catch (error) {
        return [error];
    }
}

export default axiosClient;
