declare namespace NodeJS {
    interface ProcessEnv {
        //API URL
        REACT_APP_API_URL: string;
        //API 요청 타임아웃
        REACT_APP_API_TIMEOUT?: number;
        //AXIOS 재시도 횟수
        REACT_APP_AXIOS_RETRY?: number;
    }
}