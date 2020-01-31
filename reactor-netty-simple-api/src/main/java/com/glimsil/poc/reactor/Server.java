package com.glimsil.poc.reactor;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class Server {

	public static void main(String[] args) {
		DisposableServer server =
				HttpServer.create()
						.route(routes ->
								routes.get("/hello/world",
										(request, response) -> response.sendString(Mono.just(Service.getHelloWorld())))
								.post("/json/message", (request, response) -> response.sendObject(Mono.just(
										Service.handleMessage(request.receive().then().cast(Message.class).block()))))
						)
						.host("localhost")
						.port(8080)
						.bindNow();
		server.onDispose()
				.block();
		Mono.just(new Message());
	}
	
}
