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
            //user data를 token에 추가
            user && (token.user = user);
            return Promise.resolve(token);
        },
        async session({session, token, user}){
            if(token?.user?.accessToken) {
                //access token을 세션에 추가
                session.accessToken = token?.user?.accessToken;
            }
            return Promise.resolve(session);
        },
    },
    events: {
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