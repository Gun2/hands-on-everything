import React from 'react';
import { useFindBoardAllQuery } from '@/domains/board/api/board.api.rtkq';
import BoardList, { BoardListSkeleton } from './BoardList';


/**
 * 게시글 API 조회 후 출력 컴포넌트
 * @constructor
 */
export default function BoardListContainer(){
    const {data, isLoading, isError} = useFindBoardAllQuery([]);
    if (isLoading){
        return (
            <BoardListSkeleton/>
        )
    }
    if (!data || isError){
        return (<div>정보 호출에 문제가 발생하였습니다.</div>)
    }
    return (
        <BoardList data={data.data}/>
    )
};