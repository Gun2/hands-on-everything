import React from 'react';
import { Handle, Position } from '@xyflow/react';
import { Card } from '@mui/material';
import { NodeType } from './nodeTypes';

export const CUSTOM_NODE_SIZE = {
  width: 60,
  height: 30,
}

type CustomNodeProps = {
  color: string;
  label: string;
}
const CustomNode = (
  {
    color,
    label,
  }: CustomNodeProps
) => {
  return (
    <Card sx={{backgroundColor: color, width: CUSTOM_NODE_SIZE.width, height: CUSTOM_NODE_SIZE.height}}>
      <div style={{color: 'white', padding: 5}}>{label}</div>
      <Handle type="source" position={Position.Top} />
      <Handle type="target" position={Position.Bottom} />
    </Card>
  );
};

export const RedNode = () => {
  return <CustomNode color={"red"} label={"Red"} />
}

export const BlueNode = () => {
  return <CustomNode color={"blue"} label={"Blue"} />
}

export const GreenNode = () => {
  return <CustomNode color={"green"} label={"Green"} />
}

export const getCustomNodeSizeByType = (type: NodeType) => {
  return CUSTOM_NODE_SIZE;
}