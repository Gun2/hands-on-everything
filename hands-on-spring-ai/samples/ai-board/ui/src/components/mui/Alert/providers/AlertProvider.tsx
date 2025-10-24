import React, { createContext, useCallback, useState } from 'react';
import Alert, { AlertProps } from '../index';
import { createUUID } from '@/utils/createUUID';

interface ShowAlertParams {
    title: AlertProps["title"];
    message: AlertProps["message"];
}

export interface AlertContextValue {
    showAlert: (params : ShowAlertParams) => void;
};

export const AlertContext = createContext<AlertContextValue | undefined>(undefined);


export interface AlertProviderProps{
    children:React.ReactNode;
}

interface AlertInfo extends ShowAlertParams {
    uuid: string;
}

export default function AlertProvider(
    {
        children,
    }: AlertProviderProps
){
    const [alertInfoList, setAlertInfoList] = useState<AlertInfo[]>([])
    const showAlert = useCallback((params: ShowAlertParams) => {
        setAlertInfoList(prevState => [...prevState, {...params,  uuid: createUUID()}])
    }, []);
    const removeAlertInfo = useCallback((uuid: AlertInfo["uuid"]) => {
        setAlertInfoList(prevState => prevState.filter(prev => prev.uuid !== uuid))
    }, []);
    return (
        <AlertContext.Provider value={{
            showAlert
        }}>
            {
                alertInfoList.map(alertInfo => (
                    <Alert
                        key={alertInfo.uuid}
                        open={true}
                        handleOpen={() => removeAlertInfo(alertInfo.uuid)}
                        title={alertInfo.title}
                        message={alertInfo.message}
                    />
                ))
            }
            {children}
        </AlertContext.Provider>
    )
}