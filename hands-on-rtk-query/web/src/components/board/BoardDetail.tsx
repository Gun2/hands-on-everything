import React from 'react';
import Layout from "./Layout";
import {ContentLayout} from "./ContentLayout";
import {useNavigate, useParams} from "react-router-dom";
import {Button, Divider, Drawer, Skeleton, Stack} from "@mui/material";
import {useDeleteByIdMutation, useGetBoardByIdQuery} from "../../redux/services/boardService";

const BoardRegistry = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const {data, error, isLoading} = useGetBoardByIdQuery(Number(id));
    const [deleteById, {}] = useDeleteByIdMutation();
    return (
        <Layout
            title={"게시판 정보"}
            content={(
                <>
                    <ContentLayout
                        contentArea={(
                            <>
                                {data && !isLoading ? (
                                    <Stack>
                                        <Stack direction={'row'} alignItems={'center'}>
                                            <h1 style={{flex:1}}>{data?.title}</h1>
                                            <div>{data?.createdAt}</div>
                                        </Stack>
                                        <Divider/>
                                        <pre>{data?.content}</pre>
                                    </Stack>
                                ) : (
                                    <Stack>
                                        <Skeleton/>
                                        <Skeleton/>
                                    </Stack>
                                )}
                            </>
                        )}
                        buttonArea={(
                            <>
                                <Button
                                    variant={"contained"}
                                    onClick={() => {
                                        navigate(`/board/${id}/edit`);
                                    }}>
                                    수정
                                </Button>
                                <Button
                                    color={"error"}
                                    variant={"contained"}
                                    onClick={() => {
                                        deleteById(Number(id)).then(({data, error}) => {
                                          if (!error){
                                            navigate(`/board`);
                                          }
                                        });
                                    }}>
                                    삭제
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