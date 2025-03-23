// /auth.ts
import NextAuth from 'next-auth';
import Credentials from 'next-auth/providers/credentials';
import { authConfig } from './auth.config';
import { authService } from '@/lib/api/auth/authService';
import { LoginRequest } from '@/types/auth.types';
import { CustomUser } from '@/types/next-auth';

export const { auth, signIn, signOut} = NextAuth({
    ...authConfig,
    providers: [
        Credentials({
            async authorize(credentials, request) {
                const {username, password}: LoginRequest = credentials;
                if(username && password){
                    try {
                        let loginResponse = await authService.login({username, password})
                        if (loginResponse.status === 200 && loginResponse?.data){
                            const user : CustomUser = {
                                session: loginResponse?.data?.data?.session,
                                username : "username",
                                name : "username",
                                role: "",
                            }
                            return user;
                        }
                    }catch (e){
                        console.error("login request error : ", e)
                    }
                }
                return null;
            },
        })
    ],
});