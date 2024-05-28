import React from 'react';
import {Pagination, Stack, Table, TableBody, TableCell, TableHead, TableRow} from "@mui/material";

type Column<T> = {
    name: React.ReactNode;
    selector : (row : T) => React.ReactNode;
}

type Scheme<T> = {
    id : (row : T) => any;
    columns : Column<T>[]
}

type DataTableProps<T> = {
    scheme: Scheme<T>;
    rows: T[];
    pagination: Pagination;
    search: (page: number) => void;
}

export type Pagination = {
    totalElements: number;
    totalPages: number;
    pageable: {
        pageNumber: number;
        pageSize: number;
    }
}

export function DataTable<T>(
    {
        scheme,
        rows,
        pagination,
        search,
    }: DataTableProps<T>
) {
    return (
        <Stack alignItems={"center"} spacing={2}>
            <Table>
                <TableHead>
                    {
                        scheme.columns.map((column: Column<T>, index) => (
                            <TableCell>{column.name}</TableCell>
                        ))
                    }
                </TableHead>
                <TableBody>
                    {
                        rows.length == 0 ? (
                            <TableRow>
                                <TableCell
                                    colSpan={scheme.columns.length}
                                    style={{textAlign: "center"}}
                                >
                                    no data
                                </TableCell>
                            </TableRow>
                        ): (
                            rows.map(row => (
                                <TableRow>
                                    {
                                        scheme.columns.map((column: Column<T>) => (
                                            <TableCell>{column.selector(row)}</TableCell>
                                        ))
                                    }
                                </TableRow>
                            ))
                        )
                    }
                </TableBody>
            </Table>
            <Pagination
                count={pagination.totalPages}
                onChange={(event, page) => search(page-1)}
            />
        </Stack>
    );
}