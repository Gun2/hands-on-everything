import { LoginRequest, LoginSuccessResponse, TokenResponse } from '@/types/auth.types';
import axios, { AxiosResponse } from 'axios';

const axiosInstance = axios.create({
  baseURL: process.env.API_BASE_URL,
});

export const authService = {
  login: async (data: LoginRequest): Promise<AxiosResponse<LoginSuccessResponse>> => {
    return axiosInstance.post("/auth/login", {
      ...data
    });
  },
  logout: async (session: string) => {
    return axiosInstance.post("/auth/logout", null, {
      headers: {
        Cookie: `SESSION=${session}`,
      }
    });
  },
  refresh: async (accessToken: TokenResponse["accessToken"], refreshToken: TokenResponse["refreshToken"]) => {
    return axiosInstance.post("/auth/refresh", {
      refreshToken: refreshToken,
    }, {
      headers : {
        Authorization: `Bearer ${accessToken}`
      }
    });
  }
}
