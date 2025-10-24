'use client';
import { Button, ButtonProps } from '@mui/material';
import { useCallback } from 'react';
import { PATHS } from '@/libs/router/paths';
import { useApi } from '@/hooks/useApi';
import { useDeleteBoardMutation } from '../api/board.api.rtkq';
import { Board } from '../types/board.api.type';
import { useRouter } from 'next/navigation';


export interface BoardDeleteButtonProps extends Omit<ButtonProps, "onClick"> {
    boardId: Board["id"]
}
export default function BoardDeleteButton(
    {
        boardId,
        children,
        ...props
    }: BoardDeleteButtonProps
) {
    const router = useRouter();
    const [deleteBoardTrigger] = useDeleteBoardMutation();
    const {request} = useApi();
    const onClick = useCallback(() => {
        request(deleteBoardTrigger([boardId]).unwrap()).then(result => {
            router.push(PATHS.BOARD_LIST())
        })
    }, [boardId]);
    return (
        <Button {...props} onClick={onClick}>
            {
                children ? children : "삭제"
            }
        </Button>
    )
}