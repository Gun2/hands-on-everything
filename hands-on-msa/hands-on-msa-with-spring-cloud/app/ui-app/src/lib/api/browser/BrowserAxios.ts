'use client'
import axios from "axios";
import {redirect} from "next/navigation";

const api = axios.create({});

api.interceptors.request.use(async value => {
    return value;
});

api.interceptors.response.use(async value => {
    console.log(value)
    return value;
}, async ({response}) => {
    if(response?.status === 401){
        redirect("/api/logout");
    }
    return response;
})
export default api;
