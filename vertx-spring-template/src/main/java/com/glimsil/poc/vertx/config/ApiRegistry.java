package com.glimsil.poc.vertx.config;

import io.vertx.ext.web.Route;

import java.util.ArrayList;
import java.util.List;

public class ApiRegistry {

    private static ApiRegistry instance = null;

    private ApiRegistry() {

    }

    public static synchronized ApiRegistry getInstance() {
        if(null == instance) {
            instance = new ApiRegistry();
        }
        return instance;
    }

    private final List<Endpoint> endpoints = new ArrayList<>();

    public void register(Endpoint endpoint) {
        new Route()
        endpoints.add(endpoint);
    }

    public final List<Endpoint> get() {
        return endpoints;
    }
}