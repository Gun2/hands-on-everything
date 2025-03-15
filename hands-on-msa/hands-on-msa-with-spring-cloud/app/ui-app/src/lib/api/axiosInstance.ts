import axios from 'axios';
import { auth } from '../../../auth';
import { isServer } from '@/lib/commonUtil';

// 클라이언트용 Axios 인스턴스
const clientAxios = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL,
  withCredentials: true,
});

/*clientAxios.interceptors.request.use( async value => {
  const session = await getSession();
  value.headers["Authorization"] = session?.user?.name;
  return value;
})*/


// 서버용 Axios 인스턴스
const serverAxios = axios.create({
  baseURL: process.env.API_BASE_URL,
});

serverAxios.interceptors.request.use(async value => {
  let session = await auth();
  if (session?.user?.accessToken){
    value.headers.Authorization = `Bearer ${session?.user?.accessToken}`
  }
  return value;
});

// 현재 환경에 맞는 Axios 선택
export const getAxiosInstance = () => {
  if (isServer()) {
    console.log("serveer")
    return serverAxios;
  } else {
    console.log("client")
    return clientAxios;
  }
};
