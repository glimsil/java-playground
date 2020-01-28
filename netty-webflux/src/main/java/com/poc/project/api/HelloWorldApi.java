package com.poc.project.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hello/world")
public class HelloWorldApi {

    @GetMapping
    public Mono<String> getHelloWorld() {
        return Mono.just("Hello World");
    }
}
