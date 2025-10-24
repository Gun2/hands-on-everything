'use client';
import React, { useCallback } from 'react';
import { PATHS } from '@/libs/router/paths';
import { Button, ButtonProps } from '@mui/material';
import { Board } from '../types/board.api.type';
import { useRouter } from 'next/navigation';

export interface BoardEditRouteProps extends Omit<ButtonProps, "onClick"> {
    boardId: Board["id"]
}

/**
 * 게시글 수정으로 이동
 * @param children
 * @constructor
 */
export default function BoardEditRouteButton(
    {
        boardId,
        children
    }: BoardEditRouteProps
) {
    const router = useRouter();
    const goToBoardEdit = useCallback(() => {
        router.push(PATHS.BOARD_EDIT(boardId))
    }, [boardId]);
    return (
        <Button
            onClick={goToBoardEdit}
        >{children ?? "수정"}</Button>
    )
}