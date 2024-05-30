import React, {useCallback} from 'react';
import {Link as MuiLink, LinkProps as MuiLinkProps} from "@mui/material";
import {useNavigate} from "react-router-dom";

type LinkProps = Omit<MuiLinkProps, "onClick" | "href"> & {
    href: string;
}
export const Link = (
    {
        href,
        underline='hover',
        ...rest
    }: LinkProps
) => {
    const navigate = useNavigate();
    const onClick = useCallback(() => {
        navigate(href);
    }, [href]);
    return (
        <MuiLink onClick={onClick} {...rest} underline={underline}/>
    );
};