import React from 'react';
import BoardEditContent from '@/app/board/edit/[id]/(ui)/BoardEditContent';
import { boardApi } from '@/domains/board/api/board.api';

export default async function Page(
  {
    params
  }: {
    params: {
      id: number;
    }
  }
) {
  const { id } = await params;
  const boardAxiosResponse = await boardApi.getById(id);
  return (
    <BoardEditContent initData={boardAxiosResponse.data}/>
  )
}
