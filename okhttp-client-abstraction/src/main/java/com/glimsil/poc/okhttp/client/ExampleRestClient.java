package com.glimsil.poc.okhttp.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExampleRestClient extends RestClient {

    @Override
    protected RetryPolicy retryPolicy() {
        return new RetryPolicy()
                .handle(502) // will retry when receives status 502
                .handle(503) // will retry when receives status 503
                .handle(404) // will retry when receives status 404
                .attempts(4) // will try to call endpoint 4 times
                .delay(1500) // will wait 1500 ms before retry
                .delayIncrement(500); // will add 500 ms of wait in each call (1500, 2000, 2500,..)
    }

    public void getJsonPost() {
        System.out.println(
                start("https://jsonplaceholder.typicode.com/posts/5")
                        .retry(true) // default = true
                        .get(Map.class)
        );
    }

    public void postExample() {
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Title");
        body.put("body","example");
        body.put("userId", "1");
        System.out.println(
                start("https://jsonplaceholder.typicode.com/posts").retry(true).post(body, Map.class)
        );
    }

    public void getGoogleInexistant() {
        System.out.println(start("http://google.com/keke").retry(true).get(Map.class));
    }

    public void getGoogleInexistantUseFallback() {
        Map<String, Object> body = new HashMap<>();
        body.put("fallback", "rules");
        System.out.println(start("http://google.com/keke").retry(true).fallback(() -> body).get(Map.class));
    }

    public void cascadeFallback() throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("fallback", "rules");
        body.put("cascade", "works");

        System.out.println(
                start("http://google.com/keke")
                        .fallback(() -> start("http://google.com/lala")
                                .fallback(() -> body).get(Map.class))
                        .get(Map.class).get()
        );
    }
}
