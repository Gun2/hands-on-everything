import { getAxiosInstance } from '@/lib/api/axiosInstance';
import { AxiosResponse } from 'axios';
import { Order, OrderCreateRequest, OrderGetParams, OrderUpdateRequest } from '@/lib/api/order/order.types';

const axiosInstance = getAxiosInstance();

export const orderService = {
  getAll: async (prams : OrderGetParams): Promise<AxiosResponse<Order[]>> => {
    return axiosInstance.get(`/orders`, {
      params: prams
    });
  },
  getById: async (id: Order["id"]): Promise<AxiosResponse<Order>> => {
    return axiosInstance.get(`/orders/${id}`);
  },
  create: async (data: OrderCreateRequest): Promise<AxiosResponse<Order>> => {
    return axiosInstance.post(`/orders`, data);
  },
  update: async (id : Order["id"], data: OrderUpdateRequest): Promise<AxiosResponse<Order>> => {
    return axiosInstance.put(`/orders/${id}`, data);
  },
  delete: async (id: Order["id"]): Promise<AxiosResponse<void>> => {
    return axiosInstance.delete(`/orders/${id}`);
  },
}
