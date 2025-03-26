import axios from "axios";
import {showAlert} from "@/js/custom-alert.js";
import {ref} from "vue";

export const user = ref({
    name : '',
    surname : '',
    role : ''
})

export const getUserInfo = async () => {
    try {
        const response = await axios.get("/api/user/info")
        user.value = response.data
    } catch (error) {
        showAlert(error.response.data)
    }
}