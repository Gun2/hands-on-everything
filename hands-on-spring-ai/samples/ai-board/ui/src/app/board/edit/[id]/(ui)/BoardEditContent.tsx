'use client';
import BoardRegistryForm from '@/app/board/registry/(ui)/Form';
import FormLayout from '@/app/board/(ui)/FormLayout';
import { useBoardRegistryForm } from '@/app/board/registry/(hooks)/useBoardRegistryForm';
import BoardEditActionButtons from './BoardEditActionButtons';
import { Board } from '@/domains/board/types/board.api.type';

export interface BoardEditProps {
    initData: Board;
}
export default function BoardEditContent(
    {
        initData
    }: BoardEditProps
) {
    const {data, onChange} = useBoardRegistryForm({
        initData
    });
    return (
        <FormLayout
            titleArea={"게시글 수정"}
            contentArea={<BoardRegistryForm data={data} onChange={onChange}/>}
            actionArea={<BoardEditActionButtons data={data} boardId={initData.id}/>}
        />
    )
}