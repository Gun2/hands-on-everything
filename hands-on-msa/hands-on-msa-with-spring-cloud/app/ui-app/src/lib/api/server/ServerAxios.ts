"use server"
import "server-only";
import axios from "axios";
import {auth, signOut} from "../../../../auth";
import {redirect} from "next/navigation";

const api = axios.create({
    baseURL : process.env.SERVER_URL
});


api.interceptors.request.use(async value => {
    let session = await auth();
    value.headers.set({
        Authorization: `Bearer ${session?.user?.accessToken}`,
        'Cache-Control': 'no-store',
        Pragma: 'no-store',
        Expires: '0',
    });
    return value;
});

api.interceptors.response.use(async value => {
    return value;
}, async ({response}) => {
    if(response?.status === 401){
        redirect("/api/logout");
    }
    return response;
})

export default api;
