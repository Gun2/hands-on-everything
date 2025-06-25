import { Node, useNodesInitialized, useReactFlow } from '@xyflow/react';
import { useMemo, useRef } from 'react';
import { forceLink, forceManyBody, forceSimulation, forceX, forceY, SimulationNodeDatum } from 'd3-force';
import { SimNode } from '../d3Force.types';
import { collide } from '../utils/collide';

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
export const useLayoutedElements = () : UseLayoutedElementsResult => {
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

// Create force simulation
const simulation = forceSimulation<SimulationNodeDatum & Node>()
  .force('charge', forceManyBody().strength(-1000))
  .force('x', forceX().x(0).strength(0.05))
  .force('y', forceY().y(0).strength(0.05))
  .force('collide', collide())
  .alphaTarget(0.05)
  .stop();
