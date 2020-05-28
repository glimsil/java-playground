package br.com.poc.simpleapi.lib.web;

import io.vertx.ext.web.RoutingContext;

public interface PreHookHandler {
    public void handle(RoutingContext routingContext, String[] params);
}
