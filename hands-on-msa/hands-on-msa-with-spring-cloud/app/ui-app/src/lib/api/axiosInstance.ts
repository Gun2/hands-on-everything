import axios from 'axios';

// 클라이언트용 Axios 인스턴스
const clientAxios = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL,
  withCredentials: true,
});

// 서버용 Axios 인스턴스
const serverAxios = axios.create({
  baseURL: process.env.API_BASE_URL,
});

// 현재 환경에 맞는 Axios 선택
export const getAxiosInstance = () => {
  if (typeof window === 'undefined') {
    return serverAxios;
  } else {
    return clientAxios;
  }
};
