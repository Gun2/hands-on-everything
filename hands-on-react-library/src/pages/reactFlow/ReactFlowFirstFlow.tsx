import React from 'react';
import { Edge, Node, ReactFlow } from '@xyflow/react';
import '@xyflow/react/dist/style.css';
import ReactFlowBox from './ReactFlowBox';

const initialNodes : Node[]  = [
  { id: '1', position: { x: 0, y: 0 }, data: { label: '1' } },
  { id: '2', position: { x: 0, y: 100 }, data: { label: '2' } },
];
const initialEdges : Edge[] = [{ id: 'e1-2', source: '1', target: '2' }];

/**
 * first flow 샘플
 * https://reactflow.dev/learn#creating-your-first-flow
 * @constructor
 */
const ReactFlowFirstFlow = () => {
  return (
    <ReactFlowBox>
      <ReactFlow nodes={initialNodes} edges={initialEdges} />
    </ReactFlowBox>
  );
};

export default ReactFlowFirstFlow;