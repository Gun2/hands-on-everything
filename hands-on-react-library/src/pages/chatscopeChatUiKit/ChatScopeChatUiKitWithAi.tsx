import React, { useCallback, useEffect, useState } from 'react';

import '@chatscope/chat-ui-kit-styles/dist/default/styles.min.css';
import {
  ChatContainer,
  MainContainer,
  Message,
  MessageInput,
  MessageList,
  TypingIndicator,
} from '@chatscope/chat-ui-kit-react';
import { chatAiApi } from './apis/chatAiApi';

type ChatMessage = {
  message: string;
  sentTime: Date;
  mine: boolean;
}

const ChatScopeChatUiKitWithAi = () => {
  const [waitingAnswer, setWaitingAnswer] = useState(false);

  const [messageList, setMessageList] = useState<ChatMessage[]>([]);
  const chat = useCallback((message : string) => {
    setWaitingAnswer(true);
    setMessageList(prevState => [...prevState, {
      message: message,
      mine: true,
      sentTime: new Date(),
    }])
    chatAiApi.chat({
      content: message,
    }).then(response => {
      setWaitingAnswer(false);
      setMessageList(prevState => [...prevState, {
        message: response.answer,
        mine: false,
        sentTime: new Date(),
      }])
    })
  }, []);
  return (
    <div style={{ position:"relative", height: "500px" }}>
      <MainContainer>
        <ChatContainer>
          <MessageList
            typingIndicator={waitingAnswer ? <TypingIndicator content={"Please wait.."} /> : undefined}
          >
            {
              messageList.map(message => (
                <Message model={{
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

export default ChatScopeChatUiKitWithAi;