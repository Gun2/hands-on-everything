import React from 'react';
import { auth } from '../../../../auth';
import { redirect } from 'next/navigation';

type AuthProviderProps = {
  children?: React.ReactNode
}
const AuthProvider = async (
  {
    children
  }: AuthProviderProps
) => {
  let session = await auth();
  if (session == null) {
    redirect("/login");
  }
  return (
    <>
      {children}
    </>
  );
};

export default AuthProvider;