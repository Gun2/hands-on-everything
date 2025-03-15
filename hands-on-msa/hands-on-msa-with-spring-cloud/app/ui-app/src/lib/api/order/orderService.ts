import { getAxiosInstance } from '@/lib/api/axiosInstance';
import { AxiosResponse } from 'axios';

const axiosInstance = getAxiosInstance();

export const orderService = {
  getById: async (orderId: string): Promise<AxiosResponse<string>> => {
    return axiosInstance.get(`/orders/${orderId}`);
  },
}
