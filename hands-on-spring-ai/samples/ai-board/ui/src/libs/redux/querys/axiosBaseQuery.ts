import { BaseQueryFn } from '@reduxjs/toolkit/query/react';
import { ApiMethodType } from './types/apiMethod.type';

/**
 * axios를 재사용하기 위한 base query
 * @param apiMethod
 * @param args
 */
export const axiosBaseQuery: BaseQueryFn<
    {
        apiMethod: ApiMethodType;
        args: any[]
    },
    unknown,
    unknown
> = async ({ apiMethod, args }) => {
    try {
        const data = await apiMethod(...args);
        return { data };
    } catch (error) {
        return { error };
    }
};