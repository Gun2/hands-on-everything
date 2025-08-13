import { Product } from '@/lib/api/product/product.types';

export type Order = {
  id: number;
  productId: Product["id"];
  amount: number;
  totalPrice: number;
  createdAt: string;
  updatedAt: string;
}

export type OrderGetParams = {
  productId?: Product["id"];
}

export type OrderCreateRequest = {
  productId: Product["id"];
  amount: number;
}

export type OrderUpdateRequest = OrderCreateRequest;