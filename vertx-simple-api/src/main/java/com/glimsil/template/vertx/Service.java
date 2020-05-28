package com.glimsil.template.vertx;

public class Service {
	
	public static String getHelloWorld() {
		return "Hello World!";
	}

	public static Message handleMessage(String message) {
		Message msg = new Message();
		msg.setMessage("Response: " + message);
		return msg;
	}

}
