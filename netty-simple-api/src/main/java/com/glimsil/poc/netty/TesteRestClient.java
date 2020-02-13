package com.glimsil.poc.netty;

public class TesteRestClient extends RestClient {

    public void doit() {
        System.out.println(start("https://jsonplaceholder.typicode.com/todos/1").retry(true).fallback(true).get());
    }


    public static void main(String[] args) {
        TesteRestClient testeRestClient = new TesteRestClient();
        testeRestClient.doit();
    }
}
