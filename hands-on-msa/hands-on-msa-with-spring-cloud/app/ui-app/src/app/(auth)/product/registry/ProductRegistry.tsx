'use client';
import React, { ChangeEvent, useCallback, useState } from 'react';
import PageTemplate from '@/components/PageTemplate';
import { ProductCreateRequest } from '@/lib/api/product/product.types';
import { productService } from '@/lib/api/product/productService';
import { useRouter } from 'next/navigation';

const ProductRegistry = (
  {
  }
) => {
  const router = useRouter();
  const [createData, setCreateData] = useState<ProductCreateRequest>({
    name: "",
    price: 0,
  });
  const onChange = useCallback(({target}: ChangeEvent<HTMLInputElement>) => {
    setCreateData(prev => ({
      ...prev,
      [target.name]: target.value,
    }))
  }, []);
  const save = useCallback(() => {
    productService.create(createData).then((result) => {
      if (result.status == 201) {
        router.push(`/product`);
      }
    })
  }, [createData, router]);
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
                  value={createData.name}
                />
              </td>
            </tr>
            <tr>
              <th>가격</th>
              <input
                name="price"
                onChange={onChange}
                value={createData.price}
              />
            </tr>
          </tbody>
        </table>
      )}
    />
  );
};

export default ProductRegistry;