import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'
import {Board, BoardCreateRequest, BoardSearchParams, BoardUpdateRequest} from "../../types/board";
import {Page} from "../../types/page";
import { WebSocketClientFactory } from '../../utils/webSocketClientFactory';
import { ChangeDataEvent } from '../../types/changeData';

export const boardApi = createApi({
    reducerPath: 'boardService',
    //브라우저가 포커스를 받으면 refetch
    refetchOnFocus: true,
    //네트워크가 재 연결 되면 refetch
    refetchOnReconnect: true,
    baseQuery: fetchBaseQuery({
        baseUrl: '/boards',
    }),
    tagTypes: ["Boards"],
    endpoints: build => ({
        getBoardById: build.query<Board, Board["id"]>({
            query: (id) => `/${id}`,
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
            }),
            invalidatesTags: (result, error, id) => [
                { type: 'Boards', id },
                { type: 'Boards', id: 'PARTIAL-LIST' },
            ],
        }),
        search: build.query<Page<Board>, BoardSearchParams>({
            query: ({size = 10, page = 0}: BoardSearchParams) => `?size=${size}&page=${page}`,
            async onCacheEntryAdded(
              arg,
              { updateCachedData, cacheDataLoaded, cacheEntryRemoved },
            ){
                const instance = WebSocketClientFactory.getInstance();
                const client = await instance.getClient();

                try {
                    // 초기 데이터가 로드될 때까지 대기
                    await cacheDataLoaded;

                    client.subscribe("/topic/board", (message) => {
                        const changeData = JSON.parse(message.body) as ChangeDataEvent<Board>;
                        updateCachedData(draft => {
                            switch (changeData.type){
                                case 'CREATE':
                                    if (draft.first){
                                        draft.content = [changeData.data, ...draft.content.splice(0, draft.size - 1)]
                                    }
                                    break;
                                case 'UPDATE':
                                    draft.content = draft.content.map(d => d.id === changeData.data.id ? changeData.data : d);
                                    break;
                                case 'DELETE':
                                    draft.content = draft.content.filter(d => d.id !== changeData.data.id);
                                    break;
                            }
                        })

                    });
                } catch (e) {
                    console.error(e);
                }

                // 캐시 항목이 제거될 때까지 대기
                await cacheEntryRemoved;
            },
            providesTags: (result, error, page) =>
              result
                ? [
                    ...result.content.map(({ id }) => ({ type: 'Boards' as const, id })),
                    { type: 'Boards', id: 'PARTIAL-LIST' },
                ]
                : [{ type: 'Boards', id: 'PARTIAL-LIST' }],
        })
    })
});

export const {
    useGetBoardByIdQuery,
    useLazySearchQuery,
    useCreateBoardMutation,
    useUpdateBoardMutation,
    useDeleteByIdMutation,
} = boardApi