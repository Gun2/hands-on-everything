import React from 'react';
import { Stack } from '@mui/material';
import AccordionForm from '../../components/AccordionForm/AccordionForm';
import ChatScopeChatUiKitStandardSample from './ChatScopeChatUiKitStandardSample';
import ChatScopeChatUiKitCommunication from './ChatScopeChatUiKitCommunication';

const ChatScopeChatUiKitPage = () => {
  return (
    <Stack>
      <SampleAccordion/>
      <CommunicationAccordion/>
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

export default ChatScopeChatUiKitPage;