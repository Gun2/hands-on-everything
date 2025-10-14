import React from 'react';
import { Route, Routes } from 'react-router-dom';
import TextareaAutocompletePage from './pages/textareaAutocomplete/TextareaAutocompletePage';
import PageLayout from './layout/PageLayout';
import MonacoEditorReactPage from './pages/monacoEditorReact/MonacoEditorReactPage';
import ReactFlowPage from './pages/reactFlow/ReactFlowPage';
import ChatScopeChatUiKitPage from './pages/chatscopeChatUiKit/ChatScopeChatUiKitPage';

export const ROUTING_PATH = {
  REACT_TEXTAREA_AUTOCOMPLETE : "/react-textarea-autocomplete",
  MONACO_EDITOR_REACT : "/monaco-editor-react",
  REACT_FLOW : "/react-flow",
  CHATSCOPE_CHAT_UI_KIT : "/chatscope-chat-ui-kit"
}

function App() {
  return (
    <div>
      <Routes>
        <Route path={"/"} element={<PageLayout/>}>
          <Route path={ROUTING_PATH.REACT_TEXTAREA_AUTOCOMPLETE} element={<TextareaAutocompletePage/>}/>
          <Route path={ROUTING_PATH.MONACO_EDITOR_REACT} element={<MonacoEditorReactPage/>}/>
          <Route path={ROUTING_PATH.REACT_FLOW} element={<ReactFlowPage/>}/>
          <Route path={ROUTING_PATH.CHATSCOPE_CHAT_UI_KIT} element={<ChatScopeChatUiKitPage/>}/>
        </Route>
      </Routes>
    </div>
  );
}

export default App;
