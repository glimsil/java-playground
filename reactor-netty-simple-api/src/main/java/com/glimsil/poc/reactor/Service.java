package com.glimsil.poc.reactor;

import com.glimsil.poc.reactor.db.dao.MessageDao;

public class Service {
	
	public static String getHelloWorld() {
		return "Hello World!";
	}

	public static Message handleMessage(String message) {
		Message msg = new Message();
		msg.setMessage("Response: " + message);
		return msg;
	}
	
	public static com.glimsil.poc.reactor.db.entity.Message findMessage(String message) {
		return MessageDao.findByMessage(message);
	}

}
