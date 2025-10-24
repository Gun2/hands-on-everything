'use client';
import { useState } from 'react';
import { BoardRegistryFormData, createInitialData } from '../(ui)/Form';

export interface UseBoardRegistryFormParams {
    //초기값
    initData ?: BoardRegistryFormData;
}
export interface UseBoardRegistryFormResult {
    data: BoardRegistryFormData;
    onChange: (data: BoardRegistryFormData) => void;
}
export const useBoardRegistryForm = (
    {
        initData
    }: UseBoardRegistryFormParams = {}
): UseBoardRegistryFormResult => {
    const [data, setData] = useState<BoardRegistryFormData>(initData ?? createInitialData());
    return {
        data,
        onChange: setData
    }

}