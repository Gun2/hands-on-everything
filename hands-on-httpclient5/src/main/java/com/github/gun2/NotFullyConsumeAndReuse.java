package com.github.gun2;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 결과물을 완전히 소비하지 않고 커넥션을 재 사용 하는 경우
 */
public class NotFullyConsumeAndReuse {

    public static void main(String[] args) throws IOException, InterruptedException {
        fine();
        //response content가 연결중에 완전히 소비되지 않으면 안전하게 재사용할 수 없음
        incorrect();
    }

    /**
     * response content가 연결중에 완전히 소비되지 않은 요청 수행
     * @throws IOException
     * @throws InterruptedException
     */
    public static void incorrect() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            for (int i = 0; i < 2; i++) {
                ClassicHttpRequest httpGet = ClassicRequestBuilder.get("http://httpbin.org/get")
                        .build();
                httpclient.execute(httpGet, response -> {
                    System.out.println(response.getCode() + " " + response.getReasonPhrase());
                    final HttpEntity entity1 = response.getEntity();
                    // 첫 줄만 읽고 나머지는 소비하지 않음
                    BufferedReader reader = new BufferedReader(new InputStreamReader(entity1.getContent()));
                    new Thread(() -> {
                        try {
                            String line;
                            while ((line = reader.readLine()) != null){
                                System.out.println("Response1 Body (Partial):" +  line);
                                Thread.sleep(100);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        countDownLatch.countDown();
                    }).start();
                    return null;
                });
            }
            countDownLatch.await(30, TimeUnit.SECONDS);
        }
    }

    /**
     * response content가 연결중에 완전히 소비하는 요청 수행
     * @throws InterruptedException
     * @throws IOException
     */
    public static void fine() throws InterruptedException, IOException {

        CountDownLatch countDownLatch = new CountDownLatch(2);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            Stream.generate(() -> new Thread(() -> {
                ClassicHttpRequest httpGet = ClassicRequestBuilder.get("http://httpbin.org/get")
                        .build();
                try {
                    httpclient.execute(httpGet, response -> {
                        System.out.println(response.getCode() + " " + response.getReasonPhrase());
                        final HttpEntity entity1 = response.getEntity();
                        // 첫 줄만 읽고 나머지는 소비하지 않음
                        BufferedReader reader = new BufferedReader(new InputStreamReader(entity1.getContent()));
                        try {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                System.out.println("Response Body (Partial):" + line);
                                Thread.sleep(1000);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        countDownLatch.countDown();
                        return null;
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })).limit(2).forEach(Thread::start);
            countDownLatch.await(30, TimeUnit.SECONDS);
        }
    }
}
