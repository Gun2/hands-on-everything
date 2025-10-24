import axios from 'axios';

const instance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BOARD_API_URL,
});

export default instance;
