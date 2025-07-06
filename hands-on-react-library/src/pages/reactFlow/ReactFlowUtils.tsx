import React, { useCallback, useMemo } from 'react';
import {
  addEdge,
  applyEdgeChanges,
  applyNodeChanges,
  BaseEdge,
  Edge,
  EdgeChange,
  EdgeProps,
  EdgeTypes,
  getStraightPath,
  Handle,
  Node,
  NodeChange,
  NodeProps,
  NodeTypes,
  Position,
  ReactFlow,
  ReactFlowProvider,
  reconnectEdge,
  useEdgesState,
  useNodesState,
} from '@xyflow/react';
import ReactFlowBox from './ReactFlowBox';
import { Stack } from '@mui/material';
import { Connection } from '@xyflow/system/dist/esm/types/general';


const ReactFlowUtils = () => {
  const [nodes, setNodes] = useNodesState(initialNodes);  // 노드 상태
  const [edges, setEdges] = useEdgesState(initialEdges);  // 엣지 상태
  const onConnect = useCallback(
    (connection: Connection) => {
      setEdges((oldEdges) => addEdge(connection, oldEdges));
    },
    [setEdges],
  );
  const nodeTypes: NodeTypes = useMemo(() => {
    return {
      custom: CustomNode
    }
  }, [])
  const edgeTypes: EdgeTypes = useMemo(() => {
    return {
      custom: CustomEdge
    }
  }, [])
  const onEdgesChange = useCallback(
    (changes: EdgeChange[]) => {
      setEdges((oldEdges) => applyEdgeChanges(changes, oldEdges));
    },
    [setEdges],
  );
  const onNodesChange = useCallback(
    (changes: NodeChange[]) => {
      setNodes((oldNodes) => applyNodeChanges(changes, oldNodes));
    },
    [setNodes],
  );
  const onReconnect = useCallback(
    (oldEdge: Edge, newConnection: Connection) => setEdges((els) => reconnectEdge(oldEdge, newConnection, els)),
    []
  );
  return (
    <Stack direction="column" spacing={2}>
      <ReactFlowBox>
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          nodeTypes={nodeTypes}
          edgeTypes={edgeTypes}
          onConnect={onConnect}
          onReconnect={onReconnect}
        />
      </ReactFlowBox>
    </Stack>
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
  {
    id: '4',
    type: 'custom',
    data: { label: 'Custom Node' },
    position: { x: 300, y: 300 },
  },
];

const initialEdges: Edge[] = [
  { id: 'e1-2', source: '1', target: '2'},
  { id: 'e2-3', source: '2', target: '3', animated: true, type: 'custom' },
  { id: 'e2-4', source: '2', target: '4', animated: true},
];


type CustomNodeType = Node<{
  label: string,
}>
const CustomNode = (node : NodeProps<CustomNodeType>) => {
  return (
    <div style={{backgroundColor: 'white', width: '100%', height: '100%', border: '1px solid black'}}>
      <Handle type={"source"} position={Position.Top}/>
      <Handle type={"target"} position={Position.Left}/>
      <div>{node.data.label}</div>
    </div>
  );
};


const CustomEdge = (
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

export default function(){
  return (
    <ReactFlowProvider>
      <ReactFlowUtils/>
    </ReactFlowProvider>
  )
};