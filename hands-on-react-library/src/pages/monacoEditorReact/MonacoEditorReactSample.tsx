import React from 'react';
import { Editor } from '@monaco-editor/react';

const MonacoEditorReactSample = () => {
  return (
    <Editor height="50vh" defaultLanguage="javascript" defaultValue="// some comment" />
  );
};

export default MonacoEditorReactSample;