import BoardRegistryForm, { BoardRegistryFormData } from '@/app/board/registry/(ui)/Form';
import React from 'react';


export interface BoardEditFormProps {
    data: BoardRegistryFormData;
    onChange: (data: BoardRegistryFormData) => void;
}
export default function BoardEditForm(
    {
        data,
        onChange
    }: BoardEditFormProps
){
    return <BoardRegistryForm data={data} onChange={onChange}/>
}