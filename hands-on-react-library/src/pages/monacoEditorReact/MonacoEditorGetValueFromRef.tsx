import React, { useRef } from 'react';
import { Stack } from '@mui/material';
import { Editor } from '@monaco-editor/react';

/*
const MonacoEditorGetValueFromRef = () => {
  const editorRef = useRef<IStandaloneCodeEditor>(null);
  function handleEditorDidMount(editor, monaco) {
    editorRef.current = editor;
  }

  function showValue() {
    alert(editorRef.current.getValue());
  }

  return (
    <Stack gap={1}>
      <button onClick={showValue}>Show value</button>
      <Editor
        height="50vh"
        defaultLanguage="javascript"
        defaultValue="// some comment"
        onMount={handleEditorDidMount}
      />
    </Stack>
  );
};

export default MonacoEditorGetValueFromRef;*/
