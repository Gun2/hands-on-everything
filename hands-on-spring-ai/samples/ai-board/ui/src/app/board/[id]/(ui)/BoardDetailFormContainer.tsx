'use client';
import { Board } from '@/domains/board/types/board.api.type';
import { useFindBoardByIdQuery } from '@/domains/board/api/board.api.rtkq';
import React from 'react';
import BoardDetailForm, { BoardDetailFormSkeleton } from './BoardDetailForm';

export interface BoardDetailFormContainerProps {
    id: Board["id"];
}

export default function BoardDetailFormContainer(
    {
        id
    }: BoardDetailFormContainerProps
){
    const {data, isLoading, isError} = useFindBoardByIdQuery([id]);
    if (isLoading){
        return (
            <BoardDetailFormSkeleton/>
        )
    }
    if (!data || isError){
        return (<div>정보 호출에 문제가 발생하였습니다.</div>)
    }
    return (
        <BoardDetailForm data={data.data}/>
    )
}