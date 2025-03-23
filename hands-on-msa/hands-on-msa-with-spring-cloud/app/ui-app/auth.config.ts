import type { NextAuthConfig } from 'next-auth';
import { CustomToken, CustomUser } from '@/types/next-auth';
import { authService } from '@/lib/api/auth/authService';

export const authConfig = {
    providers: [],
    pages: {
        signIn: '/login',
    },
    callbacks: {
        authorized({ auth, request: { nextUrl } }) {
            console.log("authorized auth", auth);
            return true;
        },
        async jwt({session, user, token, account}) : CustomToken {
            if (user && account){
                token.user = user;
            }
            return token;
        },
        async session({session, token, user}){
            console.log('token', token);
            if (token?.user){
                session.user = token.user;
            }
            return Promise.resolve(session);
        },
    },
    events: {
        /*signIn: async (message : any) => {
            const user = message?.user as CustomUser;
            cookies().set("SESSIONID", user?.sessionId);
        },*/
        signOut: async (message : any) => {
            const user = message?.token?.user as CustomUser;
            if (user?.session){
                try {
                    console.log('logout session', user.session);
                    await authService.logout(user.session);
                }catch (e){
                    console.error(e);
                }
            }
        }

    },
    session: { strategy: "jwt" },
} satisfies NextAuthConfig;