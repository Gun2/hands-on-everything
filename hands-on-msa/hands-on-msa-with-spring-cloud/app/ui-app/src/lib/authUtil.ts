import { isServer } from '@/lib/commonUtil';
import { auth } from '../../auth';
import { getSession } from 'next-auth/react';

/**
 * 인증 헤더와 값 정보
 */
type AuthHeaderAndValue = {
  name: string;
  value: string;
}

/**
 * 인증 헤더 정보 불러오기 (server only)
 */
export const getAuthHeaderAndValueOnServer = async (): Promise<AuthHeaderAndValue | null> => {
  if (isServer()) {
    const session = await auth();
    if (session) {
      return {
        name: "Authorization",
        value: `Bearer ${session?.accessToken}`
      }
    }
    return null;
  } else {
    throw new Error("is not on server")
  }
};
