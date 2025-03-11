import Content from "@/app/(anonymous)/login/content";
import {auth} from "../../../../auth";
import {redirect} from "next/navigation";

export default async function Page() {
    let session = await auth();
    if(session != null){
        redirect("/");
    }
    return (
        <Content/>
    )
}