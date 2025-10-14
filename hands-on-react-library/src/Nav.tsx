import React from 'react';
import { Card, Stack } from '@mui/material';
import { Link } from 'react-router-dom'
import { ROUTING_PATH } from './App';

const Nav = () => {
  return (
    <Stack direction={"row"} alignItems={"center"}>
      <NavItem text={"react-textarea-autocomplete"} link={ROUTING_PATH.REACT_TEXTAREA_AUTOCOMPLETE}/>
      <NavItem text={"monaco-editor-react"} link={ROUTING_PATH.MONACO_EDITOR_REACT}/>
      <NavItem text={"react-flow"} link={ROUTING_PATH.REACT_FLOW}/>
      <NavItem text={"chat-scope-chat-ui-kit"} link={ROUTING_PATH.CHATSCOPE_CHAT_UI_KIT}/>
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