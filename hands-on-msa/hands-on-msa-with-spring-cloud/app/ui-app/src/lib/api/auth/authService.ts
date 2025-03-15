import { LoginRequest, TokenResponse } from '@/types/auth.types';
import axios, { AxiosResponse } from 'axios';

const axiosInstance = axios.create({
  baseURL: process.env.API_BASE_URL,
});

export const authService = {
  login: async (data: LoginRequest): Promise<AxiosResponse<TokenResponse>> => {
    return axiosInstance.post("/auth/login", {
      ...data
    });
  },
  logout: async (accessToken: TokenResponse["accessToken"]) => {
    return axiosInstance.post("/auth/logout", null, {
      headers : {
        Authorization: `Bearer ${accessToken}`
      }
    });
  }
}
