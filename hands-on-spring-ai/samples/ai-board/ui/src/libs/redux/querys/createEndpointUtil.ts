// mutation 생성 헬퍼
import { ApiMethodType } from './types/apiMethod.type';
import { Build } from './types/axiosBaseBuild.type';

export type TagsFunction<T extends ApiMethodType> = (
    result: Awaited<ReturnType<T>> | undefined,
    error: unknown,
    args: Parameters<T>,
    meta: {} | undefined
) => Array<any>;
/**
 * axios를 호출하는 mutaion 생성
 * @param apiMethod axios 호출 메서드
 * @param reducerPath reducerPath
 */
const createMutation = <T extends ApiMethodType>(apiMethod: T, reducerPath : string, tag: string, invalidatesTags?: TagsFunction<T>) =>
    (build: Build<typeof tag, typeof reducerPath>) => build.mutation<Awaited<ReturnType<T>>, Parameters<T>>({
        query: (args: Parameters<T>) => ({
            apiMethod,
            args,
        }),
        invalidatesTags: invalidatesTags
    });


/**
 * axios를 호출하는 query 생성
 * @param apiMethod axios 호출 메서드
 * @param reducerPath reducerPath
 */
const createQuery = <T extends ApiMethodType>(apiMethod: T, reducerPath : string, tag: string, providesTags?: TagsFunction<T>) =>
    (build: Build<typeof tag, typeof reducerPath>) => build.query<Awaited<ReturnType<T>>, Parameters<T>>({
        query: (args: Parameters<T>) => ({
            apiMethod,
            args,
        }),
        providesTags: providesTags
    });


export default {
    createMutation,
    createQuery,
}