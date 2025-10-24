import { Stack } from '@mui/material';
import BoardListRouteButton from '@/domains/board/components/BoardListRouteButton';
import BoardEditButton from '@/domains/board/components/BoardEditButton';
import { BoardRegistryFormData } from '../../../registry/(ui)/Form';
import { Board } from '@/domains/board/types/board.api.type';

export interface BoardEditActionButtonsProps {
    data: BoardRegistryFormData;
    boardId: Board["id"]
}

export default function BoardEditActionButtons(
    {
        boardId,
        data,
    }: BoardEditActionButtonsProps
){
    return (
        <Stack direction="row" gap={1}>
            <BoardListRouteButton/>
            <BoardEditButton boardId={boardId} data={data} />
        </Stack>
    )
}