import React from 'react';
import { Card, Stack } from '@mui/material';
import { Link } from 'react-router-dom';

const Nav = () => {
  return (
    <Stack direction={"row"} alignItems={"center"}>
    </Stack>
  );
};

type NavItemProps = {
  text: string;
  link: string;
}
const NavItem = (
  {
    link,
    text
  }: NavItemProps
) => {
  return (
    <Card sx={{padding: 1}}><Link to={link}>{text}</Link></Card>
  )
}

export default Nav;