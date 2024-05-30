import {configureStore} from '@reduxjs/toolkit'
import {boardApi} from "./services/boardService";

export const store = configureStore({
    reducer: {
        [boardApi.reducerPath] : boardApi.reducer,
    },
    middleware: getDefaultMiddleware => getDefaultMiddleware().concat(boardApi.middleware)
})

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch