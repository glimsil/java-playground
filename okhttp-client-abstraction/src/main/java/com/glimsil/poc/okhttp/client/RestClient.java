package com.glimsil.poc.okhttp.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glimsil.poc.okhttp.exception.FallbackMethodFailedException;
import com.glimsil.poc.okhttp.exception.RetriesExceededException;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class RestClient {
    private OkHttpClient client = new OkHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    public class RetryPolicy {
        private int attempts = 1;
        private int delay = 1500;
        private int delayIncrement = 0;
        private List<Integer> statusCodeList = new ArrayList<>();

        public RetryPolicy handle(int statusCode) {
            statusCodeList.add(statusCode);
            return this;
        }

        public RetryPolicy delay(int delay) {
            this.delay = delay;
            return this;
        }

        public RetryPolicy delayIncrement(int delayIncrement) {
            this.delayIncrement = delayIncrement;
            return this;
        }

        public RetryPolicy attempts(int attempts) {
            this.attempts = attempts;
            return this;
        }

        public int getAttempts() {
            return attempts;
        }

        public int getDelay() {
            return delay;
        }

        public int getDelayIncrement() {
            return delayIncrement;
        }

        public List<Integer> getStatusCodeList() {
            return statusCodeList;
        }
    }

    public class RestRequest {
        private Request.Builder requestBuilder;
        private RetryPolicy retryPolicy = retryPolicy();
        private boolean retry = true;
        private Callable<Object> fallback;

        private RestRequest() {
            requestBuilder = new Request.Builder();
        }

        private RestRequest(String uri) {
            requestBuilder = new Request.Builder().url(uri);
        }

        private RestRequest url(String url) {
            requestBuilder.url(url);
            return this;
        }

        public RestRequest header(String name, String value) {
            requestBuilder.addHeader(name, value);
            return this;
        }

        public RestRequest auth(String token) {
            requestBuilder.addHeader("Authorization", token);
            return this;
        }

        /*
            Default: retry = true
         */
        public RestRequest retry(boolean active) {
            this.retry = active;
            return this;
        }

        /*
            Requires retry = true
         */
        public RestRequest fallback(Callable<Object> fallback) {
            this.fallback = fallback;
            return this;
        }

        public <T> T get(Class<T> valueType) throws IOException {
            if (retry) {
                return objectMapper.readValue(retry(() -> client.newCall(requestBuilder.get().build()).execute()).string(), valueType);
            }
            return objectMapper.readValue(client.newCall(requestBuilder.get().build()).execute().body().string(), valueType);
        }

        public <T> T post(Object object, Class<T> valueType) throws IOException {
            RequestBody requestBody = buildRequestBody(object);
            if (retry) {
                return objectMapper.readValue(retry(() -> client.newCall(requestBuilder.post(requestBody).build()).execute()).string(), valueType);
            }
            return objectMapper.readValue(client.newCall(requestBuilder.post(requestBody).build()).execute().body().string(), valueType);
        }

        public <T> T put(Object object, Class<T> valueType) throws IOException {
            RequestBody requestBody = buildRequestBody(object);
            if (retry) {
                return objectMapper.readValue(retry(() -> client.newCall(requestBuilder.put(requestBody).build()).execute()).string(), valueType);
            }
            return objectMapper.readValue(client.newCall(requestBuilder.put(requestBody).build()).execute().body().string(), valueType);
        }

        public <T> T delete(Class<T> valueType) throws IOException {
            if (retry) {
                return objectMapper.readValue(retry(() -> client.newCall(requestBuilder.delete().build()).execute()).string(), valueType);
            }
            return objectMapper.readValue(client.newCall(requestBuilder.delete().build()).execute().body().string(), valueType);
        }

        private ResponseBody retry(Callable<Response> method) {
            Response response;
            for (int i = 0; i < retryPolicy.getAttempts(); i++) {
                try {
                    response = method.call();
                    if (!retryPolicy.getStatusCodeList().contains(response.code())) {
                        return response.body();
                    }
                    Thread.sleep(retryPolicy.getDelay() + (retryPolicy.getDelayIncrement() * i));
                } catch (Exception e) {
                    System.out.println("Error on calling method.");
                }
            }
            if(null == fallback) {
                throw new RetriesExceededException("Retries exceeded.");
            } else {
                try {
                    return buildResponseBody(fallback.call());
                } catch (Exception e) {
                    throw new FallbackMethodFailedException("Fallback method failed.");
                }
            }
        }

        private RequestBody buildRequestBody(Object object) {
            try {
                return RequestBody.create(objectMapper.writeValueAsString(object),
                        MediaType.parse("application/json; charset=utf-8"));
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        private ResponseBody buildResponseBody(Object object) {
            try {
                return ResponseBody.create(objectMapper.writeValueAsString(object),
                        MediaType.parse("application/json; charset=utf-8"));
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

    }

    protected RestRequest start() {
        return new RestRequest();
    }

    protected RestRequest start(String uri) {
        return new RestRequest(uri);
    }

    private RetryPolicy retryPolicy() {
        return new RetryPolicy()
                .handle(404)
                .attempts(3)
                .delay(1500)
                .delayIncrement(500);
    }

}
