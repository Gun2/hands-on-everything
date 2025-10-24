import { EndpointBuilder } from '@reduxjs/toolkit/query';
import { BaseQueryFn } from '@reduxjs/toolkit/query/react';

/**
 * rtk build
 */
export type Build<TAG extends  string, A extends string> = EndpointBuilder<BaseQueryFn<{
    apiMethod: (...args: any) => Promise<any>
    args: any[]
}, unknown, unknown, {}, {}>, TAG, A>