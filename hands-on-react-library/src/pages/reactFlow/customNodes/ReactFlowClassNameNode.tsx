import React from 'react';
import { Handle, Node, NodeProps, Position } from '@xyflow/react';

export type ReactFlowClassNameNodeData = Node<{
  className?: "nodrag" | "nopan" | "nowheel";
  label?: string;
}, 'className' | 'label'>
const ReactFlowClassNameNode = (
  {
    data
  }: NodeProps<ReactFlowClassNameNodeData>
) => {

  return (
    <div
      className={data.className}
      style={{
        border: '1px solid #000',
        width: '100px',
        height: '100px',
      }}
    >
      {data?.label}
    <Handle type="source" position={Position.Top} />
    <Handle type="target" position={Position.Bottom} />
    </div>
  );
};

export default ReactFlowClassNameNode;