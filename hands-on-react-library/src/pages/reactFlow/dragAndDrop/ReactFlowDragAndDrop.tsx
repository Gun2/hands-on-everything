import React, { useCallback } from 'react';
import {
  addEdge,
  Background,
  Controls,
  Node,
  ReactFlow,
  ReactFlowProvider,
  useEdgesState,
  useNodesState,
  useReactFlow,
} from '@xyflow/react';

import '@xyflow/react/dist/style.css';

import Sidebar from './Sidebar';
import { DnDProvider, useDnD } from './DnDContext';
import { Connection } from '@xyflow/system/dist/esm/types/general';
import { nodeTypes } from './nodeTypes';
import ReactFlowBox from '../ReactFlowBox';
import { Stack } from '@mui/material';
import { getCustomNodeSizeByType } from './CustomNodes';

const initialNodes : Node[] = [
  {
    id: '1',
    type: 'input',
    data: { label: 'input node' },
    position: { x: 250, y: 5 },
  },
];

let id = 0;
const getId = () => `dndnode_${id++}`;

const DnDFlow = () => {
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const { screenToFlowPosition, getViewport} = useReactFlow();
  const { zoom } = getViewport();

  const { type, setType } = useDnD();

  const onConnect = useCallback((params : Connection) => setEdges((eds) => addEdge(params, eds)), []);

  const onDragOver = useCallback((event: React.DragEvent) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = 'move';
  }, []);

  const onDrop = useCallback(
    (event: React.DragEvent) => {
      event.preventDefault();
      console.log(event)
      console.log(type)
      // check if the dropped element is valid
      if (!type) {
        return;
      }

      // project was renamed to screenToFlowPosition
      // and you don't need to subtract the reactFlowBounds.left/top anymore
      // details: https://reactflow.dev/whats-new/2023-11-10
      const nodeSize = getCustomNodeSizeByType(type);
      const position = screenToFlowPosition({
        x: event.clientX - nodeSize.width / 2 * zoom,
        y: event.clientY - nodeSize.height / 2 * zoom,
      });
      const newNode = {
        id: getId(),
        type,
        position,
        data: { label: `${type} node` },
      };

      setNodes((nds) => nds.concat(newNode));
    },
    [screenToFlowPosition, type],
  );
  return (
    <Stack direction="row" spacing={2}>
      <ReactFlowBox>
          <ReactFlow
            nodeTypes={nodeTypes}
            nodes={nodes}
            edges={edges}
            onNodesChange={onNodesChange}
            onEdgesChange={onEdgesChange}
            onConnect={onConnect}
            onDrop={onDrop}
            onDragOver={onDragOver}
            fitView
          >
            <Controls />
            <Background />
          </ReactFlow>
      </ReactFlowBox>
      <Sidebar />
    </Stack>
  );
};

export default () => (
  <ReactFlowProvider>
    <DnDProvider>
      <DnDFlow />
    </DnDProvider>
  </ReactFlowProvider>
);