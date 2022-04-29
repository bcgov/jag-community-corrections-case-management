import axios from 'axios';

const axiosClient = axios.create({
    baseURL: 'https://hcydsplculhonzk.form.io',
});

export async function getFormJSON() {
    try {
        const { data } = await axiosClient.get('/demoform1');
        return [null, data];
    }catch (error) {
        return [error];
    }
}

