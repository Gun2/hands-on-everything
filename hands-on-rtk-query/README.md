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

# Streaming Updates
RTK는 영속성 쿼리에 대해서 streaming updates를 제공하여 변경되는 정보를 캐시된 데이터에 실시간으로 적용할 수 있음

## 예시
적용하려는 query에 `onCacheEntryAdded` 비동기 함수를 추가하여 적용할 수 있음
> 아래는 게시글 리스트 query에서 게시글의 생성/수정/삭제가 실시간으로 화면에 반영되도록 적용한 예시
```typescript
export const boardApi = createApi({
    reducerPath: 'boardService',
    //...
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
            //STOP를 사용한 websocket 연결을 통해 게시글에 "생성/수정/삭제"가 발생하면 해당 함수가 동작됨
            const changeData = JSON.parse(message.body) as ChangeDataEvent<Board>;
            updateCachedData(draft => {
              switch (changeData.type){
                case 'CREATE':
                  //생성인 경우 정보 추가
                  if (draft.first){
                    draft.content = [changeData.data, ...draft.content.splice(0, draft.size - 1)]
                  }
                  break;
                case 'UPDATE':
                  //수정인 경우 정보 수정
                  draft.content = draft.content.map(d => d.id === changeData.data.id ? changeData.data : d);
                  break;
                case 'DELETE':
                  //삭제인 경우 정보 삭제
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
      }
    })
  })
});

```

# Cache 동작
> 참고 : https://redux-toolkit.js.org/rtk-query/usage/cache-behavior

RTK Query의 핵심 기능 요소는 캐시 데이터를 관리하는 것이다. 서버로부터 데이터를 가져오면 RTK Query는 Redux store에 `cache`로서 데이터를 저장한다. 추가적으로 같은 데이터에 대한 요청이 동작할 때 RTK Query는 서버에 추가적인 요청을 보내지 않고 이미 존재하는 캐시된 데이터를 제공한다.

## 기본적인 Cache 동작
RTK Query의 캐시는 기본적으로 다음과 같은 특징을 가진다.
### API endpoint 정의
- 데이터를 가져오거나 서버와 상호작용하는(mutation과 query)  요청을 endpoint로 설정
- 구독이 시작됐을 때 endpoint와 함께 사용된 파라미터들은 `queryCacheKey`로 내부적으로 직렬화되고 저장된다.
#### queryCacheKey란?
**RTK Query**에서 특정 쿼리 요청을 식별하고, 해당 요청의 캐시 데이터를 저장, 갱신, 또는 재사용할 때 사용하는 고유 키로서 엔드포인트 이름과 직렬화된 쿼리 매개변수를 결합하여 생성됨

### endpoint로부터 컴포넌트들이 데이터를 구동할 때 직렬화된 쿼리 파라미터를 사용
동일한 API 엔드포인트라도 매개변수 값이 다르면 다른 캐시 항목으로 관리

### 활성 구독 참조 카운트
특정 캐시 키에 대한 참조 수를 추적하여 해당 데이터를 얼마나 사용중인지 파악가능
> e.g. 구독 컴포넌트 수가 1개 이상일 경우에만 re-fetching를 수행하는 등에 활용

### 캐시 자동 삭제 조건
- 구독 참조 카운트가 0이고 만료시간(기본 60초)이 지날 경우
- 만료시간 설정 예시 (`keepUnusedDataFor`)
```typescript
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type { Post } from './types'

export const api = createApi({
  baseQuery: fetchBaseQuery({ baseUrl: '/' }),
  // global configuration for the api
  keepUnusedDataFor: 30,
  endpoints: (builder) => ({
    getPosts: builder.query<Post[], number>({
      query: () => `posts`,
      // configuration for an individual endpoint, overriding the api setting
      keepUnusedDataFor: 5,
    }),
  }),
})
```

### re-fetching 유도하기 (`refetchOnMountOrArgChange`)
rtk query를 캐시된 데이터가 아닌 최신 상태로 유지하기 위해 refetchOnMountOrArgChange를 설정할 수 있다. boolean값과 number값을 가질 수 있는데 true로 설정하게 된다면 항상 서버로부터 데이터를 가져오고, 숫자값이 들어오면 해당 초 만큼 캐시를 사용한다.
```typescript
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type { Post } from './types'

export const api = createApi({
  baseQuery: fetchBaseQuery({ baseUrl: '/' }),
  // 30를 넘기면 refetch
  refetchOnMountOrArgChange: 30,
  //항상 refetch
  //refetchOnMountOrArgChange: ture,
  endpoints: (builder) => ({
    getPosts: builder.query<Post[], number>({
      query: () => `posts`,
    }),
  }),
})
```

### window 포커싱 시 re-fetch(`refetchOnFocus`)
브라우저의 window 포커싱 시 re-fetch가 발생하도록 설정할 수 있다.
```typescript
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type { Post } from './types'

export const api = createApi({
  baseQuery: fetchBaseQuery({ baseUrl: '/' }),
  // global configuration for the api
  refetchOnFocus: true,
  endpoints: (builder) => ({
    getPosts: builder.query<Post[], number>({
      query: () => `posts`,
    }),
  }),
})
```

### 네트워크 재 연결 시 re-fetch(`refetchOnReconnect`)
네트워크가 재 열결 될 경우 re-fetch가 발생하도록 설정할 수 있다.
```typescript
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type { Post } from './types'

export const api = createApi({
  baseQuery: fetchBaseQuery({ baseUrl: '/' }),
  // global configuration for the api
  refetchOnReconnect: true,
  endpoints: (builder) => ({
    getPosts: builder.query<Post[], number>({
      query: () => `posts`,
    }),
  }),
})
```

