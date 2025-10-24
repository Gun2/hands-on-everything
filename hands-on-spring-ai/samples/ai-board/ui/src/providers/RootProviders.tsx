'use client';
import React from 'react';
import { store } from '@/libs/redux/store';
import ConfirmProvider from '../components/mui/Confirm/providers/ConfirmProvider';
import AlertProvider from '../components/mui/Alert/providers/AlertProvider';
import { Provider } from 'react-redux';
import { AppRouterCacheProvider } from '@mui/material-nextjs/v13-appRouter';


export interface AppProvidersProps {
    children: React.ReactNode;
}

/**
 * App root에 적용할 provider 모음
 * @param children
 * @constructor
 */
export default function RootProviders(
    {
        children
    }: AppProvidersProps
){
    return (
        <Provider store={store}>
          <AppRouterCacheProvider>
            <AlertProvider>
                <ConfirmProvider>
                    {children}
                </ConfirmProvider>
            </AlertProvider>
          </AppRouterCacheProvider>
        </Provider>

    )
}