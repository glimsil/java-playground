package com.glimsil.poc.netty;

public class Service {
	
	public static String getHelloWorld() {
		return "Hello World!";
	}

	public static Message handleMessage(String message) {
		Message msg = new Message();
		msg.setMessage(message);
		return msg;
	}

}
