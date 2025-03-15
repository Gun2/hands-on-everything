import { NextRequest, NextResponse } from 'next/server';
import { auth } from '../auth';
import { getAuthHeaderAndValueOnServer } from '@/lib/authUtil';

export default async function middleware(req: NextRequest) {
  const session = await auth();
  if (session?.accessToken){
    req.headers.append("Authorization", `Bearer ${session?.accessToken}`);
    console.log('req.headers', req.headers.get("Authorization"));
    const authHeaderAndValue = await getAuthHeaderAndValueOnServer();
    console.log('authHeaderAndValue', authHeaderAndValue);
    return NextResponse.next({
      request: { headers: req.headers },
    });
  }
  return NextResponse.next()
}

export const config = {
  matcher: '/service/:path*',
}