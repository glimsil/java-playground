package br.com.poc.simpleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"br.com.poc.simpleapi"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}