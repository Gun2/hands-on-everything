import React, { useCallback } from 'react';
import {
  Edge,
  Node,
  Panel,
  ReactFlow,
  ReactFlowProvider,
  useEdgesState,
  useNodesState,
  useReactFlow,
} from '@xyflow/react';
import Dagre from '@dagrejs/dagre';
import ReactFlowBox from './ReactFlowBox';

type RankDir = "TB" |  "BT" | "LR" | "RL";
type Option = {
  direction : RankDir
}
type LayoutNode = Node & {
  measured?: {
    width?: number;
    height?: number;
  };
};

type GetLayoutedElementsResult = {
  nodes: LayoutNode[];
  edges: Edge[];
}

/**
 * Dagre를 이용해 노드들의 레이아웃 위치를 자동 계산해줌.
 * Dagre는 노드와 엣지를 기반으로 DAG(Directed Acyclic Graph) 레이아웃을 계산함.
 */
const getLayoutedElements = (
  nodes: Node[],
  edges: Edge[],
  options: Option
): GetLayoutedElementsResult => {
  // 1. Dagre 그래프 객체 생성 (무조건 Directed Graph)
  const g = new Dagre.graphlib.Graph().setDefaultEdgeLabel(() => ({}));

  // 2. 레이아웃 방향 설정
  g.setGraph({ rankdir: options.direction});

  // 3. 엣지 연결 정보 설정
  edges.forEach((edge) => g.setEdge(edge.source, edge.target));

  // 4. 노드 위치 계산을 위한 크기 정보 등록
  nodes.forEach((node) =>
    g.setNode(node.id, {
      ...node,
      width: node.measured?.width ?? 0,
      height: node.measured?.height ?? 0,
    }),
  );

  // 5. Dagre로 레이아웃 계산 수행 (실제 위치가 내부적으로 설정됨)
  Dagre.layout(g);

  // 6. 계산된 위치값으로 React Flow 노드의 위치를 변환
  const layoutedNodes = nodes.map((node) => {

    // g.node(node.id)는 { x: number, y: number }를 포함한 위치 정보
    const position = g.node(node.id);

    // Dagre는 노드 중심 좌표를 기준으로 계산
    // → React Flow는 좌상단 기준이므로, 중심 기준에서 절반 크기만큼 빼줌
    const x = position.x - (node.measured?.width ?? 0) / 2;
    const y = position.y - (node.measured?.height ?? 0) / 2;

    return {
      ...node,
      position: { x, y }, // 최종 좌표 설정
    };
  });

  return { nodes: layoutedNodes, edges };
}
const ReactFlowLayoutingWithDagre = () => {
  const { fitView } = useReactFlow();
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);

  const onLayout = useCallback(
    (direction: RankDir) => {
      console.log(nodes);
      const layouted = getLayoutedElements(nodes, edges, { direction });

      setNodes([...layouted.nodes]);
      setEdges([...layouted.edges]);

      fitView();
    },
    [nodes, edges],
  );

  return (
    <ReactFlowBox>
      <ReactFlowProvider>
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          fitView
        >
          <Panel position="top-right">
            <button onClick={() => onLayout('TB')}>vertical layout</button>
            <button onClick={() => onLayout('LR')}>horizontal layout</button>
          </Panel>
        </ReactFlow>
      </ReactFlowProvider>
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
      <ReactFlowLayoutingWithDagre />
    </ReactFlowProvider>
  );
}