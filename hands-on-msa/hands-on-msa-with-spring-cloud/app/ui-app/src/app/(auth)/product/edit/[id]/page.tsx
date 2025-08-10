import React from 'react';
import { productService } from '@/lib/api/product/productService';
import ProductEdit from '@/app/(auth)/product/edit/[id]/ProductEdit';

const Page = async (
  {
    params: { id }
  }: {
    params: {
      id: number;
    }
  }
) => {
  const productAxiosResponse = await productService.getById(id);

  return (
    <ProductEdit product={productAxiosResponse.data}/>
  );
};

export default Page;