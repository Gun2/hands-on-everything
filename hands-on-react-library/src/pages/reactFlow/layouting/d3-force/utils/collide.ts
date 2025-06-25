import { quadtree } from 'd3-quadtree';
import { SimulationNodeDatum } from 'd3-force';
import { Node } from '@xyflow/react';
import { SimNode } from '../d3Force.types';

/**
 * 노드들이 서로 겹치는 현상 방지
 */
export function collide() {
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