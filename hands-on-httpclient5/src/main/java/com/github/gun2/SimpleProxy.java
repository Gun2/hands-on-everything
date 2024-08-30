package com.github.gun2;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class SimpleProxy {
    private Thread serverSocketThread;
    private CountDownLatch stopCountDownLatch;
    public ServerSocket serverSocket;
    public void run(Integer port) throws InterruptedException {
        if (!isRunning()){
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(port);
                log.info("Proxy server started on port " + port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Thread thread = new Thread(() -> {
                    countDownLatch.countDown();
                    while (!Thread.interrupted()) {
                        try {
                            Socket clientSocket = serverSocket.accept();
                            new Thread(new ProxyThread(clientSocket)).start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (this.stopCountDownLatch != null){
                        this.stopCountDownLatch.countDown();
                    }
                    this.serverSocketThread = null;

            });
            thread.start();
            countDownLatch.await();
            setStateOfRunning(serverSocket, thread);
        }else {
            log.info("Proxy server is already started");
        }
    }

    private void setStateOfRunning(ServerSocket serverSocket, Thread serverSocketThread){
        this.serverSocket = serverSocket;
        this.serverSocketThread = serverSocketThread;
        this.stopCountDownLatch = new CountDownLatch(1);
    }

    public void stop() throws InterruptedException, IOException {
        if (this.serverSocketThread == null){
            log.info("Proxy server is not started yet");
            return;
        }
        if (!this.serverSocketThread.isAlive()){
            log.info("Proxy server is already stopped");
            return;
        }
        this.serverSocketThread.interrupt();
        this.serverSocket.close();
        if (this.stopCountDownLatch != null) {
            this.stopCountDownLatch.await();
        }
    }

    static class ProxyThread implements Runnable {

        private Socket clientSocket;

        public ProxyThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                InputStream clientInput = clientSocket.getInputStream();
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientInput));

                // 첫 번째 줄에서 요청된 호스트와 URL을 파싱
                String requestLine = clientReader.readLine();
                String[] requestParts = requestLine.split(" ");
                String url = requestParts[1];
                URL targetUrl = new URL(url);

                // 원격 서버와의 연결 설정
                Socket serverSocket = new Socket(targetUrl.getHost(), getPort(targetUrl));
                OutputStream serverOutput = serverSocket.getOutputStream();
                PrintWriter serverWriter = new PrintWriter(serverOutput);

                // 원래 요청을 서버로 전달
                serverWriter.println(requestLine);
                String headerLine;
                while ((headerLine = clientReader.readLine()) != null && !headerLine.isEmpty()) {
                    serverWriter.println(headerLine);
                }
                serverWriter.println();
                serverWriter.flush();

                // 서버로부터의 응답을 클라이언트로 전달
                InputStream serverInput = serverSocket.getInputStream();
                OutputStream clientOutput = clientSocket.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = serverInput.read(buffer)) != -1) {
                    clientOutput.write(buffer, 0, bytesRead);
                }

                // 자원 정리
                clientSocket.close();
                serverSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int getPort(URL url){
            if (url.getPort() != -1){
                return url.getPort();
            } else {
                return url.getDefaultPort();
            }
        }
    }

    public boolean isRunning(){
        return this.serverSocketThread != null;
    }
}
