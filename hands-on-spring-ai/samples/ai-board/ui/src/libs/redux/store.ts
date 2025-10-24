import { configureStore } from '@reduxjs/toolkit';
import { boardRtkqApi } from '../../domains/board/api/board.api.rtkq';

export const store = configureStore({
    reducer: {
        [boardRtkqApi.reducerPath] : boardRtkqApi.reducer,
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware({
        serializableCheck: false,
    }).concat(
        boardRtkqApi.middleware
    )
})

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch