import { Button } from '@mui/material';
import { PATHS } from '@/libs/router/paths';
import { useRouter } from 'next/navigation';

export default function BoardRegistryRouteButton(){
    const router = useRouter();
    const onClick = () => {
        router.push(PATHS.BOARD_REGISTRY())
    }
    return (
        <Button onClick={onClick}>게시글 등록</Button>
    )
}