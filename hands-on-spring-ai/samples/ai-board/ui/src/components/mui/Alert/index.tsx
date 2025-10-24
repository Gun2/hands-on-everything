import React from 'react';
import Template, { TemplateProps } from './Template';

export interface AlertProps {
    open : TemplateProps["open"];
    handleOpen: TemplateProps["handleOpen"];
    title: TemplateProps["title"];
    message: TemplateProps["message"];
}

/**
 * alert 템플릿
 */
export default function Alert(
    {
        open,
        handleOpen,
        title,
        message,
    }: AlertProps
){
    return (
        <Template
            open={open}
            handleOpen={handleOpen}
            title={title}
            message={message}
        />
    )
};