import React, { createContext, useCallback, useState } from 'react';
import Confirm, { ConfirmProps } from '../index';
import { createUUID } from '@/utils/createUUID';

interface ShowConfirmParams {
    title: ConfirmProps["title"];
    message: ConfirmProps["message"];
    onClickConfirm: ConfirmProps["onClickConfirm"];
}

export interface ConfirmContextValue {
    showConfirm: (params : ShowConfirmParams) => void;
};

export const ConfirmContext = createContext<ConfirmContextValue | undefined>(undefined);


export interface ConfirmProviderProps {
    children:React.ReactNode;
}

interface ConfirmInfo extends ShowConfirmParams {
    uuid: string;
}

export default function ConfirmProvider(
    {
        children,
    }: ConfirmProviderProps
){
    const [confirmInfoList, setConfirmInfoList] = useState<ConfirmInfo[]>([])
    const showConfirm = useCallback((params: ShowConfirmParams) => {
        setConfirmInfoList(prevState => [...prevState, {...params,  uuid: createUUID()}])
    }, []);
    const removeAlertInfo = useCallback((uuid: ConfirmInfo["uuid"]) => {
        setConfirmInfoList(prevState => prevState.filter(prev => prev.uuid !== uuid))
    }, []);
    return (
        <ConfirmContext.Provider value={{
            showConfirm
        }}>
            {
                confirmInfoList.map(confirmInfo => (
                    <Confirm
                        open={true}
                        handleOpen={() => removeAlertInfo(confirmInfo.uuid)}
                        title={confirmInfo.title}
                        message={confirmInfo.message}
                        onClickConfirm={confirmInfo.onClickConfirm}
                    />
                ))
            }
            {children}
        </ConfirmContext.Provider>
    )
}