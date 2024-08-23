import React from 'react';
import {Link, Outlet} from "react-router-dom";

function App() {
    return (
        <div>
            <div style={{display: 'flex', gap: 5}}>
                <Link to={"/2d"}>
                    <button>ForceGraph2D</button>
                </Link>
            </div>
            <div>
                <Outlet/>
            </div>
        </div>
    );
}

export default App;
