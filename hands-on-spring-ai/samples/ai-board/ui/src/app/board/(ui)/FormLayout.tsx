import React from 'react';
import { Stack, Typography } from '@mui/material';


export interface LayoutProps {
    //제목
    titleArea: React.ReactNode;
    //내용
    contentArea: React.ReactNode;
    //액션 영역
    actionArea: React.ReactNode;
}
export default function FormLayout(
    {
        contentArea,
        actionArea,
        titleArea,
    }: LayoutProps
){
    return (
        <Stack>
            <Typography>{titleArea}</Typography>
            <div>{contentArea}</div>
            <div>{actionArea}</div>
        </Stack>
    )
};