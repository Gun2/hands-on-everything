import React from 'react';
import LogoutButton from '@/app/(auth)/(components)/LogoutButton';
import CurrentTime from '@/app/(auth)/(components)/CurrentTime';

type HeaderProps = {
}
const Header = (
  {

  }: HeaderProps
) => {
  return (
    <div style={{ flex: 1, textAlign: "end", fontSize: "1.5rem" }}>
      <CurrentTime/>
      <LogoutButton/>
    </div>
  );
};

export default Header;