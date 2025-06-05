import React from 'react';
import { Paper, Stack } from '@mui/material';
import { Outlet } from 'react-router-dom'
import Nav from '../Nav';

type PageLayoutProps = {
}

const headHeight = 100;

const PageLayout = (
  {

  }: PageLayoutProps
) => {
  return (
    <Stack>
      <div style={{height: headHeight}}><Nav/></div>
      <Paper sx={{height:`calc(100vh - ${headHeight + 50}px)`, margin: 1}}>
          <Outlet />
      </Paper>
    </Stack>
  );
};

export default PageLayout;