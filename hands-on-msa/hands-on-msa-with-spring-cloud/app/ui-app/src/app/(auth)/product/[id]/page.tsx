import React from 'react';
import PageTemplate from '@/components/PageTemplate';
import ProductDetail from '@/app/(auth)/product/[id]/ProductDetail';
import { productService } from '@/lib/api/product/productService';

const Page = async (
  {
    params: {
      id
    }
  }: {
    params: {
      id : number
    }
  }
) => {
  const productAxiosResponse = await productService.getById(id);
  return (
    <ProductDetail product={productAxiosResponse.data}/>
  );
};

export default Page;