import React from 'react';
import { TableCell, TableRow } from '@mui/material';
import BoardTitleText from '@/domains/board/components/BoardTitleText';

export interface BoardRowData {
    id: number;
    title: string;
    content: string;
}
export interface BoardRowProps {
    data: BoardRowData;
}
export default function BoardRow(
    {
        data
    }: BoardRowProps
){
    return (
        <TableRow>
            <TableCell><BoardTitleText title={data.title} id={data.id}/></TableCell>
        </TableRow>
    )
};