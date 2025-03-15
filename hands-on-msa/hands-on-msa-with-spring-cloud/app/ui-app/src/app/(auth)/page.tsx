import AuthProvider from '@/app/(auth)/(components)/AuthProvider';
import ClientSideOrder from '@/app/(order)/ClientSideOrder';

export default function Home() {
  return (
    <AuthProvider>
      주문하기
      <ClientSideOrder/>
    </AuthProvider>
  );
}
