import { CustomUser } from '@/types/next-auth';
import serverAxios from '@/lib/api/server/ServerAxios';


/**
 * 서버 측 세션 종료
 * @param user
 */
export const signOutServer = async (user : CustomUser) => {
    if(user){
        serverAxios.post("/auth/logout")
    }
}
