import React from 'react';
import { Route, Routes } from 'react-router-dom';
import TextareaAutocompletePage from './pages/textareaAutocomplete/TextareaAutocompletePage';
import PageLayout from './layout/PageLayout';
import MonacoEditorReactPage from './pages/monacoEditorReact/MonacoEditorReactPage';

export const ROUTING_PATH = {
  REACT_TEXTAREA_AUTOCOMPLETE : "/react-textarea-autocomplete",
  MONACO_EDITOR_REACT : "/monaco-editor-react"
}

function App() {
  return (
    <div>
      <Routes>
        <Route path={"/"} element={<PageLayout/>}>
          <Route path={ROUTING_PATH.REACT_TEXTAREA_AUTOCOMPLETE} element={<TextareaAutocompletePage/>}/>
          <Route path={ROUTING_PATH.MONACO_EDITOR_REACT} element={<MonacoEditorReactPage/>}/>
        </Route>
      </Routes>
    </div>
  );
}

export default App;
