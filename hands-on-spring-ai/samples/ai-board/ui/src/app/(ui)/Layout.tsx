import React from 'react';
import { Stack } from '@mui/material';

export interface LayoutProps{
    //문구 영역
    contextArea: React.ReactNode;
    //버튼 영역
    buttonsArea: React.ReactNode;
}
export default function Layout(
    {
        buttonsArea,
        contextArea
    }: LayoutProps
){
    return (
        <Stack>
            {contextArea}
            <Stack direction="row">
                {buttonsArea}
            </Stack>
        </Stack>
    )
};