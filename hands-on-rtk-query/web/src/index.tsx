import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import BoardList from "./components/board/BoardList";
import {store} from './redux/store'
import {Provider} from 'react-redux'
import BoardRegistry from "./components/board/BoardRegistry";
import BoardDetail from "./components/board/BoardDetail";
import BoardEdit from "./components/board/BoardEdit";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
      <Provider store={store}>
          <div style={{padding: 10}}>
            <RouterProvider router={createBrowserRouter([
                {
                    path:'/',
                    element: <BoardList/>
                },
                {
                    path: '/board',
                    element: <BoardList/>
                },
                {
                    path: "/board/create",
                    element: <BoardRegistry/>
                },
                {
                    path: "/board/:id",
                    element: <BoardDetail/>
                },
                {
                    path: "/board/:id/edit",
                    element: <BoardEdit/>
                },

            ])}/>
          </div>
      </Provider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
