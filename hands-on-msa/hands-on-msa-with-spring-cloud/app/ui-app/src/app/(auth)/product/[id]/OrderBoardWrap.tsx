import React, { useCallback, useEffect, useState } from 'react';
import { Product } from '@/lib/api/product/product.types';
import { Order } from '@/lib/api/order/order.types';
import { orderService } from '@/lib/api/order/orderService';
import OrderBoard from '@/app/(auth)/product/[id]/OrderBoard';
import { useProductOrderSubscription } from '@/lib/subscriptions/useChangeOrderProductSubscription';

type OrderBoardWrapProp = {
  //조회할 주문의 상품 id
  productId: Product["id"]
}
const OrderBoardWrap = (
  {
    productId
  }: OrderBoardWrapProp
) => {
  const [orderList, setOrderList] = useState<Order[] | undefined>()
  const reflash = useCallback(() => {
    orderService.getAll({
      productId: productId
    }).then((data) => {
      setOrderList(data.data)
    })
  }, [productId]);
  useProductOrderSubscription({
    productId: productId,
    subscribe: () => {
      reflash();
    }
  });
  useEffect(() => {
    reflash();
  }, [])
  return (
      orderList ? (
        <OrderBoard orders={orderList}/>
      ) : (
        <div>loading</div>
      )
  );
};

export default OrderBoardWrap;