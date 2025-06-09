import React from 'react';
import { Stack } from '@mui/material';
import MonacoEditorSample from './MonacoEditorSample';
import AccordionForm from '../components/AccordionForm/AccordionForm';

const MonacoEditorPage = () => {
  return (
    <Stack>
      <SampleAccordion/>
    </Stack>
  );
};

const SampleAccordion = () => (
  <AccordionForm
    summary={<h1>sample</h1>}
    details={<MonacoEditorSample/>}
  />
);

export default MonacoEditorPage;