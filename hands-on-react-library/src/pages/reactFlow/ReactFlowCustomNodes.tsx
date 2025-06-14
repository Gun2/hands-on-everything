import React, { useCallback, useMemo } from 'react';
import ReactFlowInputNode from './customNodes/ReactFlowInputNode';
import { addEdge, Edge, Node, NodeTypes, OnConnect, ReactFlow, useEdgesState, useNodesState } from '@xyflow/react';
import ReactFlowBox from './ReactFlowBox';

const ReactFlowCustomNodes = () => {
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);  // 노드 상태
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);  // 엣지 상태
  const onConnect : OnConnect = useCallback(
    (connection) => setEdges((eds) => addEdge(connection, eds)),
    [setEdges],
  );
  const nodeTypes = useMemo<NodeTypes>(() => {
    return ({
      input : ReactFlowInputNode
    })
  }, []);
  return (
    <ReactFlowBox>
      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        nodeTypes={nodeTypes}
        onConnect={onConnect}
      />
    </ReactFlowBox>
  );
};


const initialNodes: Node[] = [
  {
    id: '1',
    type: 'input',
    data: { label: 'Input Node' },
    position: { x: 250, y: 25 },
  },

  {
    id: '2',
    // you can also pass a React component as a label
    data: { label: <div>Default Node</div> },
    position: { x: 100, y: 125 },
  },
  {
    id: '3',
    type: 'output',
    data: { label: 'Output Node' },
    position: { x: 250, y: 250 },
  },
];

const initialEdges: Edge[] = [
  { id: 'e1-2', source: '1', target: '2' },
  { id: 'e2-3', source: '2', target: '3', animated: true },
];

export default ReactFlowCustomNodes;