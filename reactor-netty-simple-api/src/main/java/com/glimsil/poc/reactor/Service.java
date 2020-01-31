package com.glimsil.poc.reactor;

public class Service {
	
	public static String getHelloWorld() {
		return "Hello World!";
	}

	public static Message handleMessage(Message message) {
		Message msg = new Message();
		msg.setMessage("Response: " + message.getMessage());
		return msg;
	}

}
