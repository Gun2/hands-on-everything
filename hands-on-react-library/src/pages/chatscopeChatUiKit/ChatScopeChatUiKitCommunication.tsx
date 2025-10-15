import React, { useCallback, useEffect, useState } from 'react';

import '@chatscope/chat-ui-kit-styles/dist/default/styles.min.css';
import { ChatContainer, MainContainer, Message, MessageInput, MessageList } from '@chatscope/chat-ui-kit-react';

type ChatMessage = {
  message: string;
  sentTime: Date;
  mine: boolean;
}

const ChatScopeChatUiKitCommunication = () => {
  const [messageList, setMessageList] = useState<ChatMessage[]>([createSampleMessage()]);
  const chat = useCallback((message : string) => {
    setMessageList(prevState => [...prevState, {
      message: message,
      mine: true,
      sentTime: new Date(),
    }])
  }, []);
  useEffect(() => {
    //마지막 채팅이 사용자가 보냈을 경우 샘플 채팅 메시지 추가
    setTimeout(() => {
      const latestChatMessage = messageList?.[messageList.length - 1];
      if (latestChatMessage?.mine === true){
        setMessageList(prev => [...prev, createSampleMessage()]);
      }
    }, 1000);
  }, [messageList])
  return (
    <div style={{ position:"relative", height: "500px" }}>
      <MainContainer>
        <ChatContainer>
          <MessageList>
            {
              messageList.map(message => (
                <Message model={{
                  position: "normal",
                  direction: message.mine ? "outgoing" : "incoming",
                  message: message.message,
                  sentTime: message.sentTime.toISOString(),
                  sender: message.mine ? "Me" : "Other"
                }} />
              ))
            }
          </MessageList>
          <MessageInput
            placeholder="Type message here"
            onSend={(innerHtml, textContent, innerText, nodes) => {
              chat(textContent);
            }}
          />
        </ChatContainer>
      </MainContainer>
    </div>
  );
};

const createSampleMessage = () : ChatMessage => ({
  mine: false,
  message: "Hello my friend",
  sentTime: new Date(),
})

export default ChatScopeChatUiKitCommunication;