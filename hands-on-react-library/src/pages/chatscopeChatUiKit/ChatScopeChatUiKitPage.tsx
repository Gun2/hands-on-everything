import React from 'react';
import { Stack } from '@mui/material';
import AccordionForm from '../../components/AccordionForm/AccordionForm';
import ChatScopeChatUiKitStandardSample from './ChatScopeChatUiKitStandardSample';
import ChatScopeChatUiKitCommunication from './ChatScopeChatUiKitCommunication';
import ChatScopeChatUiKitWithAi from './ChatScopeChatUiKitWithAi';
import ChatScopeChatUiKitWithStreamAi from './ChatScopeChatUiKitWithStreamAi';

const ChatScopeChatUiKitPage = () => {
  return (
    <Stack>
      <SampleAccordion/>
      <CommunicationAccordion/>
      <AiAccordion/>
      <StreamAiAccordion/>
    </Stack>
  );
};

const SampleAccordion = () => (
  <AccordionForm
    summary={<h1>sample</h1>}
    details={<ChatScopeChatUiKitStandardSample/>}
  />
);

const CommunicationAccordion = () => (
  <AccordionForm
    summary={<h1>communication</h1>}
    details={<ChatScopeChatUiKitCommunication/>}
  />
)

const AiAccordion = () => (
  <AccordionForm
    summary={(
      <div>
        <h1>ai</h1>
        <div>(ai-chat-server 구동이 필요한 샘플입니다.)</div>
      </div>
    )}
    details={<ChatScopeChatUiKitWithAi/>}
  />
)

const StreamAiAccordion = () => (
  <AccordionForm
    summary={(
      <div>
        <h1>stream ai</h1>
        <div>(ai-chat-stream-server 구동이 필요한 샘플입니다.)</div>
      </div>
    )}
    details={<ChatScopeChatUiKitWithStreamAi/>}
  />
)

export default ChatScopeChatUiKitPage;