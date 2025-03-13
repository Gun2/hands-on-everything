import { getAxiosInstance } from '@/lib/api/axiosInstance';
import { LoginRequest, TokenResponse } from '@/types/auth.types';
import { AxiosResponse } from 'axios';

const axiosInstance = getAxiosInstance();

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
