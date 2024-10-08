import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import {HandsOnForceGraph2DMain} from "./components/HandsOnForceGraph2D/HandsOnForceGraph2DMain";
import {Base2D} from "./components/HandsOnForceGraph2D/Base2D";
import {NodeCustom2D} from "./components/HandsOnForceGraph2D/NodeCustom2D";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const router = createBrowserRouter([
    {
        path: "/",
        element: <App/>,
    },
    {
        path: "/2d",
        element: <HandsOnForceGraph2DMain/>,
        children: [
            {
                path: "",
                element: <Base2D/>
            },
            {
                path: "node-custom",
                element: <NodeCustom2D/>
            }
        ]
    }
]);

root.render(
  <React.StrictMode>
      <RouterProvider router={router}/>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
