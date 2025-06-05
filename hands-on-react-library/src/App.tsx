import React from 'react';
import { Route, Routes } from 'react-router-dom';
import PageLayout from './layout/PageLayout';

export const ROUTING_PATH = {
}

function App() {
  return (
    <div>
      <Routes>
        <Route path={"/"} element={<PageLayout/>}>
        </Route>
      </Routes>
    </div>
  );
}

export default App;
