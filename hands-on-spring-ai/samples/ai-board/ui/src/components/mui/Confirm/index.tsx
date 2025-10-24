import React from 'react';
import Template, { TemplateProps } from './Template';

export interface ConfirmProps {
    open : TemplateProps["open"];
    handleOpen: TemplateProps["handleOpen"];
    title: TemplateProps["title"];
    message: TemplateProps["message"];
    onClickConfirm: TemplateProps["onClickConfirm"];

}

/**
 * confirm 템플릿
 */
export default function Confirm(
    {
        open,
        handleOpen,
        title,
        message,
        onClickConfirm,
    }: ConfirmProps
){
    return (
        <Template
            open={open}
            handleOpen={handleOpen}
            title={title}
            message={message}
            onClickConfirm={onClickConfirm}
        />
    )
};