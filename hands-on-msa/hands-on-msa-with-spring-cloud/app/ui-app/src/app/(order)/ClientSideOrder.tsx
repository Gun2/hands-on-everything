'use client';
import React, { useCallback, useState } from 'react';
import { orderService } from '@/lib/api/order/orderService';

const ClientSideOrder = () => {
  const [result, setResult] = useState<string>('');
  const onClick = useCallback(() => {
    orderService.getById("123").then(value => {
      setResult(value.data);
    }).catch(reason => {
      setResult(reason)
    });
  }, []);
  return (
    <div>
      <div>
        <button onClick={onClick}>
          get order (client)
        </button>
      </div>
      <div>{result + ""}</div>
    </div>
  );
};

export default ClientSideOrder;