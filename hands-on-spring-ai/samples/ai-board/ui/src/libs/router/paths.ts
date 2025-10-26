export const PATHS = {
    //home
    HOME: () => "/",
    //게시글 조회
    BOARD_LIST: () => "/board",
    BOARD_REGISTRY: () => "/board/registry",
    BOARD_DETAIL: (id: number) => `/board/${id}`,
    BOARD_EDIT: (id:number) => `/board/edit/${id}`,

    CHAT: () => "/chat",
}