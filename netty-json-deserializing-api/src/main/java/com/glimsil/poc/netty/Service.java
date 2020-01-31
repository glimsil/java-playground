package com.glimsil.poc.netty;

public class Service {
	
	public static String getResult() {
		return "Hello World!";
	}

	public static Message handleMessage(String message) {
		Message msg = new Message();
		msg.setMessage("Response: " + message);
		return msg;
	}
}
