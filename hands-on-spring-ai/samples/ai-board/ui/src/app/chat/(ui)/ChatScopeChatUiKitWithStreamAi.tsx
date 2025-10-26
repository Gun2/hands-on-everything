'use client';
import React, { useCallback, useState } from 'react';

import '@chatscope/chat-ui-kit-styles/dist/default/styles.min.css';
import {
  ChatContainer,
  MainContainer,
  Message,
  MessageInput,
  MessageList,
  TypingIndicator,
} from '@chatscope/chat-ui-kit-react';
import { createUUID } from '@/utils/createUUID';

type ChatMessage = {
  uuid: string;
  message: string;
  sentTime: Date;
  mine: boolean;
}

const ChatScopeChatUiKitWithStreamAi = () => {
  const [waitingAnswer, setWaitingAnswer] = useState(false);

  const [messageList, setMessageList] = useState<ChatMessage[]>([]);
  //uuid가 존재하면 수정, 없으면 생성하여 추가
  const updateOrPushMessage = useCallback((message : ChatMessage) => {
    setMessageList(prev => {
      const exist = prev.some(v => v.uuid === message.uuid);
      if (exist) {
        return prev.map(v => v.uuid === message.uuid ? ({
          ...v,
          ...message,
          sentTime: v.sentTime,
        }) : v);
      }
      return [...prev, message];
    })
  }, []);
  const chat = useCallback(async (message : string) => {
    setWaitingAnswer(true);
    setMessageList(prevState => [...prevState, {
      uuid: createUUID(),
      message: message,
      mine: true,
      sentTime: new Date(),
    }])
    const response = await fetch(`${process.env.NEXT_PUBLIC_CHAT_API_URL}/stream-chat-ai`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        content: message,
      })
    })

    // Response.body는 ReadableStream
    const reader = response?.body?.getReader?.();
    const decoder = new TextDecoder();
    const uuid = createUUID();
    let answerMessage : ChatMessage = {
      uuid,
      message: "",
      mine: false,
      sentTime: new Date(),
    }
    while (reader) {
      const { done, value } = await reader.read();
      if (done) {
        setWaitingAnswer(false);
        break;
      };
      const chunk = decoder.decode(value);
      answerMessage = {
        ...answerMessage,
        message: answerMessage.message + chunk
      };
      updateOrPushMessage(answerMessage)
    }
  }, []);
  return (
    <div style={{ position:"relative", height: "500px" }}>
      <MainContainer>
        <ChatContainer>
          <MessageList
            typingIndicator={waitingAnswer ? <TypingIndicator content={"Please wait.."} /> : undefined}
          >
            {
              messageList.map((message, index) => (
                <Message key={index} model={{
                  position: "normal",
                  direction: message.mine ? "outgoing" : "incoming",
                  message: message.message,
                  sentTime: message.sentTime.toISOString(),
                  sender: message.mine ? "Me" : "Ai"
                }} />
              ))
            }
          </MessageList>
          <MessageInput
            placeholder="Type message here"
            disabled={waitingAnswer}
            onSend={(innerHtml, textContent, innerText, nodes) => {
              chat(textContent);
            }}
          />
        </ChatContainer>
      </MainContainer>
    </div>
  );
};

export default ChatScopeChatUiKitWithStreamAi;