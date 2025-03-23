import { NextRequest, NextResponse } from 'next/server';
import { auth } from '../auth';
import { getAuthHeaderAndValueOnServer } from '@/lib/authUtil';

export default async function middleware(req: NextRequest) {
  const session = await auth();
  if (session?.user?.session){
    req.cookies.set("SESSION", session.user.session);
    return NextResponse.next({
      request: req
    });
  }
  return NextResponse.next()
}

export const config = {
  matcher: '/service/:path*',
}