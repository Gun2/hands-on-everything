import React, { useMemo } from 'react';
import {
  Background,
  BackgroundVariant,
  ControlButton,
  Controls,
  Edge,
  Handle,
  MiniMap,
  Node,
  NodeProps,
  NodeResizeControl,
  NodeResizer,
  NodeToolbar,
  NodeTypes,
  Panel,
  Position,
  ReactFlow,
  useEdgesState,
  useNodesState,
  ViewportPortal,
} from '@xyflow/react';
import ReactFlowBox from './ReactFlowBox';
import AutoFixHighIcon from '@mui/icons-material/AutoFixHigh';


const ReactFlowComponents = () => {
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);  // 노드 상태
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);  // 엣지 상태

  const nodeTypes : NodeTypes = useMemo(() => ({
    custom : CustomNode
  }), [])

  return (
    <ReactFlowBox>
      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        nodeTypes={nodeTypes}
      >
        <Background id="1" variant={BackgroundVariant.Dots} gap={12} size={1} />
        <Controls>
          <ControlButton onClick={() => alert('Something magical just happened. ✨')}>
            <AutoFixHighIcon />
          </ControlButton>
        </Controls>
        <MiniMap nodeStrokeWidth={3} />
        <Panel position="top-left">top-left</Panel>
        <ViewportPortal>
          <div
            style={{ transform: 'translate(100px, 100px)', position: 'absolute' }}
          >
            This div is positioned at [100, 100] on the flow.
          </div>
        </ViewportPortal>
      </ReactFlow>
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
  {
    id: '4',
    type: 'custom',
    data: { label: 'Custom Node' },
    position: { x: 250, y: 300 },
  },
];

const initialEdges: Edge[] = [
  { id: 'e1-2', source: '1', target: '2' },
  { id: 'e2-3', source: '2', target: '3', animated: true },
  { id: 'e3-4', source: '2', target: '4'},
];

type CustomNodeType = Node<{
  label: string,
}>
const CustomNode = (node : NodeProps<CustomNodeType>) => {
  return (
    <div style={{backgroundColor: 'white', width: '100%', height: '100%', border: '1px solid black'}}>
      <NodeToolbar isVisible position={Position.Top}>
        <button>delete</button>
        <button>copy</button>
        <button>expand</button>
      </NodeToolbar>
      <div>{node.data.label}</div>
      <Handle type="target" position={Position.Left} />
      <Handle type="source" position={Position.Right} />
      <NodeResizeControl />
      <NodeResizer minWidth={100} minHeight={30} />
    </div>
  );
};

export default ReactFlowComponents;