import { Input } from '@mui/material';
import React, { ChangeEvent, useCallback, useState } from 'react';
import { Handle, Position } from '@xyflow/react';

const ReactFlowInputNode = () => {
  const [value, setValue] = useState("");;

  const onChange = useCallback(({target : {value}} : ChangeEvent<HTMLInputElement>) => {
    setValue(value);
  }, [])
  return (
    <>
      <Input
        value={value}
        onChange={onChange}
      />
    <Handle type="source" position={Position.Top} />
    <Handle type="target" position={Position.Bottom} />
    </>
  );
};

export default ReactFlowInputNode;