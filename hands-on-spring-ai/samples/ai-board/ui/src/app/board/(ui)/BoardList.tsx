import React from 'react';
import { Board } from '@/domains/board/types/board.api.type';
import { Skeleton, Stack, styled, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';
import BoardRow from './BoardRow';

export interface BoardListProps {
    data: Board[];
}
export default function BoardList(
    {
        data
    }: BoardListProps
){
    return (
        <Table>
            <TableHead>
                <TableRow>
                    <CustomTableCell>제목</CustomTableCell>
                </TableRow>
            </TableHead>
            <TableBody>
                {
                    data.map((board) => (
                        <BoardRow key={board.id} data={board}/>
                    ))
                }
            </TableBody>
        </Table>
    )
};

const CustomTableCell = styled(TableCell)(({theme}) => ({
    backgroundColor: theme.palette.primary.main,
    color: theme.palette.getContrastText(theme.palette.primary.main),
}))

export function BoardListSkeleton() {
    return (
        <Stack gap={1}>
            <Skeleton/>
            <Skeleton/>
            <Skeleton/>
        </Stack>
    )
}