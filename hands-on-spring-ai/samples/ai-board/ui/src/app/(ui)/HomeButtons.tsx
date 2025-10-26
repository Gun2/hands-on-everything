import React from 'react';
import BoardListRouteButton from '../../domains/board/components/BoardListRouteButton';
import { Stack } from '@mui/material';
import ChatRouteButton from '@/domains/chat/components/ChatRouteButton';


/**
 * Home에 위치한 버튼들
 * @constructor
 */
export default function HomeButtons(){
    return (
        <Stack direction={"row"} gap={1}>
            <BoardListRouteButton>게시판</BoardListRouteButton>
            <ChatRouteButton>채팅</ChatRouteButton>
        </Stack>
    )
};