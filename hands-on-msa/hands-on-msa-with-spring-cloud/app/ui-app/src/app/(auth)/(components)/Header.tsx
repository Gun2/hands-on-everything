import React from 'react';
import LogoutButton from '@/app/(auth)/(components)/LogoutButton';
import CurrentTime from '@/app/(auth)/(components)/CurrentTime';
import { auth } from '../../../../auth';

type HeaderProps = {
}
const Header = async (
  {

  }: HeaderProps
) => {
  let session = await auth();
  if (session == null) {
    return;
  }
  return (
    <div style={{ flex: 1, textAlign: "end", fontSize: "1.5rem" }}>
      <CurrentTime/>
      <LogoutButton/>
    </div>
  );
};

export default Header;