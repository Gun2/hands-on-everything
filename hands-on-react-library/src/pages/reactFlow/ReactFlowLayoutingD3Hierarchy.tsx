import React, { useCallback } from 'react';
import {
  ReactFlow,
  ReactFlowProvider,
  Panel,
  useNodesState,
  useEdgesState,
  useReactFlow,
  Node,
  Edge,
} from '@xyflow/react';
import { stratify, tree, HierarchyNode } from 'd3-hierarchy';
import '@xyflow/react/dist/style.css';
import ReactFlowBox from './ReactFlowBox';

// 타입 선언
type NodeData = {
  data: any;
  [key: string]: any;
};

type GetLayoutedElementsResult = {
  nodes: Node[];
  edges: Edge[];
}

// d3-hierarchy 트리 레이아웃 생성기
const g = tree<NodeData>();

const getLayoutedElements = (
  nodes: Node[],
  edges: Edge[]
): GetLayoutedElementsResult => {
  if (nodes.length === 0) return { nodes, edges };

  //루트 노드 element 가져오기 (샘플 데이터의 경우 가장 첫 번째 노드)
  const firstNodeEl = document.querySelector(`[data-id="${nodes[0].id}"]`);
  if (!firstNodeEl) return { nodes, edges };

  const { width, height } = firstNodeEl.getBoundingClientRect();

  const hierarchy = stratify<NodeData>()
    .id((node) => node.id)
    .parentId((node) => edges.find((edge) => edge.target === node.id)?.source);

  const root = hierarchy(nodes as any);
  const layout = g.nodeSize([width * 2, height * 2])(root as HierarchyNode<NodeData>);

  return {
    nodes: layout
      .descendants()
      .map((node) => ({ ...nodes.find(originalNode => node.id === originalNode.id) as Node, position: { x: node.x, y: node.y } })),
    edges,
  };
};

const ReactFlowLayoutingD3Hierarchy = () => {
  const { fitView } = useReactFlow();
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);

  const onLayout = useCallback(() => {
    const { nodes: layoutedNodes, edges: layoutedEdges } = getLayoutedElements(
      nodes,
      edges
    );

    setNodes([...layoutedNodes]);
    setEdges([...layoutedEdges]);
    fitView();
  }, [nodes, edges, fitView, setNodes, setEdges]);

  return (
    <ReactFlowBox>
      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        fitView
      >
        <Panel position="top-right">
          <button onClick={onLayout}>layout</button>
        </Panel>
      </ReactFlow>
    </ReactFlowBox>
  );
};

const initialNodes: Node[] = [
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

const initialEdges: Edge[] = [
  { id: 'e12', source: '1', target: '2', animated: true },
  { id: 'e13', source: '1', target: '3', animated: true },
  { id: 'e22a', source: '2', target: '2a', animated: true },
  { id: 'e22b', source: '2', target: '2b', animated: true },
  { id: 'e22c', source: '2', target: '2c', animated: true },
  { id: 'e2c2d', source: '2c', target: '2d', animated: true },
];

export default function() {
  return (
    <ReactFlowProvider>
      <ReactFlowLayoutingD3Hierarchy/>
    </ReactFlowProvider>
  )
};