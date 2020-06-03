package br.com.poc.simpleapi.api;


import br.com.poc.simpleapi.api.dto.ProductDto;
import br.com.poc.simpleapi.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class SimpleApi {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductService productService;

    @GetMapping("/hello/world")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello world springboot!!");
    }

    @GetMapping("/hello/world/mono")
    public Mono<String> hello(Mono<Principal> principal) {
        return principal
                .map(Principal::getName)
                .map(this::helloMessage);
    }

    private String helloMessage(String username) {
        return "Hello, " + username + "!";
    }


    @PostMapping("/json/message")
    public ResponseEntity<?> getJsonProduct() {
        return ResponseEntity.ok(ProductDto.builder()
                .id(1)
                .name("John")
                .build());
    }

}