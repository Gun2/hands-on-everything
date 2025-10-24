import { Button, ButtonProps } from '@mui/material';
import { PATHS } from '@/libs/router/paths';
import { useApi } from '@/hooks/useApi';
import { BoardRegistryFormData } from '@/app/board/registry/(ui)/Form';
import { useCreateBoardMutation } from '../api/board.api.rtkq';
import { useRouter } from 'next/navigation';


export interface BoardSaveButtonProps extends Omit<ButtonProps, "onClick"> {
    data: BoardRegistryFormData
}
export default function BoardSaveButton(
    {
        data,
        children,
        ...props
    }: BoardSaveButtonProps
) {
    const router = useRouter();
    const [createBoardTrigger] = useCreateBoardMutation();
    const {request} = useApi();
    const onClick = () => {
        request(createBoardTrigger([{
            title: data.title,
            content: data.content,
        }]).unwrap()).then(result => {
            router.push(PATHS.BOARD_LIST())
        })
    }
    return (
        <Button {...props} onClick={onClick}>
            {
                children ? children : "저장"
            }
        </Button>
    )
}