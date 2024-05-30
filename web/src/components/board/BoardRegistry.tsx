import React, {ChangeEvent, useCallback, useState} from 'react';
import Layout from "./Layout";
import {Alert, Button, Stack, TextField} from "@mui/material";
import {BoardCreateRequest} from "../../types/board";
import {useNavigate} from "react-router-dom";
import {useCreateBoardMutation} from "../../redux/services/boardService";
import {ContentLayout} from "./ContentLayout";

const BoardRegistry = () => {
    const [data, setData] = useState<BoardCreateRequest>({
        content: "",
        title: "",
    });
    const navigate = useNavigate();
    const onChange = useCallback(({target: {name, value}} : ChangeEvent<HTMLInputElement>) => {
        setData(prevState => ({
            ...prevState,
            [name]: value,
        }));
    }, []);
    const [createBoard, result] = useCreateBoardMutation();
    console.log(result);
    return (
        <Layout
            title={"게시판 등록"}
            content={(
                <>
                    {result.isError && <Alert severity={"error"}>저장에 실패하였습니다.</Alert>}
                    <ContentLayout
                        contentArea={(
                            <>
                                <TextField label={"제목"} name={"title"} onChange={onChange}/>
                                <TextField label={"내용"} name={"content"} multiline rows={4} onChange={onChange}/>
                            </>
                        )}
                        buttonArea={(
                            <>
                                <Button variant={"contained"}
                                        onClick={() => createBoard(data).then(({data, error}) => {
                                            if(!error){
                                                navigate('/board');
                                            }
                                        })}
                                >
                                    저장
                                </Button>
                                <Button variant={"outlined"} onClick={() => navigate('/board')}>목록</Button>
                            </>
                        )}
                    />
                </>
            )}
        />
    );
};

export default BoardRegistry;