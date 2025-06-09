import React from 'react';
import { Accordion, AccordionDetails, AccordionSummary } from '@mui/material';

export type AccordionFormProps = {
  summary: React.ReactNode;
  details: React.ReactNode;
}
const AccordionForm = (
  {
    summary,
    details
  }: AccordionFormProps
) => {
  return (
    <Accordion>
      <AccordionSummary
        expandIcon={"▼"}
      >
        {summary}
      </AccordionSummary>
      <AccordionDetails>
        {details}
      </AccordionDetails>
    </Accordion>
  );
};

export default AccordionForm;