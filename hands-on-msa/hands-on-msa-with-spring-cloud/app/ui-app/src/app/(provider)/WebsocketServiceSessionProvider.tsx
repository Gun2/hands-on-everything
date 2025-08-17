"use client";
import React from 'react';
import { StompSessionProvider } from 'react-stomp-hooks';

type WebsocketServiceSessionProviderProps = {
  children: React.ReactNode;
}
/**
 * websocket service의 websocket 세션 연결을 위한 프로바이더
 * @constructor
 */
const WebsocketServiceSessionProvider = (
  {
    children
  }: WebsocketServiceSessionProviderProps
) => {
  return (
    <StompSessionProvider
      url={"/service/ws"}
      //All options supported by @stomp/stompjs can be used here
    >
      {children}
    </StompSessionProvider>
  );
};

export default WebsocketServiceSessionProvider;