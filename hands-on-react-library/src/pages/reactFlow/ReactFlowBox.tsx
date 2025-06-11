import React from 'react';

type ReactFlowBoxProps = {
  children ?: React.ReactNode
}
const ReactFlowBox = (
  {
    children
  }: ReactFlowBoxProps
) => {
  return (
    <div style={{ width: '100%', height: '50vh', border: "1px solid #000" }}>
      {children}
    </div>
  );
};

export default ReactFlowBox;