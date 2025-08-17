import { IMessage, useSubscription } from 'react-stomp-hooks';
import { ResourceEvent } from '@/lib/subscriptions/types/resourceEvent.types';
import { Order } from '@/lib/api/order/order.types';
import { Product } from '@/lib/api/product/product.types';


export type UseChangeOrderSubscriptionArgs = {
  productId: Product["id"];
  subscribe: (response: ResourceEvent<Order["id"]>) => void;
}
export const useProductOrderSubscription = (
  {
    productId,
    subscribe,
  }: UseChangeOrderSubscriptionArgs
) => {

  useSubscription(`/topic/products/${productId}/orders`, (message: IMessage) => subscribe(JSON.parse(message.body)));
}