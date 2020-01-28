package com.poc.servicetalk;

import io.servicetalk.http.netty.HttpServers;

import static io.servicetalk.concurrent.api.Single.failed;
import static io.servicetalk.concurrent.api.Single.succeeded;
import static io.servicetalk.http.api.HttpSerializationProviders.textSerializer;

public final class Server {

    public static void main(String[] args) throws Exception {
        HttpServers.forPort(8080)
                .listenAndAwait((ctx, request, responseFactory) -> {
                    if("/hello/world".equals(request.path())) {
                        return succeeded(responseFactory.ok()
                                .payloadBody("Hello World!", textSerializer()));
                    }
                    return succeeded(responseFactory.notFound());
                })
                .awaitShutdown();
    }
}