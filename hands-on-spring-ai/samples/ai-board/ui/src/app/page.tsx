import Layout from '@/app/(ui)/Layout';
import WelcomeContext from '@/app/(ui)/WelcomeContext';
import HomeButtons from '@/app/(ui)/HomeButtons';
import React from 'react';

export default function Page() {
  return (
    <Layout
      contextArea={<WelcomeContext/>}
      buttonsArea={<HomeButtons/>}
    />
  );
}
