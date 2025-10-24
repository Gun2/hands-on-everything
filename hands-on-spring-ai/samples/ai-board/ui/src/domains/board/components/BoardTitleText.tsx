import { Board } from '@/domains/board/types/board.api.type';
import { PATHS } from '@/libs/router/paths';
import { styled } from '@mui/material';
import { useRouter } from 'next/navigation';

export interface BoardTitleTextProps {
    title: Board["title"];
    id: Board["id"];
}

export interface BoardTitleTextProps {
    title: string;
    id: number;
}

export default function BoardTitleText(
    {
        title,
        id
    }: BoardTitleTextProps
){
    const router = useRouter();
    const onClick = () => {
        router.push(PATHS.BOARD_DETAIL(id));
    }
    return (
        <Text onClick={onClick}>
            {title}
        </Text>
    )
}

const Text = styled("div")((props) => ({
    color: props.theme.palette.primary.main,
    cursor: "pointer",
}))