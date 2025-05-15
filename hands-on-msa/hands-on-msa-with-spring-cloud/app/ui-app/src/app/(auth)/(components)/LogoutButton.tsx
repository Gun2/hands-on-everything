import React from 'react';
import { auth, signOut } from '../../../../auth';

const LogoutButton = async () => {
  let session = await auth();
  if (session == null) {
    return;
  }
  return (
    <div>
      {session?.user?.name}
      {
        <form action={
          async function() {
            "use server"
            await signOut();
          }
        }>
          <button>logout</button>
        </form>
      }
    </div>
  );
};

export default LogoutButton;