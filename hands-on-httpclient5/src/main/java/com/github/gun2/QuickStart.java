package com.github.gun2;

import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class QuickStart {
    public static void main(String[] args) throws IOException {
        getAndPost();
        asyncGet();
        asyncPost();
    }

    /**
     * get 요청과 post 요청
     * @throws IOException
     */
    private static void getAndPost() throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            ClassicHttpRequest httpGet = ClassicRequestBuilder.get("http://httpbin.org/get")
                    .build();
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            httpclient.execute(httpGet, response -> {
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                final HttpEntity entity1 = response.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
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
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
                return null;
            });
        }
    }

    /**
     * 비동기 요청
     */
    public static void asyncGet(){
        try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
            // Start the client
            httpclient.start();

            // Execute request
            SimpleHttpRequest request1 = SimpleRequestBuilder.get("http://httpbin.org/get").build();
            Future<SimpleHttpResponse> future = httpclient.execute(request1, null);
            // and wait until response is received
            SimpleHttpResponse response1 = future.get();
            System.out.println(request1.getRequestUri() + "->" + response1.getCode());

            // One most likely would want to use a callback for operation result
            CountDownLatch latch1 = new CountDownLatch(1);
            SimpleHttpRequest request2 = SimpleRequestBuilder.get("http://httpbin.org/get").build();
            httpclient.execute(request2, new FutureCallback<SimpleHttpResponse>() {

                @Override
                public void completed(SimpleHttpResponse response2) {
                    latch1.countDown();
                    System.out.println(request2.getRequestUri() + "->" + response2.getCode());
                }

                @Override
                public void failed(Exception ex) {
                    latch1.countDown();
                    System.out.println(request2.getRequestUri() + "->" + ex);
                }

                @Override
                public void cancelled() {
                    latch1.countDown();
                    System.out.println(request2.getRequestUri() + " cancelled");
                }

            });
            latch1.await();

            // In real world one most likely would want also want to stream
            // request and response body content
            CountDownLatch latch2 = new CountDownLatch(1);
            AsyncRequestProducer producer3 = AsyncRequestBuilder.get("http://httpbin.org/get").build();
            AbstractCharResponseConsumer<HttpResponse> consumer3 = new AbstractCharResponseConsumer<HttpResponse>() {

                HttpResponse response;

                @Override
                protected void start(HttpResponse response, ContentType contentType) throws HttpException, IOException {
                    this.response = response;
                }

                @Override
                protected int capacityIncrement() {
                    return Integer.MAX_VALUE;
                }

                @Override
                protected void data(CharBuffer data, boolean endOfStream) throws IOException {
                    // Do something useful
                    System.out.println("data : " + data);
                    System.out.println("endOfStream : " + endOfStream);
                }

                @Override
                protected HttpResponse buildResult() throws IOException {
                    return response;
                }

                @Override
                public void releaseResources() {
                    System.out.println("releaseResources");
                }

            };
            httpclient.execute(producer3, consumer3, new FutureCallback<HttpResponse>() {

                @Override
                public void completed(HttpResponse response3) {
                    latch2.countDown();
                    System.out.println(request2.getRequestUri() + "->" + response3.getCode());
                }

                @Override
                public void failed(Exception ex) {
                    latch2.countDown();
                    System.out.println(request2.getRequestUri() + "->" + ex);
                }

                @Override
                public void cancelled() {
                    latch2.countDown();
                    System.out.println(request2.getRequestUri() + " cancelled");
                }

            });
            latch2.await();

        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void asyncPost(){
        try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
            // Start the client
            httpclient.start();

            SimpleHttpRequest request1 = SimpleRequestBuilder.post("http://httpbin.org/post")
                    .addParameters(
                            new BasicNameValuePair("username", "vip"),
                            new BasicNameValuePair("password", "secret")
                    ).build();
            Future<SimpleHttpResponse> future = httpclient.execute(request1, null);
            SimpleHttpResponse simpleHttpResponse = future.get();
            System.out.println("simpleHttpResponse : " + simpleHttpResponse.getBodyText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}