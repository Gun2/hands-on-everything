import { createApi } from '@reduxjs/toolkit/query/react';
import { axiosBaseQuery } from '../../../libs/redux/querys/axiosBaseQuery';
import { boardApi } from './board.api';
import CreateEndpointUtil from '@/libs/redux/querys/createEndpointUtil';


const reducerPath = 'boardRtkqApi';
const TAG = "Board";
const LIST_TAG_ID = "LIST";
export const boardRtkqApi = createApi({
    reducerPath: reducerPath,
    baseQuery: axiosBaseQuery,
    tagTypes: [TAG],
    endpoints: (build) => ({
        createBoard: CreateEndpointUtil.createMutation(boardApi.create, reducerPath, TAG, (result, error, args, meta) => {
            return [
                {
                    type: TAG,
                    id: LIST_TAG_ID
                }
            ]
        })(build),
        updateBoard: CreateEndpointUtil.createMutation(boardApi.update, reducerPath, TAG, (result, error, args, meta) => {
            return [
                {
                    type: TAG,
                    id: args[0]
                },
                {
                    type: TAG,
                    id: LIST_TAG_ID
                }
            ]
        })(build),
        deleteBoard: CreateEndpointUtil.createMutation(boardApi.delete, reducerPath, TAG, (result, error, args, meta) => {
            return [
                {
                    type: TAG,
                    id: args[0]
                },
                {
                    type: TAG,
                    id: LIST_TAG_ID
                }
            ]
        })(build),
        findBoardById: CreateEndpointUtil.createQuery(boardApi.getById, reducerPath, TAG, (result, error, args, meta) => {
            return [
                {
                    type: TAG,
                    id: args[0]
                }
            ]
        })(build),
        findBoardAll: CreateEndpointUtil.createQuery(boardApi.getAll, reducerPath, TAG, (result, error, args, meta) => {
            return [
                {
                    type: TAG,
                    id: LIST_TAG_ID
                }
            ]
        })(build),
    }),
})

export const {
    useCreateBoardMutation,
    useUpdateBoardMutation,
    useDeleteBoardMutation,
    useFindBoardByIdQuery,
    useLazyFindBoardByIdQuery,
    useFindBoardAllQuery,
} = boardRtkqApi;
