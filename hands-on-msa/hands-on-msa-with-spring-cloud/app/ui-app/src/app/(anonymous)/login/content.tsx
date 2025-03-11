'use client';

import React, { ChangeEvent, useState } from 'react';
import { authenticate } from '@/app/(anonymous)/login/actions';

type LoginInfo = {
    username: string,
    password: string,
}

const Content = () => {
    const [errorMessage, dispatch] = React.useActionState(authenticate, undefined);

    const [loginInfo, setLoginInfo] = useState<LoginInfo>({
        username: "",
        password: "",
    })
    const onChange = (event: ChangeEvent<HTMLInputElement>) => {
        setLoginInfo(prevState => ({
            ...prevState,
            [event.target.name]: event.target.value
        }))
    }
    return (
        <div style={{
            width: 300,
            height: "80vh",
            margin: "auto",
            display: "flex",
            justifyContent: "center",
            flexDirection: "column"
        }}>

            <form action={dispatch} className="space-y-3">
                <div style={{display: "flex", flexDirection: "column", gap: 5}}>
                    <input name={"username"} value={loginInfo.username} onChange={onChange}/>
                    <input name={"password"} value={loginInfo.password} onChange={onChange}/>
                </div>
                <button>submit</button>
            </form>
        </div>

    )
};

export default Content;