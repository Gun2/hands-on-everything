import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'
import {Board, BoardCreateRequest, BoardSearchParams, BoardUpdateRequest} from "../../types/board";
import {Page} from "../../types/page";

export const boardApi = createApi({
    reducerPath: 'boardService',
    //브라우저가 포커스를 받으면 refetch
    refetchOnFocus: true,
    //네트워크가 재 연결 되면 refetch
    refetchOnReconnect: true,
    baseQuery: fetchBaseQuery({
        baseUrl: '/boards',
    }),
    endpoints: build => ({
        getBoardById: build.query<Board, Board["id"]>({
            query: (id) => `/${id}`
        }),
        createBoard: build.mutation<Board, BoardCreateRequest>({
            query: (boardRequest) => ({
                url: ``,
                method: 'POST',
                body: boardRequest
            })
        }),
        updateBoard: build.mutation<Board, BoardUpdateRequest>({
            query: (boardRequest) => ({
                url: `/${boardRequest.id}`,
                method: 'PUT',
                body: boardRequest
            })
        }),
        deleteById: build.mutation<Board, Board["id"]>({
            query: (id) => ({
                url: `/${id}`,
                method: 'DELETE'
            })
        }),
        search: build.mutation<Page<Board>, BoardSearchParams>({
            query: ({size = 10, page = 0}: BoardSearchParams) => `?size=${size}&page=${page}`,
        })
    })
});

export const {
    useGetBoardByIdQuery,
    useSearchMutation,
    useCreateBoardMutation,
    useUpdateBoardMutation,
    useDeleteByIdMutation,
} = boardApi