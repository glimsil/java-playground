package com.glimsil.poc.netty;

import com.glimsil.poc.netty.db.dao.MessageDao;

public class Service {
	
	public static String getResult() {
		return "Hello World!";
	}

	public static Message handleMessage(String message) {
		Message msg = new Message();
		msg.setMessage("Response: " + message);
		return msg;
	}
	
	public static com.glimsil.poc.netty.db.entity.Message findMessage(String message) {
		return MessageDao.findByMessage(message);
	}
}
