import { ClientChatResponse } from '../types/clientChatResponse.types';
import { ClientChatRequest } from '../types/clientChatRequest.types';

/**
 * Board 관련 API 요청을 담당하는 서비스
 */
export const chatAiApi = {
  /**
   * ai chat 입력
   * @param data - 게시판 생성 요청 데이터
   * @return - 생성된 게시판 데이터
   */
  chat: async (data: ClientChatRequest): Promise<ClientChatResponse> => {
    const response = await fetch('http://localhost:8080/chat-ai', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  },
}