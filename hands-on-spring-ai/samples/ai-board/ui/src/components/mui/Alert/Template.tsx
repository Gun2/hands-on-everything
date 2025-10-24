import React, { useCallback } from 'react';
import { Button, Dialog, DialogContent, DialogTitle, styled } from '@mui/material';

export interface TemplateProps {
    open: boolean;
    handleOpen: (open: boolean) => void;
    title: string;
    message: React.ReactNode;
}

/**
 *
 * @param open alert 표시 상태
 * @param handleOpen alert 표시 상태 핸들링
 * @param title alert 제목
 * @param message alert 메세지
 */
export default function Template(
    {
        open,
        handleOpen,
        title,
        message,
    }: TemplateProps
){
    const onClickClose = useCallback(() => {
        handleOpen(false);
    }, [handleOpen]);
    return (
        <CustomDialog
             open={open}
        >
            <DialogTitle>{title}</DialogTitle>
            <DialogContent>{message}</DialogContent>
            <Button ref={element => element?.focus?.()} onClick={onClickClose}>close</Button>
        </CustomDialog>
    );
};

const CustomDialog = styled(Dialog)((props) => ({
    "& .MuiPaper-root" : {
        minWidth: 300
    }
}))