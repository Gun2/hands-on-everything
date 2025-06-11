import React from 'react';
import { Route, Routes } from 'react-router-dom';
import TextareaAutocompletePage from './pages/textareaAutocomplete/TextareaAutocompletePage';
import PageLayout from './layout/PageLayout';
import MonacoEditorReactPage from './pages/monacoEditorReact/MonacoEditorReactPage';
import ReactFlowPage from './pages/reactFlow/ReactFlowPage';

export const ROUTING_PATH = {
  REACT_TEXTAREA_AUTOCOMPLETE : "/react-textarea-autocomplete",
  MONACO_EDITOR_REACT : "/monaco-editor-react",
  REACT_FLOW : "/react-flow"
}

function App() {
  return (
    <div>
      <Routes>
        <Route path={"/"} element={<PageLayout/>}>
          <Route path={ROUTING_PATH.REACT_TEXTAREA_AUTOCOMPLETE} element={<TextareaAutocompletePage/>}/>
          <Route path={ROUTING_PATH.MONACO_EDITOR_REACT} element={<MonacoEditorReactPage/>}/>
          <Route path={ROUTING_PATH.REACT_FLOW} element={<ReactFlowPage/>}/>
        </Route>
      </Routes>
    </div>
  );
}

export default App;
