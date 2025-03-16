import AuthProvider from '@/app/(auth)/(components)/AuthProvider';
import ClientSideOrder from '@/app/(order)/ClientSideOrder';
import ServerSideOrder from '@/app/(order)/ServerSideOrder';

export default async function Home(
  {
    searchParams,
  }: {
    searchParams
  }
) {
  const {output} = await searchParams
  return (
    <AuthProvider>
      주문하기
      <ClientSideOrder/>
      <ServerSideOrder output={output}/>
    </AuthProvider>
  );
}
