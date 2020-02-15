package com.glimsil.poc.okhttp.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glimsil.poc.okhttp.exception.BodyParsingException;
import com.glimsil.poc.okhttp.exception.FallbackMethodFailedException;
import com.glimsil.poc.okhttp.exception.RetriesExceededException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public abstract class RestClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private OkHttpClient client = new OkHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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

        public RestRequest fallback(Callable<Object> fallback) {
            this.fallback = fallback;
            return this;
        }

        public <T> Optional<T> get(Class<T> valueType) {
            Request request = requestBuilder.get().build();
            ResponseBody responseBody = execute(() -> client.newCall(request).execute(), retry ? retryPolicy : null);
            return parseBody(responseBody, valueType);
        }

        public <T> Optional<T> post(Object object, Class<T> valueType) {
            RequestBody requestBody = buildRequestBody(object);
            ResponseBody responseBody = execute(() -> client.newCall(requestBuilder.post(requestBody).build()).execute(), retry ? retryPolicy : null);
            return parseBody(responseBody, valueType);
        }

        public <T> Optional<T> put(Object object, Class<T> valueType) {
            RequestBody requestBody = buildRequestBody(object);
            ResponseBody responseBody = execute(() -> client.newCall(requestBuilder.put(requestBody).build()).execute(), retry ? retryPolicy : null);
            return parseBody(responseBody, valueType);
        }

        public <T> Optional<T> delete(Class<T> valueType) {
            Request request = requestBuilder.delete().build();
            ResponseBody responseBody = execute(() -> client.newCall(request).execute(), retry ? retryPolicy : null);
            return parseBody(responseBody, valueType);
        }

        private ResponseBody execute(Callable<Response> method, RetryPolicy retryPolicy) {
            Response response;
            for (int i = 0; i < (null == retryPolicy ? 1 : retryPolicy.getAttempts()); i++) {
                try {
                    if(i > 0) {
                        Thread.sleep(retryPolicy.getDelay() + (retryPolicy.getDelayIncrement() * i-1));
                    }
                    response = method.call();
                    if (null == retryPolicy || !retryPolicy.getStatusCodeList().contains(response.code())) {
                        return response.body();
                    }
                } catch (Exception e) {
                    logger.error("Error on calling method.", e);
                }
            }
            if(null == fallback) {
                throw new RetriesExceededException("Retries exceeded.");
            } else {
                try {
                    return buildResponseBody(fallback.call());
                } catch (Exception e) {
                    throw new FallbackMethodFailedException("Fallback method failed.", e);
                }
            }
        }

        private <T> Optional<T> parseBody(ResponseBody responseBody, Class<T> valueType) {
            Optional<T> body = Optional.empty();
            try {
                if(null != responseBody) {
                    body = Optional.of(objectMapper.readValue(responseBody.string(), valueType));
                }
            } catch (IOException e) {
                logger.error("Error trying to parse body.", e);
                throw new BodyParsingException("Error trying to parse body.", e);
            }
            return body;
        }

        private RequestBody buildRequestBody(Object object) {
            try {
                return RequestBody.create(objectMapper.writeValueAsString(object),
                        MediaType.parse("application/json; charset=utf-8"));
            } catch (JsonProcessingException e) {
                logger.error("Error on building request body: " + e.getMessage(), e);
            }
            return RequestBody.create(null);
        }

        private ResponseBody buildResponseBody(Object object) {
            try {
                if(object instanceof Optional) {
                    object = ((Optional) object).get();
                }
                return ResponseBody.create(objectMapper.writeValueAsString(object),
                        MediaType.parse("application/json; charset=utf-8"));
            } catch (JsonProcessingException e) {
                logger.error("Error on building response body: " + e.getMessage(), e);
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

    protected RetryPolicy retryPolicy() {
        return new RetryPolicy()
                .handle(404)
                .attempts(3)
                .delay(1500)
                .delayIncrement(500);
    }

}
