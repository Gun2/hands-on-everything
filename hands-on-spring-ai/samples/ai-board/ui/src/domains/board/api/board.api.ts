import { AxiosResponse } from 'axios';
import { Board, BoardCreateRequest, BoardUpdateRequest } from '../types/board.api.type';
import defaultAxios from '../../../libs/api/defaultAxios';

/**
 * Board 관련 API 요청을 담당하는 서비스
 */
export const boardApi = {
    /**
     * 모든 게시판 목록 조회
     * @return - 게시판 배열
     */
    getAll: async (): Promise<AxiosResponse<Board[]>> => {
        return defaultAxios.get(`/boards`);
    },
    /**
     * 특정 게시판 단건 조회
     * @param id - 게시판 ID
     * @return - 단일 게시판 데이터
     */
    getById: async (id: Board["id"]): Promise<AxiosResponse<Board>> => {
        return defaultAxios.get(`/boards/${id}`);
    },
    /**
     * 새로운 게시판 생성
     * @param data - 게시판 생성 요청 데이터
     * @return - 생성된 게시판 데이터
     */
    create: async (data: BoardCreateRequest): Promise<AxiosResponse<Board>> => {
        return defaultAxios.post(`/boards`, data);
    },
    /**
     * 게시판 수정
     * @param id - 게시판 ID
     * @param data - 수정할 게시판 데이터
     * @return - 수정된 게시판 데이터
     */
    update: async (id : Board["id"], data: BoardUpdateRequest): Promise<AxiosResponse<Board>> => {
        return defaultAxios.put(`/boards/${id}`, data);
    },
    /**
     * 게시판 삭제
     * @param id - 게시판 ID
     * @return - 응답 본문 없음
     */
    delete: async (id: Board["id"]): Promise<AxiosResponse<void>> => {
        return defaultAxios.delete(`/boards/${id}`);
    },
}