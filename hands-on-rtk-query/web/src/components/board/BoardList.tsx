import React from 'react';
import { useDeleteByIdMutation, useLazySearchQuery } from '../../redux/services/boardService';
import {Button, Skeleton} from "@mui/material";
import {useNavigate} from "react-router-dom";
import Layout from "./Layout";
import {DataTable} from "../ui/DataTable";
import {Link} from "../ui/Link";
import {useDataTable} from "../../hooks/useDataTable";
import {Board} from "../../types/board";

const BoardList = () => {
    const navigate = useNavigate();
    const [deleteById] = useDeleteByIdMutation();
    const [searchBoard, {data, isLoading}] = useLazySearchQuery();
    const dataTableProps = useDataTable<Board>({
        page: 0,
        size: 10,
        paging: (page, size, handle) => {
            searchBoard({
                page: page,
                size: size,
            });
        }
    });

    return (
        <Layout
            title={"게시판"}
            content={(
                <>
                    <div style={{display:'flex', justifyContent: 'flex-end'}}>
                        <Button
                            variant={"contained"}
                            onClick={() => navigate(`/board/create`)}
                        >
                            등록
                        </Button>
                    </div>
                    {data != undefined ? (
                        <DataTable
                            scheme={{
                                id: row => row.id,
                                columns: [
                                    {
                                        name: "제목",
                                        selector: row => (
                                            <Link href={`/board/${row.id}`}>
                                                {row.title}
                                            </Link>
                                        ),
                                    },
                                    {
                                        name: "내용",
                                        selector: row => row.content,
                                    },
                                    {
                                        name: "등록시간",
                                        selector: row => row.createdAt,
                                    },
                                    {
                                        name: "",
                                        selector: row => (
                                          <Button
                                            color={"error"}
                                            variant={"contained"}
                                            onClick={() => {
                                                deleteById(Number(row.id));
                                            }}>
                                              삭제
                                          </Button>
                                        )
                                    }

                                ]
                            }}
                            rows={data.content}
                            pagination={{
                                totalPages: data.totalPages,
                                totalElements: data.totalElements,
                                pageable: data.pageable
                            }}
                            search={dataTableProps.search}
                        />
                    ) : (
                        <Skeleton/>
                    )}
                </>
            )}
        />
    );
};

export default BoardList;