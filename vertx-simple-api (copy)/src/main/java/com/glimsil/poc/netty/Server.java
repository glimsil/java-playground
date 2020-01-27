package com.glimsil.poc.netty;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

public class Server extends AbstractVerticle {
	private final static int PORT = 8080;

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Server());
	}

	private Map<String, JsonObject> products = new HashMap<>();

	@Override
	public void start() {

		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());
		router.get("/message/:message").handler(this::handleMessage);
		router.get("/hello/world").handler(this::handleHelloWorld);

		vertx.createHttpServer().requestHandler(router).listen(8080);
	}

	private void handleMessage(RoutingContext routingContext) {
		String message = routingContext.request().getParam("message");
		HttpServerResponse response = routingContext.response();
		if (message == null) {
			sendError(400, response);
		} else {
			JsonObject messageJson = JsonObject.mapFrom(Service.handleMessage(message));
			if (messageJson == null) {
				sendError(404, response);
			} else {
				response.putHeader("content-type", "application/json").end(messageJson.encodePrettily());
			}
		}
	}

	private void handleHelloWorld(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.end(Service.getHelloWorld());
	}

	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}

	
}
