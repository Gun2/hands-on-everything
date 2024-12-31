import { Client } from "@stomp/stompjs";
import Sockjs from 'sockjs-client';

export class WebSocketClientFactory {
    private static instance : WebSocketClientFactory;
    private client: Promise<Client>;
    private constructor() {
        this.client = new Promise<Client>(resolve => {
            const sockjs = new Sockjs('/ws');
            const client = new Client({
                webSocketFactory: () => sockjs,
                onConnect: () => {
                    resolve(client);
                }
            });
            client.activate();
        })
    }

    public static getInstance(): WebSocketClientFactory {
        if (!WebSocketClientFactory.instance){
            WebSocketClientFactory.instance = new WebSocketClientFactory();
        }
        return WebSocketClientFactory.instance;
    }

    public getClient(): Promise<Client> {
        return this.client;
    }
}