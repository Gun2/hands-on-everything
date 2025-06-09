import React, { useRef, useState } from 'react';
import { Stack } from '@mui/material';
import Editor, { OnChange } from '@monaco-editor/react';
import * as monaco from 'monaco-editor';

const MonacoEditorGetValueFromOnChange = () => {
  const [value, setValue] = useState<string>();
  const handleEditorChange: OnChange = (value, event) => {
    setValue(value)
  };


  return (
    <Stack gap={1}>
      <Editor
        height="50vh"
        defaultLanguage="javascript"
        defaultValue="// some comment"
        onChange={handleEditorChange}
      />
      <div>value</div>
      <pre>{value}</pre>
    </Stack>
  );
};

export default MonacoEditorGetValueFromOnChange;