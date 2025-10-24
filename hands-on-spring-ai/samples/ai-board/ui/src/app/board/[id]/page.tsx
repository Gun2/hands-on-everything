import React from 'react';
import FormLayout from '@/app/board/(ui)/FormLayout';
import BoardDetailFormContainer from '@/app/board/[id]/(ui)/BoardDetailFormContainer';
import BoardDetailActionButtons from '@/app/board/[id]/(ui)/BoardDetailActionButtons';

export default function Page(
  {
    params: {
      id
    }
  }: {
    params: {
      id: number;
    }
  }
) {
  return(
    <FormLayout
      titleArea={"게시글 정보"}
      contentArea={<BoardDetailFormContainer id={Number(id)}/>}
      actionArea={<BoardDetailActionButtons boardId={Number(id)}/>}
    />
  );
}
