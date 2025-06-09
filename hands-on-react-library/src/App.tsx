import React from 'react';
import { Route, Routes } from 'react-router-dom';
import TextareaAutocompletePage from './textareaAutocomplete/TextareaAutocompletePage';
import PageLayout from './layout/PageLayout';
import MonacoEditorPage from './monacoEditor/MonacoEditorPage';

export const ROUTING_PATH = {
  REACT_TEXTAREA_AUTOCOMPLETE : "/react-textarea-autocomplete",
  MONACO_EDITOR : "/monaco-editor"
}

function App() {
  return (
    <div>
      <Routes>
        <Route path={"/"} element={<PageLayout/>}>
          <Route path={ROUTING_PATH.REACT_TEXTAREA_AUTOCOMPLETE} element={<TextareaAutocompletePage/>}/>
          <Route path={ROUTING_PATH.MONACO_EDITOR} element={<MonacoEditorPage/>}/>
        </Route>
      </Routes>
    </div>
  );
}

export default App;
