import React, { useRef } from 'react';
import { Stack } from '@mui/material';
import Editor, { OnMount } from '@monaco-editor/react';
import * as monaco from 'monaco-editor';

const MonacoEditorGetValueFromRef = () => {
  const editorRef = useRef<monaco.editor.IStandaloneCodeEditor | null>(null);

  const handleEditorDidMount: OnMount = (editor, monacoInstance) => {
    editorRef.current = editor;
  };

  function showValue() {
    if (editorRef.current) {
      alert(editorRef.current.getValue());
    }
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

export default MonacoEditorGetValueFromRef;