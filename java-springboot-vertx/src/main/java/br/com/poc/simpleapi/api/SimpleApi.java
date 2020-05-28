package br.com.poc.simpleapi.api;


import br.com.poc.simpleapi.api.dto.ProductDto;
import br.com.poc.simpleapi.api.dto.RestResultDto;
import br.com.poc.simpleapi.lib.web.annotation.Api;
import br.com.poc.simpleapi.lib.web.annotation.Endpoint;
import br.com.poc.simpleapi.lib.web.annotation.Secure;
import br.com.poc.simpleapi.repository.Product;
import br.com.poc.simpleapi.service.ProductService;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Api("/")
public class SimpleApi {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductService productService;

    @Endpoint(method = HttpMethod.GET, path = "/hello/world")
    public void hello(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.end("Hello world springboot!!");
    }

    @Endpoint(method = HttpMethod.POST, path = "/json/message")
    public void getJsonProduct(RoutingContext routingContext) {
        JsonObject json = JsonObject.mapFrom(ProductDto.builder()
                .id(1)
                .name("John")
                .build());
        routingContext
                .response()
                .putHeader("content-type", "application/json")
                .end(json.encodePrettily());
    }



    @Endpoint(method = HttpMethod.GET, path = "/product")
    public void getProducts(RoutingContext routingContext) {
        RestResultDto restResultDto = new RestResultDto();
        restResultDto.setResult(productService.find());
        JsonObject json = JsonObject.mapFrom(restResultDto);
        routingContext
                .response()
                .putHeader("content-type", "application/json")
                .end(json.encodePrettily());
    }

    @Secure({"ALL", "ADMIN"})
    @Endpoint(method = HttpMethod.GET, path = "/product/:id")
    public void getProduct(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        Optional<Product> product = productService.find(Integer.parseInt(id));
        if(product.isPresent()) {
            JsonObject json = JsonObject.mapFrom(product.get());
            routingContext
                    .response()
                    .putHeader("content-type", "application/json")
                    .end(json.encodePrettily());
        } else {
            routingContext.fail(404);
        }
    }

}