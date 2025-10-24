import { FormLabel, Skeleton, Stack, Typography } from '@mui/material';

export interface BoardDetailFormData {
    id: number;
    title: string;
    content: string;
}

export interface BoardDetailFormProps {
    data: BoardDetailFormData
}

export default function BoardDetailForm(
    {
        data
    }: BoardDetailFormProps
){
    return (
        <Stack gap={1}>
          <FormLabel>제목</FormLabel>
          <Typography>{data.title}</Typography>
          <FormLabel>내용</FormLabel>
          <Typography
            sx={{
              whiteSpace: 'pre-line',
            }}
          >{data.content}</Typography>
        </Stack>
    )
}

export function BoardDetailFormSkeleton(){
    return (
        <Stack gap={1}>
            <Skeleton/>
            <Skeleton/>
        </Stack>
    )
}