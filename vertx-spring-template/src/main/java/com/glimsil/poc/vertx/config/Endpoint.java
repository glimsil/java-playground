package com.glimsil.poc.vertx.config;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class Endpoint {
    String route;
    Handler<RoutingContext> requestHandler;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Handler<RoutingContext> getRequestHandler() {
        return requestHandler;
    }

    public void setRequestHandler(Handler<RoutingContext> requestHandler) {
        this.requestHandler = requestHandler;
    }
}
