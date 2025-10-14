import React from 'react';
import { Stack } from '@mui/material';
import AccordionForm from '../../components/AccordionForm/AccordionForm';
import ChatScopeChatUiKitStandardSample from './ChatScopeChatUiKitStandardSample';
const ChatScopeChatUiKitPage = () => {
  return (
    <Stack>
      <SampleAccordion/>
    </Stack>
  );
};

const SampleAccordion = () => (
  <AccordionForm
    summary={<h1>sample</h1>}
    details={<ChatScopeChatUiKitStandardSample/>}
  />
);

export default ChatScopeChatUiKitPage;