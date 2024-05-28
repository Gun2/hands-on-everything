import {useCallback, useEffect, useState} from "react";
import {Pagination} from "../components/ui/DataTable";

type HandleArg<T> = {rows : T[], pagination : Pagination};
type Handle<T> = ({rows, pagination}: HandleArg<T>) => void;

export type UseDataTable<T> = {
    page?: number;
    size?: number;
    paging: (page: number, size: number, handle: Handle<T>) => void
}
export function useDataTable<T>(
    {
        page = 0,
        size = 10,
        paging,
    }: UseDataTable<T>
){
    const [rows, setRows] = useState<T[]>();
    const [pagination, setPagination] = useState<Pagination>({
        totalElements: 0,
        totalPages: 0,
        pageable: {
            pageSize: size,
            pageNumber: page
        },
    });

    const handle = useCallback(({rows, pagination}:HandleArg<T>) => {
        setRows(rows);
        setPagination(pagination);
    }, [])

    const search = useCallback((page: number) => {
        paging(page, size, handle);
    }, [paging, size, handle])

    useEffect(() => {
        search(page);
    }, [])

    return {
        search,
        rows,
        pagination
    }
}