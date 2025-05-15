'use client'
import React, { useState } from 'react';
import { StompSessionProvider, useSubscription } from 'react-stomp-hooks';

const CurrentTime = () => {

  return (
    <StompSessionProvider
      url={"/service/ws"}
      //All options supported by @stomp/stompjs can be used here
    >
      <SubscribingComponent/>
    </StompSessionProvider>
  );
};

const SubscribingComponent = () => {
  const [time, setTime] = useState<string>();
  useSubscription("/topic/time", (message) => setTime(message.body));

  return (
    <div>{time}</div>
  )
}

export default CurrentTime;