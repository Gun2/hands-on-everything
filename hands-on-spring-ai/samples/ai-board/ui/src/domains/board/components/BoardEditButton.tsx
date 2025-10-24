'use client';
import { Button, ButtonProps } from '@mui/material';
import { useCallback } from 'react';
import { PATHS } from '@/libs/router/paths';
import { useApi } from '@/hooks/useApi';
import { BoardRegistryFormData } from '@/app/board/registry/(ui)/Form';
import { useUpdateBoardMutation } from '../api/board.api.rtkq';
import { Board } from '../types/board.api.type';
import { useRouter } from 'next/navigation';


export interface BoardEditButtonProps extends Omit<ButtonProps, "onClick"> {
    data: BoardRegistryFormData
    boardId: Board["id"]
}
export default function BoardEditButton(
    {
        data,
        boardId,
        children,
        ...props
    }: BoardEditButtonProps
) {
    const router = useRouter();
    const [updateBoardTrigger] = useUpdateBoardMutation();
    const {request} = useApi();
    const onClick = useCallback(() => {
        request(updateBoardTrigger([boardId, {
            title: data.title,
            content: data.content,
        }]).unwrap()).then(result => {
            router.push(PATHS.BOARD_LIST())
        })
    }, [data, boardId]);
    return (
        <Button {...props} onClick={onClick}>
            {
                children ? children : "저장"
            }
        </Button>
    )
}