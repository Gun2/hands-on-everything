import React from 'react';
import {Drawer} from "@mui/material";

type LayoutProps = {
    title: string,
    content: React.ReactNode
}
const Layout = (
    {
        title,
        content
    }: LayoutProps
) => {
    return (
        <div style={{display: 'flex', gap: 5, flexDirection: 'column'}}>
            <h1>{title}</h1>
            <Drawer/>
            {content}
        </div>
    );
};

export default Layout;