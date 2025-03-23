import axios from 'axios';
import { isServer } from '@/lib/commonUtil';
import { auth } from '../../../auth';

// 클라이언트용 Axios 인스턴스
const clientAxios = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL,
  withCredentials: true,
});

// 서버용 Axios 인스턴스
const serverAxios = axios.create({
  baseURL: process.env.API_BASE_URL,
});

serverAxios.interceptors.request.use(async value => {
  if (isServer()){
    const session = await auth();
    if (session?.user.session){
      value.headers.set({
        Cookie: `SESSION=${session?.user?.session}`,
      });
    }
  }
  return value;
});

// 현재 환경에 맞는 Axios 선택
export const getAxiosInstance = () => {
  if (isServer()) {
    console.log("server")
    return serverAxios;
  } else {
    console.log("client")
    return clientAxios;
  }
};
