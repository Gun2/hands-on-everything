'use client';
import React, { useCallback } from 'react';
import { PATHS } from '@/libs/router/paths';
import { Button, ButtonProps } from '@mui/material';
import { useRouter } from 'next/navigation';

export type BoardListButtonProps = Omit<ButtonProps, "onClick">

/**
 * 게시글 목록으로 이동
 * @param children
 * @constructor
 */
export default function BoardListRouteButton(
    {
        children
    }: BoardListButtonProps
) {
    const navigate = useRouter();
    const goToBoardList = useCallback(() => {
        navigate.push(PATHS.BOARD_LIST());
    }, [navigate]);
    return (
        <Button
            onClick={goToBoardList}
        >{children ?? "목록"}</Button>
    )
}