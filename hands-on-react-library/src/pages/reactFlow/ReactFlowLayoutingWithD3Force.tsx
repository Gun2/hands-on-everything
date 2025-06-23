import React, { useCallback, useMemo, useRef } from 'react';
import {
  Edge,
  Node,
  Panel,
  ReactFlow,
  ReactFlowProvider,
  useEdgesState, useNodesInitialized,
  useNodesState,
  useReactFlow,
} from '@xyflow/react';
import Dagre from '@dagrejs/dagre';
import { quadtree } from 'd3-quadtree';

import ReactFlowBox from './ReactFlowBox';
import { forceLink, forceManyBody, forceSimulation, forceX, forceY, SimulationNodeDatum } from 'd3-force';

type SimNode = Node & SimulationNodeDatum & {
  fx?: number;
  fy?: number;
};

// Create force simulation
const simulation = forceSimulation<SimulationNodeDatum & Node>()
  .force('charge', forceManyBody().strength(-1000))
  .force('x', forceX().x(0).strength(0.05))
  .force('y', forceY().y(0).strength(0.05))
  .force('collide', collide())
  .alphaTarget(0.05)
  .stop();


type UseLayoutedElementsResult = {
  initialized : boolean;
  toggle?: () => void;
  isRunning?: () => boolean;
  dragEvents : {
    start: (_event: unknown, node: Node) => void;
    drag: (_event: unknown, node: Node) => void;
    stop: () => void;
  }
};
// Custom hook for running force simulation
const useLayoutedElements = () : UseLayoutedElementsResult => {
  const { getNodes, setNodes, getEdges, fitView } = useReactFlow();
  const initialized = useNodesInitialized();
  const draggingNodeRef = useRef<Node | null>(null);

  const dragEvents = useMemo(() => ({
    start: (_event: unknown, node: Node) => (draggingNodeRef.current = node),
    drag: (_event: unknown, node: Node) => (draggingNodeRef.current = node),
    stop: () => (draggingNodeRef.current = null),
  }), []);

  return useMemo(() => {
    let nodes : SimNode[] = getNodes().map((node) => ({
      ...node,
      x: node.position.x,
      y: node.position.y,
    }));
    let edges = getEdges();
    let running = false;

    if (!initialized || nodes.length === 0) {
      return {
        initialized: false,
        dragEvents
      };
    }

    simulation.nodes(nodes).force(
      'link',
      forceLink(edges)
        .id((d: any) => d.id)
        .strength(0.05)
        .distance(100)
    );

    const tick = () => {
      getNodes().forEach((node, i) => {
        const dragging = draggingNodeRef.current?.id === node.id;
        if (dragging) {
          nodes[i].fx = draggingNodeRef.current!.position.x;
          nodes[i].fy = draggingNodeRef.current!.position.y;
        } else {
          delete nodes[i].fx;
          delete nodes[i].fy;
        }
      });

      simulation.tick();
      setNodes(
        nodes.map((node) => ({
          ...node,
          position: {
            x: node.fx ?? node.x ?? 0,
            y: node.fy ?? node.y ?? 0,
          },
        }))
      );

      window.requestAnimationFrame(() => {
        fitView();
        if (running) tick();
      });
    };

    const toggle = () => {
      if (!running) {
        getNodes().forEach((node, index) => {
          const simNode = nodes[index];
          Object.assign(simNode, node);
          simNode.x = node.position.x;
          simNode.y = node.position.y;
        });
      }
      running = !running;
      if (running) window.requestAnimationFrame(tick);
    };

    return {
      initialized: true,
      toggle,
      isRunning: () => running,
      dragEvents
    }
  }, [initialized, dragEvents, getNodes, getEdges, setNodes, fitView]);
};


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


// Type-safe collide function using d3-quadtree
function collide() {
  let nodes : SimNode[] = [];

  const force = (alpha: number) => {
    const tree = quadtree<SimulationNodeDatum & Node>(
      nodes,
      (d) => d.x ?? 0,
      (d) => d.y ?? 0
    );

    for (const node of nodes) {
      const r = (node.measured?.width ?? 100) / 2;
      const nx1 = node.x! - r;
      const nx2 = node.x! + r;
      const ny1 = node.y! - r;
      const ny2 = node.y! + r;

      tree.visit((quad, x1, y1, x2, y2) => {
        if (!quad.length) {
          let d = quad.data;
          if (d && d !== node) {
            const r = (node.measured?.width ?? 100) / 2 + (d.measured?.width ?? 100) / 2;
            let dx = node.x! - d.x!;
            let dy = node.y! - d.y!;
            let l = Math.hypot(dx, dy);

            if (l < r) {
              l = ((l - r) / l) * alpha;
              node.x! -= dx *= l;
              node.y! -= dy *= l;
              d.x! += dx;
              d.y! += dy;
            }
          }
        }
        return x1 > nx2 || x2 < nx1 || y1 > ny2 || y2 < ny1;
      });
    }
  };

  force.initialize = (newNodes: SimNode[]) => (nodes = newNodes);

  return force;
}


export default function () {
  return (
    <ReactFlowProvider>
      <ReactFlowLayoutingWithD3Force />
    </ReactFlowProvider>
  );
}