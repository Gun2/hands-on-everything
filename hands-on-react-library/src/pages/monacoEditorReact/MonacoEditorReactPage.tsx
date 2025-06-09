import React from 'react';
import { Stack } from '@mui/material';
import MonacoEditorReactSample from './MonacoEditorReactSample';
import AccordionForm from '../../components/AccordionForm/AccordionForm';

const MonacoEditorReactPage = () => {
  return (
    <Stack>
      <SampleAccordion/>
    </Stack>
  );
};

const SampleAccordion = () => (
  <AccordionForm
    summary={<h1>sample</h1>}
    details={<MonacoEditorReactSample/>}
  />
);

export default MonacoEditorReactPage;