import React from 'react';
import { Stack } from '@mui/material';
import MonacoEditorReactSample from './MonacoEditorReactSample';
import AccordionForm from '../../components/AccordionForm/AccordionForm';
import MonacoEditorGetValueFromRef from './MonacoEditorGetValueFromRef';
import MonacoEditorGetValueFromOnChange from './MonacoEditorGetValueFromOnChange';

const MonacoEditorReactPage = () => {
  return (
    <Stack>
      <SampleAccordion/>
      <GatValueFromRefAccordion/>
      <GatValueFromOnChangeAccordion/>
    </Stack>
  );
};

const SampleAccordion = () => (
  <AccordionForm
    summary={<h1>sample</h1>}
    details={<MonacoEditorReactSample/>}
  />
);

const GatValueFromRefAccordion = () => (
  <AccordionForm
    summary={<h1>get value (ref)</h1>}
    details={<MonacoEditorGetValueFromRef/>}
  />
);

const GatValueFromOnChangeAccordion = () => (
  <AccordionForm
    summary={<h1>get value (onChange)</h1>}
    details={<MonacoEditorGetValueFromOnChange/>}
  />
);

export default MonacoEditorReactPage;