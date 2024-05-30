import React from 'react';
import {Stack} from "@mui/material";

type ContentLayoutProps = {
    contentArea: React.ReactNode;
    buttonArea: React.ReactNode;
}
export const ContentLayout = (
    {
        contentArea,
        buttonArea
    }: ContentLayoutProps
) => {
    return (
        <>
            <Stack spacing={2}>
                {contentArea}
            </Stack>
            <Stack>
                <Stack direction={"row"} spacing={1} justifyContent={"flex-end"}>
                    {buttonArea}
                </Stack>
            </Stack>
        </>
    );
};

