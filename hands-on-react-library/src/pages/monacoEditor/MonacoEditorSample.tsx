import React from 'react';
import { Editor } from '@monaco-editor/react';

const MonacoEditorSample = () => {
  return (
    <Editor height="50vh" defaultLanguage="javascript" defaultValue="// some comment" />
  );
};

export default MonacoEditorSample;