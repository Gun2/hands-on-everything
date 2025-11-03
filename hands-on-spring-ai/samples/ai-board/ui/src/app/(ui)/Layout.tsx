import React from 'react';
import { Stack } from '@mui/material';

export interface LayoutProps{
    //내용 영역
    contentArea: React.ReactNode;
    //헤더 영역
    headerArea: React.ReactNode;
}
export default function Layout(
    {
        headerArea,
        contentArea
    }: LayoutProps
){
    return (
        <Stack style={{width: '100%'}}>
              {headerArea}
            <div style={{width: '100%'}}>
              {contentArea}
            </div>
        </Stack>
    )
};