import { User } from 'next-auth';
import { JWT } from '@auth/core/src/jwt';

interface CustomUser extends User{
    session: string,
    username : string,
    role: string
}
declare module "next-auth" {
    /**
     * Returned by `useSession`, `getSession` and received as a prop on the `SessionProvider` React Context
     */
    interface Session {
        user: CustomUser,
    }
}

/**
 * app에서 사용할 token 타입
 */
export type CustomToken = JWT & {
    user ?: CustomUser
}