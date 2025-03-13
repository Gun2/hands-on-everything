import type { NextAuthConfig } from 'next-auth';
import { AdapterUser } from '@auth/core/adapters';
import { CustomUser } from '@/types/next-auth';
import { authService } from '@/lib/api/auth/authService';

export const authConfig = {
    providers: [],
    pages: {
        signIn: '/login',
    },
    callbacks: {
        /*authorized({ auth, request: { nextUrl } }) {
            if(nextUrl.pathname?.startsWith("/user") && auth?.user?.authCode !== "MANAGER"){
                return false;
            }
            return !!auth?.user;
        },*/
        async jwt({session, user, token}){

            console.log(`1 user`, user);
            console.log(`1 session`, session);
            console.log(`1 token`, token);

            user && (token.user = user);
            return Promise.resolve(token);
        },
        async session({session, token, user}){

            if(token?.accessToken){
                const accessToken = token?.accessToken as AdapterUser & CustomUser;
                session.accessToken = accessToken;
            }
            return Promise.resolve(session);
        },
    },
    events: {
        /*signIn: async (message) => {
            const user = message?.user as CustomUser;
            cookies().set("JSESSIONID", user?.sessionId);
        },*/
        signOut: async (message : any) => {
            const user = message?.token?.user as CustomUser;
            if (user?.accessToken){
                try {
                    await authService.logout(user.accessToken);
                }catch (e){
                    console.error(e);
                }
            }
        }

    }
} satisfies NextAuthConfig;