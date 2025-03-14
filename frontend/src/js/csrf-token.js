import {ref} from "vue";
import axios from "axios";


export const token = ref();

export const get_token = async () => {
    const response = await axios.get('/app/csrf-token');
    token.value = response.data.csrfToken;

    axios.interceptors.request.use((config) => {
        config.headers['X-CSRF-Token'] = token.value;
        return config;
    });
}




