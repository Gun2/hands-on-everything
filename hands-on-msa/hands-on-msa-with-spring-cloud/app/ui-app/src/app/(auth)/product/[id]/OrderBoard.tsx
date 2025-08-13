import React from 'react';
import { Order } from '@/lib/api/order/order.types';

type OrderBoardProp = {
  orders: Order[];
}
const OrderBoard = (
  {
    orders
  }: OrderBoardProp
) => {
  return (
    <table>
      <thead>
        <tr>
            <th>주문 번호</th>
            <th>수량</th>
            <th>총 금액</th>
            <th>주문 일시</th>
        </tr>
      </thead>
      <tbody>
      {orders.map((order) => (
        <tr key={order.id}>
          <td>{order.id}</td>
          <td>{order.amount}</td>
          <td>{order.totalPrice}</td>
          <td>{order.createdAt}</td>
        </tr>
      ))}
      </tbody>
    </table>
  );
};

export default OrderBoard;