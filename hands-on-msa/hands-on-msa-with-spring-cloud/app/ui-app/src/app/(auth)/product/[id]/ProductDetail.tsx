'use client';
import React, { useCallback } from 'react';
import PageTemplate from '@/components/PageTemplate';
import { Product } from '@/lib/api/product/product.types';
import { useRouter } from 'next/navigation';
import { productService } from '@/lib/api/product/productService';

type ProductDetailProps = {
  product: Product;
}
const ProductDetail = (
  {
    product,
  }: ProductDetailProps
) => {
  const router = useRouter();
  const deleteProduct = useCallback(() => {
    productService.delete(product.id).then((result) => {
      if (result.status === 200) {
        router.push('/product');
      }
    })
  }, [product.id]);
  return (
    <PageTemplate
      title={"상품 정보"}
      buttonArea={(
        <div style={{ display: "flex", gap: 5 }}>
          <button
            onClick={() => router.push('/product')}
          >목록</button>
          <button
            onClick={() => router.push(`/product/edit/${product.id}`)}
          >수정</button>
          <button
            onClick={deleteProduct}
          >삭제</button>
        </div>
      )}
      contentArea={(
        <table>
          <tbody>
            <tr>
              <th>상품명</th>
              <td>{product.name}</td>
            </tr>
            <tr>
              <th>가격</th>
              <td>{product.price}</td>
            </tr>
            <tr>
              <th>등록일시</th>
              <td>{product.createdAt}</td>
            </tr>
            <tr>
              <th>수정일시</th>
              <td>{product.updatedAt}</td>
            </tr>
          </tbody>
        </table>
      )}
    />
  );
};

export default ProductDetail;