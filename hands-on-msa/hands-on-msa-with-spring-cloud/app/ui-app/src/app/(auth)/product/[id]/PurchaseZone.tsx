import React, { useCallback, useState } from 'react';
import { Product } from '@/lib/api/product/product.types';
import { orderService } from '@/lib/api/order/orderService';

type PurchaseZoneData = Pick<Product, "id" | "price">;

type PurchaseZoneProps = {
  product: PurchaseZoneData;
}
/**
 * 구매 영역
 * @param orders
 * @constructor
 */
const PurchaseZone = (
  {
    product
  }: PurchaseZoneProps
) => {
  const [loading, setLoading] = useState(false);
  const [amount, setAmount] = useState(1);
  const purchase = useCallback(() => {
    setLoading(true);
    orderService.create({
      amount: amount,
      productId: product.id,
    }).then(() => {
      setLoading(false);
    });
  }, [amount, product]);
  return (
    <div>
      <div style={{
        display: 'flex',
        gap: 5
      }}>
        <div>구매 수량</div>
        <input value={amount} type={"number"} onChange={e => setAmount(e.target.value as unknown as number)} />
        <button onClick={purchase} disabled={loading}>구매하기</button>
      </div>
      <div>총 금액 : {amount * product.price}원</div>
    </div>
  );
};

export default PurchaseZone;