import { User } from 'next-auth';

interface CustomUser extends User{
    accessToken : string,
    refreshToken: string,
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


