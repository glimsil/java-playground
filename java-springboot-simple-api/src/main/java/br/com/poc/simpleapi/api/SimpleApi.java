package br.com.poc.simpleapi.api;


import br.com.poc.simpleapi.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SimpleApi {

    @GetMapping("/hello/world")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/user/json")
    public ResponseEntity<UserDto> getUser() {
        return ResponseEntity.ok(UserDto.builder()
                .id("1")
                .age(10)
                .name("John")
                .build());
    }
}