import React from 'react';
import { Stack } from '@mui/material';
import { useDnD } from './DnDContext';
import { BlueNode, GreenNode, RedNode } from './CustomNodes';
import { NodeType } from './nodeTypes';

const Sidebar = () => {
  return (
    <aside>
      <Stack spacing={2}>
        <DragAndDropItem
          label={'Red node'}
          nodeType={"red"}
        >
          <RedNode/>
        </DragAndDropItem>
        <DragAndDropItem
          label={'Green node'}
          nodeType={"green"}
        >
          <GreenNode/>
        </DragAndDropItem>
        <DragAndDropItem
          label={'Blue node'}
          nodeType={"blue"}
        >
          <BlueNode/>
        </DragAndDropItem>
      </Stack>
    </aside>
  );
};

type DragAndDropItemProps = Omit<React.HTMLAttributes<HTMLDivElement>, "onDragStart" | "draggable"> & {
  label: React.ReactNode;
  nodeType: NodeType;
}

const DragAndDropItem = (
  {
    label,
    children,
    style,
    nodeType,
    ...props
  }: DragAndDropItemProps
) => {
  const {setType, type} = useDnD();

  const onDragStart = (event: React.DragEvent<HTMLDivElement>, nodeType: NodeType) => {
    setType(nodeType);
    event.dataTransfer.effectAllowed = 'move';
  };

  return (
    <div
      {...props}
      style={{position:'relative', width: 150, height: 50, ...style }}
    >
      <div
         style={{position: 'absolute', width: '100%', height: '100%', display: "flex", alignItems: "center", justifyItems: "center", border: '1px solid #000', padding: 2}}
         onDragStart={(event) => onDragStart(event, nodeType)}
         draggable
      >
        <Stack direction="row" spacing={2}>
          <div>
            {children}
          </div>
          <div style={{flex: 1, display: 'flex', alignItems: "center"}}>
            {label}
          </div>
        </Stack>
      </div>
    </div>
  )
}

export default Sidebar;