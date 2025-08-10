import { redirect } from 'next/navigation';

export default async function Home(
  {
  }
) {

  redirect("/product");
  return (
    <div>home</div>
  );
}
