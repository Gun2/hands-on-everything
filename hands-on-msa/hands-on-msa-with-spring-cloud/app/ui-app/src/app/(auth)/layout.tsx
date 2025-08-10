import React from 'react';
import AuthProvider from '@/app/(auth)/(components)/AuthProvider';

const Layout = (
  {
    children,
  }: {
    children?: React.ReactNode;
  }
) => {
  return (
    <AuthProvider>
      {children}
    </AuthProvider>
  );
};

export default Layout;