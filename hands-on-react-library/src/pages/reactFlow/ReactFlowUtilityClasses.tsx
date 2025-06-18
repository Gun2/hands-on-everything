import React, { useCallback, useMemo } from 'react';
import { addEdge, Edge, Node, NodeTypes, OnConnect, ReactFlow, useEdgesState, useNodesState } from '@xyflow/react';
import ReactFlowBox from './ReactFlowBox';
import ReactFlowClassNameNode, { ReactFlowClassNameNodeData } from './customNodes/ReactFlowClassNameNode';

const ReactFlowUtilityClasses = () => {
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);  // 노드 상태
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);  // 엣지 상태
  const onConnect : OnConnect = useCallback(
    (connection) => setEdges((eds) => addEdge(connection, eds)),
    [setEdges],
  );
  const nodeTypes = useMemo<NodeTypes>(() => {
    return ({
      className : ReactFlowClassNameNode
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


const initialNodes: Node[] | ReactFlowClassNameNodeData[] = [
  {
    id: '1',
    type: 'className',
    data: { label: 'No drag Node', className: 'nodrag' },
    position: { x: 250, y: 25 },
  },

  {
    id: '2',
    type: 'className',
    data: { label: 'No wheel Node', className: 'nowheel' },
    position: { x: 100, y: 125 },
  },
  {
    id: '3',
    type: 'className',
    data: { label: 'No pan Node', className: 'nopan' },
    position: { x: 250, y: 250 },
  },
  {
    id: '4',
    type: 'className',
    data: { label: 'No class Node' },
    position: { x: 300, y: 300 },
  },
];

const initialEdges: Edge[] = [
  { id: 'e1-2', source: '1', target: '2' },
  { id: 'e2-3', source: '2', target: '3', animated: true },
];

export default ReactFlowUtilityClasses;