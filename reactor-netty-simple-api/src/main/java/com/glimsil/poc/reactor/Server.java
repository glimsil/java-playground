package com.glimsil.poc.reactor;

import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.glimsil.poc.reactor.service.MessageService;
import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class Server {
	
	private static final Gson GSON = new Gson();
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	
	public static void main(String[] args) {
		final AnnotationConfigApplicationContext appCtxt = new AnnotationConfigApplicationContext("com.glimsil.poc.reactor");
		final MessageService svc = appCtxt.getBean(MessageService.class);
		final DisposableServer server = HttpServer.create()
				.route(routes -> routes
						.get("/hello/world", (request, response) -> response.sendString(Mono.fromCallable(svc::getHelloWorld)))
						.post("/json/message", (request, response) -> response.send(request
								.receive()
								.aggregate()
								.map(byteBuf -> byteBuf.toString(CHARSET))
								.map(svc::handleMessage)
								.map(Server::fromObject)))
						.get("/json/message/{message}", (request, response) -> response.send(
								Mono.fromCallable(() -> svc.findMessage(request.param("message")))
								.map(Server::fromObject)))
						.get("/lettuce/{key}", (request, response) -> response.sendString(
								svc.getFromCache(request.param("key")))))
				.host("localhost")
				.port(8080)
				.bindNow();
		LOGGER.info("Application up");
		final CountDownLatch cdl = new CountDownLatch(1);
		server.onDispose(() -> {
			appCtxt.close();
			LOGGER.info("Application down");
			cdl.countDown();
		});
		try {
			cdl.await();
		} catch (final InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	private static ByteBuf fromObject(Object obj) {
		return Unpooled.wrappedBuffer(GSON.toJson(obj).getBytes(CHARSET));
	}
	
}
