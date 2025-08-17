import { useSubscription } from 'react-stomp-hooks';


export type UseCurrentTimeSubscriptionArgs = {
  subscribe: (time: string) => void;
}
export const useCurrentTimeSubscription = (
  {
    subscribe,
  }: UseCurrentTimeSubscriptionArgs
) => {

  useSubscription("/topic/time", (message) => subscribe(message.body));
}