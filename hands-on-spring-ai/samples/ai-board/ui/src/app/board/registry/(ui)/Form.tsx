import { FormLabel, Skeleton, Stack, TextField } from '@mui/material';
import { useHandleInputEvent } from '@/hooks/useHandleInputEvent';


export interface BoardRegistryFormData {
    content: string,
    title: string,
}
export interface BoardRegistryFormProps {
    data: BoardRegistryFormData;
    onChange: (data: BoardRegistryFormData) => void;
}
export default function BoardRegistryForm(
    {
        onChange,
        data
    }: BoardRegistryFormProps
){
    const {handleInputEvent} = useHandleInputEvent({
        data: data,
        onChange
    })
    return (
        <Stack>
          <FormLabel>제목</FormLabel>
          <TextField
                fullWidth
                onChange={handleInputEvent}
                name="title"
                value={data.title}
                variant="outlined"
            />

          <FormLabel>내용</FormLabel>
          <TextField
                fullWidth
                multiline
                onChange={handleInputEvent}
                name="content"
                value={data.content}
                variant="outlined"
            />
        </Stack>
    )
}

export function BoardRegistryFormSkeleton(){
    return (
        <Stack>
            <Skeleton/>
            <Skeleton/>
        </Stack>
    )
}

/**
 * 기본값 반환
 */
export const createInitialData = (): BoardRegistryFormData => ({
    content: "",
    title: "",
})