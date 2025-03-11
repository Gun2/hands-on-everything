import React from 'react';
import { auth, signOut } from '../../../../auth';

const LogoutHeader = async () => {
  let session = await auth();
  if (session == null) {
    return;
  }
  return (
    <div style={{ flex: 1, textAlign: "end", fontSize: "1.5rem" }}>
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

export default LogoutHeader;