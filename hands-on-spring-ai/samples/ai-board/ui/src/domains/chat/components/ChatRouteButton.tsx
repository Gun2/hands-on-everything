'use client';
import React, { useCallback } from 'react';
import { PATHS } from '@/libs/router/paths';
import { Button, ButtonProps } from '@mui/material';
import { useRouter } from 'next/navigation';

export type ChatRouteButtonProps = Omit<ButtonProps, "onClick">

/**
 * 채팅 페이지로 이동
 * @param children
 * @constructor
 */
export default function ChatRouteButton(
    {
        children
    }: ChatRouteButtonProps
) {
    const navigate = useRouter();
    const goToChat = useCallback(() => {
        navigate.push(PATHS.CHAT());
    }, [navigate]);
    return (
        <Button
            onClick={goToChat}
        >{children ?? "채팅"}</Button>
    )
}