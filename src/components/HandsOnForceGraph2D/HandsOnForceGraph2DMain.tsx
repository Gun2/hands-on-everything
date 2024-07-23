import React from 'react';
import {Link, Outlet} from "react-router-dom";

export const HandsOnForceGraph2DMain = () => {
    return (
        <div>
            <div style={{display:'flex', gap:5}}>
                <Link to={"/"}>
                    <button>main</button>
                </Link>
                <Link to={"/2d"}>
                    <button>base</button>
                </Link>
            </div>
            <div>
                <Outlet/>
            </div>
        </div>
    );
};