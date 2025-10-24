'use client';
import FormLayout from '@/app/board/(ui)/FormLayout';
import BoardRegistryForm from './Form';
import { useBoardRegistryForm } from '@/app/board/registry/(hooks)/useBoardRegistryForm';
import ActionButtons from './ActionButtons';

export default function BoardRegistryFormLayout(
    {

    }
){
    const {data, onChange} = useBoardRegistryForm();

    return (
        <FormLayout
            titleArea={"게시글 등록"}
            contentArea={<BoardRegistryForm data={data} onChange={onChange}/>}
            actionArea={<ActionButtons data={data}/>}
        />
    )
}