import React from 'react';
import { Stack } from '@mui/material';
import AccordionForm from '../../components/AccordionForm/AccordionForm';
import ReactFlowFirstFlow from './ReactFlowFirstFlow';
import ReactFlowAddingInteractivity from './ReactFlowAddingInteractivity';
import ReactFlowControlled from './ReactFlowControlled';
import ReactFlowUncontrolled from './ReactFlowUncontrolled';
import ReactFlowConnectionNode from './ReactFlowConnectionNode';
import ReactFlowCustomNodes from './ReactFlowCustomNodes';

const ReactFlowPage = () => {
  return (
    <Stack>
      <FirstAccordion/>
      <AddInteractivityAccordion/>
      <ControlledAccordion/>
      <UncontrolledAccordion/>
      <ConnectionNodeAccordion/>
      <CustomNodeAccordion/>
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

const ControlledAccordion = () => (
  <AccordionForm
    summary={"Controlled"}
    details={<ReactFlowControlled/>}
  />
)

const UncontrolledAccordion = () => (
  <AccordionForm
    summary={"Uncontrolled"}
    details={<ReactFlowUncontrolled/>}
  />
)

const ConnectionNodeAccordion = () => (
  <AccordionForm
    summary={"Connection Node"}
    details={<ReactFlowConnectionNode/>}
  />
)

const CustomNodeAccordion = () => (
  <AccordionForm
    summary={"Custom Node"}
    details={<ReactFlowCustomNodes/>}
  />
)
export default ReactFlowPage;