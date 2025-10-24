import React from 'react';
import { Stack, Typography } from '@mui/material';


export interface LayoutProps {
    //제목
    titleArea: React.ReactNode;
    //내용
    contentArea: React.ReactNode;
    //도구 영역
    toolArea: React.ReactNode;
}
export default function Layout(
    {
        contentArea,
        toolArea,
        titleArea,
    }: LayoutProps
){
    return (
        <Stack>
            <Typography>{titleArea}</Typography>
            <div>{toolArea}</div>
            <div>{contentArea}</div>
        </Stack>
    )
};