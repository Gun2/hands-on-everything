import React from 'react';
import { Stack } from '@mui/material';
import AccordionForm from '../../components/AccordionForm/AccordionForm';
import ReactFlowFirstFlow from './ReactFlowFirstFlow';
import ReactFlowAddingInteractivity from './ReactFlowAddingInteractivity';

const ReactFlowPage = () => {
  return (
    <Stack>
      <FirstAccordion/>
      <AddInteractivityAccordion/>
    </Stack>
  );
};

const FirstAccordion = () => (
  <AccordionForm
    summary={"First Creating"}
    details={<ReactFlowFirstFlow/>}
  />
)

const AddInteractivityAccordion = () => (
  <AccordionForm
    summary={"Add Interactivity"}
    details={<ReactFlowAddingInteractivity/>}
  />
)

export default ReactFlowPage;