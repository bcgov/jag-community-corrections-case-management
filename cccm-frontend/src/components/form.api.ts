import axios from 'axios';

const axiosClient = axios.create({
    baseURL: 'http://localhost:8080',
});

// function to fetch the location info
export async function getLocationInfo() {
    try {
        const { data } = await axiosClient.get('/forms/1');
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

// function to fetch sideCard details
export async function getSideCardsDetails() {
    try {
        const { data } = await axiosClient.get('/sideCards');
        return [null, data];
    }catch (error) {
        return [error];
    }
}

// function to fetch form navidation details
export async function getNavigationDetails() {
    try {
        const { data } = await axiosClient.get('/navigation');
        return [null, data];
    }catch (error) {
        return [error];
    }
}