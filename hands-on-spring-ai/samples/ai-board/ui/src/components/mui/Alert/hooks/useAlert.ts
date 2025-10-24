import React, { useCallback, useState } from 'react';
import { AlertProps } from '../index';

interface ShowAlertParams {
    title: AlertProps["title"];
    message: AlertProps["message"];
}

export interface UseAlertResult {
    alertProps : AlertProps
    /**
     * alert 나타내기
     * @param title 제목
     * @param message 내용
     */
    showAlert: (params: ShowAlertParams) => void;

}

export const useAlert = () : UseAlertResult => {
    const [open, setOpen] = useState(false);
    const handleOpen = useCallback((_open : boolean) => {
        setOpen(_open);
    }, []);
    const [title, setTitle] = useState('');
    const [message, setMessage] = useState<React.ReactNode>('');
    const showAlert = useCallback((params : ShowAlertParams) => {
        setTitle(params.title);
        setMessage(params.message);
        handleOpen(true);
    }, [handleOpen]);
    return {
        alertProps : {
            open,
            handleOpen,
            title,
            message,
        },
        showAlert,
    }

}