## Automated Re-fetching
RTK Query는 “cache tag”시스템을 사용하여 변경된 데이터들에 대해서 자동으로 re-fetching을 할 수 있다.

### Tags
re-fetching 목적으로 특정 데이터의 캐시 무효화 또는 캐시 제어를 위해  수집된 데이터에 부여될 수 있는 이름값이다.
Tag들은 `tagTypes`설정 값으로 api 정의 시에 설정할 수 있다.
> 문자열 또는 {type: string, id?: string|number} 타입의 배열을 가질 수 있다.
```typescript
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query'
import type { Post, User } from './types'

const api = createApi({
  baseQuery: fetchBaseQuery({
    baseUrl: '/',
  }),
  tagTypes: ['Post', 'User'],
  endpoints: (build) => ({
    getPosts: build.query<Post[], void>({
      query: () => '/posts',
      //type + id 타입의 태그 선언
      providesTags: (result, error, arg) =>
        result
          ? [...result.map(({ id }) => ({ type: 'Post' as const, id })), 'Post']
          : ['Post'],
    }),
    getUsers: build.query<User[], void>({
      query: () => '/users',
      //string타입의 태그 선언
      providesTags: ['User'],
    }),
  }),
})
```
> 위의 코드는 posts 또는 users 정보 조회 시 각 캐시 데이터에 ‘Post’, ‘User’ 태그 명을 부여한다.

### Providing cache data
각각의 query endpoint는 `providesTags`값 설정을 통해 캐시 데이터에 특정 tag들을 제공할 수 있다. 
> 서로 다른 endpoint 끼리는 tag가 공유되지 않는다.
> <br/> 즉 중복된 tag 이름을 여러 endpoint에서 사용중일 때 특정 endpoint의 tag를 무효화 시키면 해당 endpoint의 tag와 관련된 캐시 데이터만 무효화 되고 나머지 endpoint는 tag 이름이 같을지라도 영향을 받지 않는다.

```typescript
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query'
import type { Post, User } from './types'

const api = createApi({
  baseQuery: fetchBaseQuery({
    baseUrl: '/',
  }),
  tagTypes: ['Post', 'User'],
  endpoints: (build) => ({
    getPosts: build.query<Post[], void>({
      query: () => '/posts',
      //type + id 타입의 태그 선언
      providesTags: (result, error, arg) =>
        result
          ? [...result.map(({ id }) => ({ type: 'Post' as const, id })), 'Post']
          : ['Post'],
    }),
    getUsers: build.query<User[], void>({
      query: () => '/users',
      //string타입의 태그 선언
      providesTags: ['User'],
    }),
  }),
})
```
> 위의 코드는 posts 또는 users 정보 조회 시 각 캐시 데이터에 ‘Post’, ‘User’ 태그 명을 부여한다. 

### Invalidating cache data
각각의 mutation endpoint는 `invalidatesTags`값 설정을 통해 캐시 데이터가 존재하는 특정 태그를 무효화 시킬 수 있다.
```typescript
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query'
import type { Post, User } from './types'

const api = createApi({
  baseQuery: fetchBaseQuery({
    baseUrl: '/',
  }),
  tagTypes: ['Post', 'User'],
  endpoints: (build) => ({
    //...
    addPost: build.mutation<Post, Omit<Post, 'id'>>({
      query: (body) => ({
        url: 'post',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['Post'],
    }),
    editPost: build.mutation<Post, Partial<Post> & Pick<Post, 'id'>>({
      query: (body) => ({
        url: `post/${body.id}`,
        method: 'POST',
        body,
      }),
      invalidatesTags: ['Post'],
    }),
  }),
})
```
> 위의 코드는 Post 정보의 Add/Edit 시 ‘Post’태그를 무효화 시키게 된다. ‘Post’ 태그가 무효화 되고 해당 태그가 존재하는 cache data를 구독중인 컴포넌트가 존재할 경우 정보가 자동으로 re-fetching 된다.

### paging에 적용
페이징 데이터에는 수많은 row에 해당하는 data들이 데이터로 존재할 수 있으며 `row들 중 하나 이상의 값이 서버에서 변경되었다면` 해당 페이징 데이터는 re-fetching하여 `최신 데이터로 갱신 할 필요`가 생긴다.
RTK Query에서는 페이징 쿼리에서 가져온 data들의 정보를 `providesTags`를 통해 `tag` 이름을 명시할 수 있으며, 값이 변경될 경우 `invalidatesTags`를 통해 해당 `tag`를 무효화 시켜 re-fetching을 유도할 수 있다.

```typescript
export const boardApi = createApi({
  reducerPath: 'boardService',
  //...
  baseQuery: fetchBaseQuery({
    baseUrl: '/boards',
  }),
  tagTypes: ["Boards"],
  endpoints: build => ({
    //...
    deleteById: build.mutation<Board, Board["id"]>({
      query: (id) => ({
        url: `/${id}`,
        method: 'DELETE'
      }),
      //삭제된 데이터의 태그 무효화
      invalidatesTags: (result, error, id) => [
        { type: 'Boards', id },
        { type: 'Boards', id: 'PARTIAL-LIST' },
      ],
    }),
    search: build.query<Page<Board>, BoardSearchParams>({
      query: ({size = 10, page = 0}: BoardSearchParams) => `?size=${size}&page=${page}`,
      //...
      //각 데이터값을 활용하여 태그 이름 명시
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

```