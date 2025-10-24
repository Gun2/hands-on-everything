import { Stack } from '@mui/material';
import BoardListRouteButton from '@/domains/board/components/BoardListRouteButton';
import BoardSaveButton from '@/domains/board/components/BoardSaveButton';
import { BoardRegistryFormData } from './Form';


export interface ActionButtonsProps {
    //저장할 데이터
    data: BoardRegistryFormData;
}
export default function ActionButtons(
    {
        data,
    }: ActionButtonsProps
) {
    return (
        <Stack direction="row" gap={1}>
            <BoardListRouteButton/>
            <BoardSaveButton data={data}/>
        </Stack>
    )
}