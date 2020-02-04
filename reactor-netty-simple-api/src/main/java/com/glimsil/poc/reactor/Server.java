package com.glimsil.poc.reactor;

import java.nio.charset.Charset;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class Server {
	
	private static final Gson GSON = new Gson();
	private static final Charset CHARSET = Charset.forName("UTF-8");
	
	public static void main(String[] args) {
		DisposableServer server = HttpServer.create()
				.route(routes -> routes
						.get("/hello/world", (request, response) -> response.sendString(Mono.fromCallable(Service::getHelloWorld)))
						.post("/json/message", (request, response) -> response.send(request
								.receive()
								.aggregate()
								.map(byteBuf -> byteBuf.toString(CHARSET))
								.map(Service::handleMessage)
								.map(Server::fromObject)))
						.get("/json/message/{message}", (request, response) -> response.send(
								Mono.fromCallable(() -> Service.findMessage(request.param("message")))
								.map(Server::fromObject))))
				.host("localhost")
				.port(8080)
				.bindNow();
		server.onDispose().block();
	}
	
	private static ByteBuf fromObject(Object obj) {
		return Unpooled.wrappedBuffer(GSON.toJson(obj).getBytes(CHARSET));
	}
	
}
