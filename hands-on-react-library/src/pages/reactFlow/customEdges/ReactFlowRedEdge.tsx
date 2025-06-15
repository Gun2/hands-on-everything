import React from 'react';
import { BaseEdge, Edge, EdgeProps, getStraightPath } from '@xyflow/react';


const ReactFlowRedEdge = (
  {
    id,
    sourceX,
    sourceY,
    targetX,
    targetY
  }: EdgeProps
) => {
  const [edgePath] = getStraightPath({
    sourceX,
    sourceY,
    targetX,
    targetY,
  });

  return <BaseEdge
    id={id}
    path={edgePath}
    style={{
      stroke: "#f00"
    }}
  />;

};

export default ReactFlowRedEdge;