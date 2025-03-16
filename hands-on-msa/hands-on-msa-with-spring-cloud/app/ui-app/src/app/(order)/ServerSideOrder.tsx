import React from 'react';
import { orderService } from '@/lib/api/order/orderService';
import { redirect } from 'next/navigation';

const ServerSideOrder = async (
  {
    output
  }: {
    output : string
  }
) => {

  return (
    <div>
      <form action={
        async function() {
          "use server"

          let output = "";
          try {
            const stringAxiosResponse = await orderService.getById("123");
            output = stringAxiosResponse.data;
          }catch (error) {
            console.error(error)
            output = error;
          }
          redirect(`/?output=${encodeURIComponent(output + "")}`);
        }
      }>
        <button>
          get order (server)
        </button>
      </form>
      <div>
        {output}
      </div>
    </div>
  );
};

export default ServerSideOrder;