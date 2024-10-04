# hands-on-rtk-query
RTK Query 핸즈온 프로젝트

# RTK Query란
데이터 조회, 캐싱을 지원하는 tool로서 RTK 패키지에 포함되어 있음
> `@reduxjs/toolkit`에 포함됨
# 프로젝트 구조
```shell
├── server #백엔드 API 서버
└── web #RTK Query를 사용한 웹 UI
```

# 백엔드 샘플 API 항목
RTK Query를 통해 호출할 API 샘플

| method | path                           | 용도 |
|--------|--------------------------------|----|
| GET    | boards/{id}                    | 조회 |
| POST   | boards                         | 생성 |
| PUT    | boards/{id}                    | 수정 |
| DELETE | boards/{id}                    | 삭제 |
| GET    | boards?page={page}&size={size} | 검색 |

# RTK Query Preparing
RTK Query 사전준비
## RTK 설치
```shell
npm install @reduxjs/toolkit react-redux
```

## Store 생성
Redux의 Store 생성
### src/redux/store.ts
```typescript
import {configureStore} from '@reduxjs/toolkit'

export const store = configureStore({
    reducer: {
        ...,
    },
})
// 타입 스크립트에서 redux 관련 타입을 사용하기 위해 경우 하단의 타입을 정의
// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch
```

## Redux Store 사용
Provider 컴포넌트를 통해 리액트 컴포넌트에 `Redux Store`를 제공

### src/index.tsx
```tsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import {store} from './redux/store'
import {Provider} from 'react-redux'

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
      <Provider store={store}>
          ...
      </Provider>
  </React.StrictMode>
);
```

# API Slice 생성
RTK Query를 통해 API를 호출할 수 있는 Slice를 생성
### src/redux/services/boardService.ts
```ts
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
    // endpoints에 api 정의
    endpoints: build => ({
        /**
         * 조회 API의 경우 query 함수를 통해 엔드포인트를 정의하며
         * 제네릭 첫번 째는 반환 타입, 두 번째는 hook을 통해 쿼리에 전달할 값의 타입
         */
        getBoardById: build.query<Board, Board["id"]>({
            query: (id) => `/${id}`
        }),
        /**
         * 데이터의 변경과 관련된 API의 경우 mutation함수를 통해 엔드포인트를 정의하며
         * 제네릭 첫번 째는 반환 타입, 두 번째는 hook을 통해 쿼리에 전달할 값의 타입
         */
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
/**
 * @reduxjs/toolkit/query/react로 createApi를 import한 경우
 * endpoint에 정의된 이름으로 hook이 자동 생성됨
 */
export const {
    useGetBoardByIdQuery,
    useSearchMutation,
    useCreateBoardMutation,
    useUpdateBoardMutation,
    useDeleteByIdMutation,
} = boardApi
```

## Query Hook
조회 타입의 API에 사용되는 RTK Query Hook 유형 
<br/>endpoints에서 query를 통해 정의한 경우 아래 유형으로 네이밍된 hook을 사용할 수 있음
### useQuery
컴포넌트 렌더링 되면 자동으로 서버에서 데이터를 요청하고, 요청된 데이터를 Redux store에 캐시하며, 캐시된 데이터를 컴포넌트에서 사용할 수 있음
```tsx
const {data, error, isLoading} = useQuery(id);
```

### useLazyQuery
`useQuery`와 유사하나 자동으로 데이터를 요청하지 않고 trigger함수가 호출되어야함

```tsx
const [trigger, {data, isLoading}] = useLazySearchQuery();
return (
    <button onClick={() => trigger(id)}>
        Fetch
    </button>
);
```

## Mutation Hook
데이터의 변경사항을 전달하는 API(수정, 생성, 삭제)에 사용되는 RTK Query Hook 유형
<br/>정보를 전달하고 변경 사항을 로컬 케시에 반영함
### useMutation
hook이 반환하는 첫번 째 값에 트리거 함수가 있고, 두번 째 값에 결과값이 있음 
```tsx
const [trigger, result] = useMutation();

return (
    <button onClick={() => trigger(updateData)}>
        Fetch
    </button>
);
```

# API Slice 적용
### src/redux/store.ts
```ts
import {configureStore} from '@reduxjs/toolkit'
import {boardApi} from "./services/boardService";

export const store = configureStore({
    reducer: {
        [boardApi.reducerPath] : boardApi.reducer,
    },
    middleware: getDefaultMiddleware => getDefaultMiddleware().concat(boardApi.middleware)
})
```