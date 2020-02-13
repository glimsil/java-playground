package com.glimsil.poc.okhttp.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExampleRestClient extends RestClient {

    public void getJsonPost() {
        try {
            System.out.println(start("https://jsonplaceholder.typicode.com/posts/5").retry(true).get(Map.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postExample() {
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Title");
        body.put("body","example");
        body.put("userId", "1");
        try {
            System.out.println(start("https://jsonplaceholder.typicode.com/posts").retry(true).post(body, Map.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getGoogleInexistant() {
        try {
            System.out.println(start("http://google.com/keke").retry(true).get(Map.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getGoogleInexistantUseFallback() { //Fallback requires retry == true
        Map<String, Object> body = new HashMap<>();
        body.put("fallback", "rules");
        try {
            System.out.println(start("http://google.com/keke").retry(true).fallback(() -> body).get(Map.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
