import React, {ChangeEvent, useCallback, useEffect, useState} from 'react';
import Layout from "./Layout";
import {ContentLayout} from "./ContentLayout";
import {Alert, Button, Skeleton, TextField} from "@mui/material";
import {BoardUpdateFrom, BoardUpdateRequest} from "../../types/board";
import {useNavigate, useParams} from "react-router-dom";
import {useGetBoardByIdQuery, useUpdateBoardMutation} from "../../redux/services/boardService";

const BoardRegistry = () => {
    const {id} = useParams();
    const [data, setData] = useState<BoardUpdateFrom>({
        content: "",
        title: "",
    });
    const {data : serverData, error, isLoading, refetch} = useGetBoardByIdQuery(Number(id));
    useEffect(() => {
        if(!!serverData){
            setData(serverData)
        }
    }, [serverData]);
    const navigate = useNavigate();
    const onChange = useCallback(({target: {name, value}} : ChangeEvent<HTMLInputElement>) => {
        setData(prevState => ({
            ...prevState,
            [name]: value,
        }));
    }, []);
    const [updateBoard, updateBoardResult] = useUpdateBoardMutation();
    return (
        <Layout
            title={"게시판 수정"}
            content={(
                <>
                    {updateBoardResult.isError && <Alert severity={"error"}>저장에 실패하였습니다.</Alert>}
                    <ContentLayout
                        contentArea={(
                            <>
                                {
                                    data && !isLoading ? (
                                        <>
                                            <TextField label={"제목"} name={"title"} onChange={onChange} value={data.title}/>
                                            <TextField label={"내용"} name={"content"} multiline rows={4} onChange={onChange} value={data.content}/>
                                        </>
                                    ) : (
                                        <>
                                            <Skeleton/>
                                            <Skeleton/>
                                        </>
                                    )
                                }
                            </>
                        )}
                        buttonArea={(
                            <>
                                <Button
                                    variant={"contained"}
                                    onClick={() => {
                                        updateBoard({
                                            id: Number(id),
                                            ...data
                                        }).then(({data, error}) => {
                                            if (!error){
                                                refetch();
                                                navigate(`/board/${id}`);
                                            }
                                        });
                                    }}
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