import { getAxiosInstance } from '@/lib/api/axiosInstance';
import { AxiosResponse } from 'axios';
import { Product, ProductCreateRequest, ProductUpdateRequest } from '@/lib/api/product/product.types';

const axiosInstance = getAxiosInstance();

export const productService = {
  getAll: async (): Promise<AxiosResponse<Product[]>> => {
    return axiosInstance.get(`/products`);
  },
  getById: async (id: Product["id"]): Promise<AxiosResponse<Product>> => {
    return axiosInstance.get(`/products/${id}`);
  },
  create: async (data: ProductCreateRequest): Promise<AxiosResponse<Product>> => {
    return axiosInstance.post(`/products`, data);
  },
  update: async (id : Product["id"], data: ProductUpdateRequest): Promise<AxiosResponse<Product>> => {
    return axiosInstance.put(`/products/${id}`, data);
  },
  delete: async (id: Product["id"]): Promise<AxiosResponse<void>> => {
    return axiosInstance.delete(`/products/${id}`);
  },
}
