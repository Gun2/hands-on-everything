import React, { useCallback } from 'react';
import { Button, Dialog, DialogContent, DialogTitle, Stack, styled } from '@mui/material';

export interface TemplateProps {
    open: boolean;
    handleOpen: (open: boolean) => void;
    onClickConfirm: () => void;
    title: string;
    message: React.ReactNode;
}

/**
 *
 * @param open confirm 표시 상태
 * @param handleOpen confirm 표시 상태 핸들링
 * @param title confirm 제목
 * @param message confirm 메세지
 * @param onClickConfirm confirm 확인 클릭 시 호출
 */
export default function Template(
    {
        open,
        handleOpen,
        title,
        message,
        onClickConfirm
    }: TemplateProps
){
    const onClickClose = useCallback(() => {
        handleOpen(false);
    }, [handleOpen]);
    const onClickConfirmButton = useCallback(() => {
        onClickConfirm?.();
        onClickClose();
    }, [onClickClose, onClickConfirm]);
    return (
        <CustomDialog
             open={open}
        >
            <DialogTitle>{title}</DialogTitle>
            <DialogContent>{message}</DialogContent>
            <Stack direction="row" gap={1}>
                <Button ref={element => element?.focus?.()} onClick={onClickConfirmButton}>confirm</Button>
                <Button color={"secondary"} onClick={onClickClose}>close</Button>
            </Stack>
        </CustomDialog>
    );
};

const CustomDialog = styled(Dialog)((props) => ({
    "& .MuiPaper-root" : {
        minWidth: 300
    }
}))