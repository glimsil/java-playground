package com.glimsil.poc.okhttp;

import com.glimsil.poc.okhttp.client.ExampleRestClient;
import com.glimsil.poc.okhttp.exception.RetriesExceededException;
import okhttp3.RequestBody;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ExampleRestClient exampleRestClient = new ExampleRestClient();
        exampleRestClient.getJsonPost();
        exampleRestClient.postExample();
        exampleRestClient.cascadeFallback();
        exampleRestClient.getGoogleInexistantUseFallback();
        try {
            exampleRestClient.getGoogleInexistant(); // will throws RetriesExceededException
        } catch (RetriesExceededException e) {
            e.printStackTrace();
        }
    }
}
