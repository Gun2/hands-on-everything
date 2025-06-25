import { Node } from '@xyflow/react';
import { SimulationNodeDatum } from 'd3-force';

export type SimNode = Node & SimulationNodeDatum & {
  fx?: number;
  fy?: number;
};
