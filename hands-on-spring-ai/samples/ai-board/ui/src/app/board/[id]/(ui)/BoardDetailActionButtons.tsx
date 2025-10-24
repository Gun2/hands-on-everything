import { Stack } from '@mui/material';
import BoardListRouteButton from '@/domains/board/components/BoardListRouteButton';
import BoardEditRouteButton from '@/domains/board/components/BoardEditRouteButton';
import { Board } from '@/domains/board/types/board.api.type';
import BoardDeleteButton from '@/domains/board/components/BoardDeleteButton';


export interface BoardDetailActionButtonsProps {
    boardId: Board["id"]
}
export default function BoardDetailActionButtons(
    {
        boardId,
    }: BoardDetailActionButtonsProps
) {
    return (
        <Stack direction="row" gap={1}>
            <BoardListRouteButton/>
            <BoardEditRouteButton boardId={boardId}/>
            <BoardDeleteButton boardId={boardId}/>
        </Stack>
    )
}
