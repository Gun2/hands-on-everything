import { BlueNode, GreenNode, RedNode } from './CustomNodes';


export type NodeType = "red" | "blue" | "green";

export const NODE_TYPES : Record<string, NodeType> = {
  RED: "red",
  BLUE: "blue",
  GREEN: "green",
}


export const nodeTypes = {
  [NODE_TYPES.RED]: RedNode,
  [NODE_TYPES.BLUE]: BlueNode,
  [NODE_TYPES.GREEN]: GreenNode,
}
