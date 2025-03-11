// /auth.ts
import NextAuth from 'next-auth';
import Credentials from 'next-auth/providers/credentials';
import { authConfig } from './auth.config';
import { CustomUser } from '@/types/next-auth';
import { authService } from '@/lib/api/auth/authService';
import { LoginRequest } from '@/types/auth.types';

export const { auth, signIn, signOut} = NextAuth({
    ...authConfig,
    providers: [
        Credentials({
            async authorize(credentials, request) {
                console.log("credentials")
                console.log(credentials)
                const {username, password}: LoginRequest = credentials;
                if(username && password){
                    let loginResponse = await authService.login({username, password})
                    if (loginResponse.status === 200 && loginResponse?.data){
                        let data = loginResponse.data;
                        const user : CustomUser = {
                            accessToken: data.accessToken,
                            refreshToken: data.refreshToken,
                            username : username,
                            name : username,
                            role: "",
                        }
                        return user
                    }

                }
                return null;
            },
        }),
    ],
});