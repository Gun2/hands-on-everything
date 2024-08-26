package com.github.gun2;

import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 결과물을 완전히 소비하지 않고 커넥션을 재 사용 하는 경우
 */
public class NotFullyConsumeAndReuse {
    public static void main(String[] args) throws IOException {

        List<Runnable> list = new ArrayList<>();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            ClassicHttpRequest httpGet = ClassicRequestBuilder.get("http://httpbin.org/get")
                    .build();
            httpclient.execute(httpGet, response -> {
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                final HttpEntity entity1 = response.getEntity();
                // 첫 줄만 읽고 나머지는 소비하지 않음
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity1.getContent()));
                System.out.println("Response Body (Partial):" +  reader.readLine());
                if (!list.isEmpty()){
                    list.getFirst().run();
                }
                list.add(() -> {
                    try {
                        String line;
                        while ((line = reader.readLine()) != null){
                            System.out.println("Response Body (Partial):" +  line);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                return null;
            });

            ClassicHttpRequest httpPost = ClassicRequestBuilder.post("http://httpbin.org/post")
                    .setEntity(new UrlEncodedFormEntity(Arrays.asList(
                            new BasicNameValuePair("username", "vip"),
                            new BasicNameValuePair("password", "secret"))))
                    .build();
            httpclient.execute(httpPost, response -> {
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                final HttpEntity entity2 = response.getEntity();
                // 첫 줄만 읽고 나머지는 소비하지 않음
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity2.getContent()));
                System.out.println("Response Body (Partial):" +  reader.readLine());
                if (!list.isEmpty()){
                    list.getFirst().run();
                }
                list.add(() -> {
                    try {
                        String line;
                        while ((line = reader.readLine()) != null){
                            System.out.println("Response Body (Partial):" +  line);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                return null;
            });
        }

    }
}
