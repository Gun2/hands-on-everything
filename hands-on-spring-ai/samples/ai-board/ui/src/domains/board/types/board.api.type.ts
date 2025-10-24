export interface Board {
    id: number;
    title: string;
    content: string;
}

export interface BoardCreateRequest {
    title: Board["title"];
    content: Board["content"];
}

export interface BoardUpdateRequest extends BoardCreateRequest {
}