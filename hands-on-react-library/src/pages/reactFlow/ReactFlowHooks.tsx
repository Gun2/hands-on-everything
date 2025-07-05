import React, { useCallback, useMemo, useState } from 'react';
import {
  Edge,
  Handle,
  Node,
  NodeProps,
  NodeTypes,
  Position,
  ReactFlow,
  ReactFlowProvider,
  useConnection,
  useEdges,
  useEdgesState,
  useInternalNode,
  useKeyPress,
  useNodeConnections,
  useNodeId,
  useNodes,
  useNodesData,
  useNodesInitialized,
  useNodesState,
  useOnViewportChange,
  useReactFlow,
  useStore, useUpdateNodeInternals,
  Viewport,
} from '@xyflow/react';
import ReactFlowBox from './ReactFlowBox';
import { Button, Stack } from '@mui/material';


const ReactFlowHooks = () => {
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);  // 노드 상태
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);  // 엣지 상태
  const nodeTypes: NodeTypes = useMemo(() => {
    return {
      custom: CustomNode
    }
  }, [])
  const connection = useConnection();
  const edgesFromHook = useEdges();
  const internalNode = useInternalNode('1');
  const spacePressed = useKeyPress(
    //감지할 키
    'Space'
  );
  //키 조합의 경우 + 문자를 사용하여 작성할 수 있음
  const cmdAndSPressed = useKeyPress(['Meta+s', 'Strg+s']);
  const nodesFromHook = useNodes();
  const nodeData = useNodesData('4');
  const nodesInitialized = useNodesInitialized();
  useOnViewportChange({
    onStart: (viewport: Viewport) => console.log('start', viewport),
    onChange: (viewport: Viewport) => console.log('change', viewport),
    onEnd: (viewport: Viewport) => console.log('end', viewport),
  });
  const reactFlow = useReactFlow();
  const nodesFromStore = useStore(state => state.nodes);
  return (
    <Stack direction="column" spacing={2}>
      <div>
        {connection ? `Someone is trying to make a connection from ${connection.fromNode?.id} to this one.` : 'There are currently no incoming connections!'}
      </div>
      <div>
        There are currently {edgesFromHook.length} edges!
      </div>
      <div>
        {`node id : ${internalNode?.id} position x :${internalNode?.internals?.positionAbsolute.x} y : ${internalNode?.internals?.positionAbsolute.y}`}
      </div>
      <div>
        {spacePressed && <p>Space pressed!</p>}
        {cmdAndSPressed && <p>Cmd + S pressed!</p>}
      </div>
      <div>There are currently {nodesFromHook.length} nodes!</div>
      <div>Type of node id 4 is {nodeData?.type}</div>
      <div>nodesInitialized : {nodesInitialized + ''}</div>
      <div><Button onClick={() => reactFlow?.fitView()}>fit view</Button></div>
      <ReactFlowBox>
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          nodeTypes={nodeTypes}
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
  { id: 'e2-3', source: '2', target: '3', animated: true},
  { id: 'e2-4', source: '2', target: '4', animated: true},
];


type CustomNodeType = Node<{
  label: string,
}>
const CustomNode = (node : NodeProps<CustomNodeType>) => {
  const updateNodeInternals = useUpdateNodeInternals();

  const [handleCount, setHandleCount] = useState(0);

  const connections = useNodeConnections({
    handleType: 'target',
  });
  const nodeId = useNodeId();
  const randomizeHandeCount = useCallback(() => {
    setHandleCount(Math.floor(Math.random() * 10));
    updateNodeInternals(node.id)
  }, [updateNodeInternals, node.id]);
  return (
    <div style={{backgroundColor: 'white', width: '100%', height: '100%', border: '1px solid black'}}>
      <Handle id={"original"} type={"source"} position={Position.Top}/>
      <Handle type={"target"} position={Position.Left}/>
      {
        Array.from({length: handleCount}).map((_, index) => <Handle key={index} type={"source"}  id={`d-${index}`} position={Position.Bottom}/>)
      }
      <div>There are currently {connections.length} incoming connections!</div>
      <div>{node.data.label}</div>
      <span>{nodeId}</span>
      <Button
        onClick={randomizeHandeCount}
      >
        Randomize handle count
      </Button>
      <div>handle count : {handleCount}</div>
    </div>
  );
};


export default function(){
  return (
    <ReactFlowProvider>
      <ReactFlowHooks/>
    </ReactFlowProvider>
  )
};