import React from 'react';
import { Stack } from '@mui/material';
import TextareaAutocompleteSample from './TextareaAutocompleteSample';
import AccordionForm from '../components/AccordionForm/AccordionForm';

const TextareaAutocompletePage = () => {
  return (
    <Stack>
      <Sample/>
    </Stack>
  );
};

const Sample = () => (
  <AccordionForm

     summary={<h1>sample</h1>}
     details={<TextareaAutocompleteSample/>}
  />
)

export default TextareaAutocompletePage;