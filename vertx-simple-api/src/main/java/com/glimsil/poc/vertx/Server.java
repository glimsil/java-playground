package com.glimsil.poc.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

public class Server extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Server());
	}

	@Override
	public void start() {

		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());
		router.post("/json/message").handler(this::handleMessage);
		router.get("/hello/world").handler(this::handleHelloWorld);

		vertx.createHttpServer().requestHandler(router).listen(8080);
	}

	private void handleMessage(RoutingContext routingContext) {
		JsonObject body = routingContext.getBodyAsJson();
		Message bodyMessage = body.mapTo(Message.class);
		HttpServerResponse response = routingContext.response();
		if (bodyMessage == null) {
			sendError(400, response);
		} else {
			JsonObject messageJson = JsonObject.mapFrom(Service.handleMessage(bodyMessage.getMessage()));
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
