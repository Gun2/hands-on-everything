import React, { useCallback, useState } from 'react';
import { ConfirmProps } from '../index';

interface ShowConfirmParams {
    title: ConfirmProps["title"];
    message: ConfirmProps["message"];
    onClickConfirm: ConfirmProps["onClickConfirm"];

}

export interface UseConfirmResult {
    confirmProps : ConfirmProps
    /**
     * confirm 나타내기
     * @param title 제목
     * @param message 내용
     */
    showConfirm: (params: ShowConfirmParams) => void;

}

export const useConfirm = () : UseConfirmResult => {
    const [open, setOpen] = useState(false);
    const handleOpen = useCallback((_open : boolean) => {
        setOpen(_open);
    }, []);
    const [title, setTitle] = useState('');
    const [message, setMessage] = useState<React.ReactNode>('');
    const [onClickConfirm, setOnClickConfirm] = useState<ConfirmProps["onClickConfirm"]>(() => {})
    const showConfirm = useCallback((params : ShowConfirmParams) => {
        setTitle(params.title);
        setMessage(params.message);
        setOnClickConfirm(() => params.onClickConfirm);
        handleOpen(true);
    }, [handleOpen]);
    return {
        confirmProps : {
            open,
            handleOpen,
            title,
            message,
            onClickConfirm,
        },
        showConfirm,
    }

}