import { ChangeEvent, useCallback } from 'react';


export interface UseHandleInputEventParams {
    //현재 데이터
    data: any,
    //데이터 변경 함수
    onChange: (data: any) => void,
}

/**
 * input event를 상태에 반영하기 위한 핸들러 생성 hook
 */
export const useHandleInputEvent = (
    {
        onChange,
        data,
    }: UseHandleInputEventParams
) => {
    const handleInputEvent = useCallback((e: ChangeEvent<HTMLInputElement>) => {
        onChange({
            ...data,
            [e.target.name]: e.target.value
        })
    }, [data, onChange]);
    return {handleInputEvent}
}