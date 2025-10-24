'use client';
import React from 'react';
import Layout from '@/app/board/(ui)/Layout';
import BoardListContainer from '@/app/board/(ui)/BoardListContainer';
import BoardTool from '@/app/board/(ui)/BoardTool';

export default function BoardListFormLayout() {
  return (
    <Layout
      titleArea={"게시글"}
      contentArea={<BoardListContainer/>}
      toolArea={<BoardTool/>}
    />
  );
}
