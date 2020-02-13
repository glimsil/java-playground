package com.glimsil.poc.okhttp;

import com.glimsil.poc.okhttp.client.ExampleRestClient;
import com.glimsil.poc.okhttp.exception.RetriesExceededException;

public class Main {
    public static void main(String[] args) {
        ExampleRestClient exampleRestClient = new ExampleRestClient();
        exampleRestClient.getJsonPost();
        exampleRestClient.postExample();
        exampleRestClient.getGoogleInexistantUseFallback();
        try {
            exampleRestClient.getGoogleInexistant(); // will throws RetriesExceededException
        } catch (RetriesExceededException e) {
            e.printStackTrace();
        }
    }
}
