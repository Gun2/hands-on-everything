import React from 'react';
import { BaseEdge, Edge, EdgeLabelRenderer, EdgeProps, getStraightPath, useReactFlow } from '@xyflow/react';


const ReactFlowEdgeLabelButton = (
  {
    id,
    sourceX,
    sourceY,
    targetX,
    targetY
  }: EdgeProps
) => {
  const { deleteElements } = useReactFlow();
  const [edgePath,labelX, labelY] = getStraightPath({
    sourceX,
    sourceY,
    targetX,
    targetY,
  });

  return (
      <>
        <BaseEdge
          id={id}
          path={edgePath}
        />
        <EdgeLabelRenderer>
          <button
            //label position 정보로 style로 위치 조정
            style={{
              position: 'absolute',
              transform: `translate(-50%, -50%) translate(${labelX}px, ${labelY}px)`,
              pointerEvents: 'all',
            }}
            onClick={() => deleteElements({ edges: [{ id }] })}>
            delete
          </button>
        </EdgeLabelRenderer>
      </>
    )

};

export default ReactFlowEdgeLabelButton;