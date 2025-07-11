import React from 'react';
import { Edge, Node, Panel, ReactFlow, ReactFlowProvider, useEdgesState, useNodesState } from '@xyflow/react';

import ReactFlowBox from '../../ReactFlowBox';
import { useLayoutedElements } from './hooks/useLayoutedElements';

const ReactFlowLayoutingWithD3Force = () => {
  const [nodes, , onNodesChange] = useNodesState(initialNodes);
  const [edges, , onEdgesChange] = useEdgesState(initialEdges);
  const {initialized, toggle, isRunning, dragEvents} = useLayoutedElements();

  return (
    <ReactFlowBox>
      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodeDragStart={dragEvents.start}
        onNodeDrag={dragEvents.drag}
        onNodeDragStop={dragEvents.stop}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
      >
        <Panel>
          {initialized && (
            <button onClick={toggle}>
              {isRunning?.() ? 'Stop' : 'Start'} force simulation
            </button>
          )}
        </Panel>
      </ReactFlow>
    </ReactFlowBox>
  );
};

export const initialNodes : Node[] = [
  {
    id: '1',
    type: 'input',
    data: { label: 'input' },
    position: { x: 0, y: 0 },
  },
  {
    id: '2',
    data: { label: 'node 2' },
    position: { x: 0, y: 100 },
  },
  {
    id: '2a',
    data: { label: 'node 2a' },
    position: { x: 0, y: 200 },
  },
  {
    id: '2b',
    data: { label: 'node 2b' },
    position: { x: 0, y: 300 },
  },
  {
    id: '2c',
    data: { label: 'node 2c' },
    position: { x: 0, y: 400 },
  },
  {
    id: '2d',
    data: { label: 'node 2d' },
    position: { x: 0, y: 500 },
  },
  {
    id: '3',
    data: { label: 'node 3' },
    position: { x: 200, y: 100 },
  },
];

export const initialEdges: Edge[] = [
  { id: 'e12', source: '1', target: '2', animated: true },
  { id: 'e13', source: '1', target: '3', animated: true },
  { id: 'e22a', source: '2', target: '2a', animated: true },
  { id: 'e22b', source: '2', target: '2b', animated: true },
  { id: 'e22c', source: '2', target: '2c', animated: true },
  { id: 'e2c2d', source: '2c', target: '2d', animated: true },
];



export default function () {
  return (
    <ReactFlowProvider>
      <ReactFlowLayoutingWithD3Force />
    </ReactFlowProvider>
  );
}