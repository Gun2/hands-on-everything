
export type Board = {
    id: number;
    title: string;
    content: string;
    createdAt: string;
}

export type BoardSearchParams = {
    size: number,
    page: number,
}

export type BoardCreateRequest = {
    title: Board["title"];
    content?: Board["content"];
}

export type BoardUpdateRequest = {
    id: Board["id"];
    title: Board["title"];
    content?: Board["content"];
}

export type BoardUpdateFrom = {
    title: Board["title"];
    content?: Board["content"];
}