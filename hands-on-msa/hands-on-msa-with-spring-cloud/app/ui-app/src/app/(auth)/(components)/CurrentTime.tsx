'use client';
import React, { useState } from 'react';
import { useCurrentTimeSubscription } from '@/lib/subscriptions/useCurrentTimeSubscription';

const CurrentTime = () => {

  return (
    <SubscribingComponent/>
  );
};

const SubscribingComponent = () => {
  const [time, setTime] = useState<string>();
  useCurrentTimeSubscription({
    subscribe: time => {
      setTime(time)
    }
  })
  return (
    <div>{time}</div>
  )
}

export default CurrentTime;