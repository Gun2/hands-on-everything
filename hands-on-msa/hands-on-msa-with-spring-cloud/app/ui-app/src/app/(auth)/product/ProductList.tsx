'use client';
import React, { useCallback, useEffect, useState } from 'react';
import { Product } from '@/lib/api/product/product.types';
import { productService } from '@/lib/api/product/productService';
import Link from 'next/link';
import PageTemplate from '@/components/PageTemplate';
import { useRouter } from 'next/navigation';

const ProductList = () => {
  const [products, setProducts] = useState<Product[] | undefined>();
  const router = useRouter();

  const refresh = useCallback(() => {
    productService.getAll().then(response => {
      setProducts(response.data);
    })
  }, []);

  const goRegistry = useCallback(() => {
    router.push("/product/registry")
  }, [router]);

  useEffect(() => {
    refresh();
  }, [refresh]);

  return (
    <PageTemplate
       title={"상품 리스트"}
       buttonArea={(
         <div style={{
           display: 'flex',
           gap: '5px',
         }}>
           <button onClick={refresh}>새로고침</button>
           <button onClick={goRegistry}>등록</button>
         </div>
       )}
       contentArea={(
         <table>
           <thead>
           <tr>
             <th>상품명</th>
             <th>가격</th>
             <th>등록일시</th>
             <th>수정일시</th>
           </tr>
           </thead>
           <tbody>
           {
             products ? (
               products.map((product: Product) => (
                 <tr key={product.id}>
                   <Link href={`/product/${product.id}`}>{product.name}</Link>
                   <td>{product.price}원</td>
                   <td>{product.createdAt}</td>
                   <td>{product.updatedAt}</td>
                 </tr>
               ))
             ) : (
               <tr>
                 <td colSpan={4}>loading</td>
               </tr>
             )
           }

           </tbody>
         </table>
       )}
    />
  );
};

export default ProductList;