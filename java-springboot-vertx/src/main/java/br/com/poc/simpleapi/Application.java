package br.com.poc.simpleapi;

import br.com.poc.simpleapi.lib.vertx.VertxServerVerticle;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    @Autowired
    private VertxServerVerticle vertxServerVerticle;

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @PostConstruct
    public void deployServerVerticle() {
        Vertx.vertx().deployVerticle(vertxServerVerticle);
    }
}