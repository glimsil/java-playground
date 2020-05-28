package br.com.poc.simpleapi.lib.vertx;

import br.com.poc.simpleapi.lib.ApplicationConfiguration;
import br.com.poc.simpleapi.lib.web.RouteConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class VertxServerVerticle extends AbstractVerticle {
    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private RouteConfig routeConfig;
    @Override
    public void start() throws Exception {
        super.start();
        vertx.createHttpServer().requestHandler(router()::accept).listen(applicationConfiguration.httpPort());
    }
    private Router router() {
        Router router = routeConfig.configRoutes(vertx, "br.com.poc.simpleapi.api");
        router.get("/info").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("Content-Type", "application/json");
        });
        router.get("/hello").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.end("hello");
        });
        return router;
    }
}