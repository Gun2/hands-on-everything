import React from 'react';
import { Stack } from '@mui/material';
import AccordionForm from '../../components/AccordionForm/AccordionForm';
import ReactFlowFirstFlow from './ReactFlowFirstFlow';
import ReactFlowAddingInteractivity from './ReactFlowAddingInteractivity';
import ReactFlowControlled from './ReactFlowControlled';
import ReactFlowUncontrolled from './ReactFlowUncontrolled';
import ReactFlowConnectionNode from './ReactFlowConnectionNode';
import ReactFlowCustomNodes from './ReactFlowCustomNodes';
import ReactFlowCustomEdges from './ReactFlowCustomEdges';
import ReactFlowUtilityClasses from './ReactFlowUtilityClasses';
import ReactFlowLayoutingWithDagre from './ReactFlowLayoutingWithDagre';
import ReactFlowLayoutingD3Hierarchy from './ReactFlowLayoutingD3Hierarchy';

const ReactFlowPage = () => {
  return (
    <Stack>
      <FirstAccordion/>
      <AddInteractivityAccordion/>
      <ControlledAccordion/>
      <UncontrolledAccordion/>
      <ConnectionNodeAccordion/>
      <CustomNodeAccordion/>
      <CustomEdgeAccordion/>
      <UtilityClassesAccordion/>
      <LayoutingWithDagreAccordion/>
      <LayoutingWithD3HierarchyAccordion/>
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

const CustomEdgeAccordion = () => (
  <AccordionForm
    summary={"Custom Edge"}
    details={<ReactFlowCustomEdges/>}
  />
)

const UtilityClassesAccordion = () => (
  <AccordionForm
    summary={"Utility Classes Edge"}
    details={<ReactFlowUtilityClasses/>}
  />
)

const LayoutingWithDagreAccordion = () => (
  <AccordionForm
    summary={"Layouting With Dagre"}
    details={<ReactFlowLayoutingWithDagre/>}
  />
)

const LayoutingWithD3HierarchyAccordion = () => (
  <AccordionForm
    summary={"Layouting With D3Hierarchy"}
    details={<ReactFlowLayoutingD3Hierarchy/>}
  />
)


export default ReactFlowPage;