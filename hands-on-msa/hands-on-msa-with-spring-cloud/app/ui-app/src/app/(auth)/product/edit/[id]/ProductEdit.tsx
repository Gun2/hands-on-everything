'use client';
import React, { ChangeEvent, useCallback, useState } from 'react';
import PageTemplate from '@/components/PageTemplate';
import { Product, ProductUpdateRequest } from '@/lib/api/product/product.types';
import { productService } from '@/lib/api/product/productService';
import { useRouter } from 'next/navigation';

type ProductEditProps = {
  product: Product;
}
const ProductEdit = (
  {
    product,
  }: ProductEditProps
) => {
  const router = useRouter();
  const [editData, setEditData] = useState<ProductUpdateRequest>({
    name: product.name,
    price: product.price,
  });
  const onChange = useCallback(({target}: ChangeEvent<HTMLInputElement>) => {
    setEditData(prev => ({
      ...prev,
      [target.name]: target.value,
    }))
  }, []);
  const save = useCallback(() => {
    productService.update(product.id, editData).then((result) => {
      if (result.status == 200) {
        router.push(`/product/${product.id}`);
      }
    })
  }, [product, editData, router]);
  return (
    <PageTemplate
      title={"상품 정보"}
      buttonArea={(
        <div style={{ display: "flex", gap: 5 }}>
          <button
            onClick={() => router.push('/product')}
          >목록</button>
          <button
            onClick={save}
          >저장</button>
        </div>
      )}
      contentArea={(
        <table>
          <tbody>
            <tr>
              <th>상품명</th>
              <td>
                <input
                  name="name"
                  onChange={onChange}
                  value={editData.name}
                />
              </td>
            </tr>
            <tr>
              <th>가격</th>
              <input
                name="price"
                onChange={onChange}
                value={editData.price}
              />
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

export default ProductEdit;