import React, { useCallback, useMemo } from 'react';
import {
  Background,
  BackgroundVariant, Edge,
  type Node,
  type NodeMouseHandler,
  ReactFlow,
  ReactFlowProvider,
  useReactFlow,
} from '@xyflow/react';
import { Slide, SLIDE_HEIGHT, SLIDE_PADDING, SLIDE_WIDTH, type SlideData } from './Slide';
import ReactFlowBox from '../../ReactFlowBox';

type Stack = {
  id: string;
  position: {
    x: number;
    y: number;
  }
}
const slidesToElements = () : {start: string; nodes: Node[]; edges: Edge[]} => {
  const start = Object.keys(slides)[0];
  const stack: Array<Stack> = [{ id: start, position: { x: 0, y: 0 } }];
  const visited = new Set();
  const nodes: Node[] = [];
  const edges: Edge[] = [];

  while (stack.length) {
    const { id, position } = stack.pop() as Stack;
    const slide = slides[id];
    const node = {
      id,
      type: 'slide',
      position,
      data: slide,
      draggable: false,
    } satisfies Node<SlideData>;

    if (slide.left && !visited.has(slide.left)) {
      const nextPosition = {
        x: position.x - (SLIDE_WIDTH + SLIDE_PADDING),
        y: position.y,
      };

      stack.push({ id: slide.left, position: nextPosition });
      edges.push({
        id: `${id}->${slide.left}`,
        source: id,
        target: slide.left,
      });
    }

    if (slide.up && !visited.has(slide.up)) {
      const nextPosition = {
        x: position.x,
        y: position.y - (SLIDE_HEIGHT + SLIDE_PADDING),
      };

      stack.push({ id: slide.up, position: nextPosition });
      edges.push({ id: `${id}->${slide.up}`, source: id, target: slide.up });
    }

    if (slide.down && !visited.has(slide.down)) {
      const nextPosition = {
        x: position.x,
        y: position.y + (SLIDE_HEIGHT + SLIDE_PADDING),
      };

      stack.push({ id: slide.down, position: nextPosition });
      edges.push({
        id: `${id}->${slide.down}`,
        source: id,
        target: slide.down,
      });
    }

    if (slide.right && !visited.has(slide.right)) {
      const nextPosition = {
        x: position.x + (SLIDE_WIDTH + SLIDE_PADDING),
        y: position.y,
      };

      stack.push({ id: slide.right, position: nextPosition });
      if (slide.down){
        edges.push({
          id: `${id}->${slide.down}`,
          source: id,
          target: slide.down,
        });
      }
    }

    nodes.push(node);
    visited.add(id);
  }

  return { start, nodes, edges };
};

const slides: Record<string, SlideData> = {
  '0': { source: '# Hello, React Flow!', right: '1' },
  '1': { source: '...', left: '0', right: '2' },
  '2': { source: '...', left: '1' },
};
const nodeTypes = {
  slide: Slide,
};


const ReactFlowSlideshow = () => {

  const { fitView } = useReactFlow();
  const { start, nodes, edges } = useMemo(() => slidesToElements(), []);

  const handleNodeClick = useCallback<NodeMouseHandler>(
    (_, node) => {
      fitView({ nodes: [{ id: node.id }], duration: 150 });
    },
    [fitView],
  );

  return (
    <ReactFlowBox>
      <ReactFlow
        nodes={nodes}
        nodeTypes={nodeTypes}
        edges={edges}
        fitView
        fitViewOptions={{ nodes: [{ id: start }] }}
        minZoom={0.1}
        onNodeClick={handleNodeClick}
      >
        <Background color="#f2f2f2" variant={BackgroundVariant.Lines} />
      </ReactFlow>
    </ReactFlowBox>
  );
};

export default function() {
  return (
    <ReactFlowProvider>
      <ReactFlowSlideshow />
    </ReactFlowProvider>
  )
}