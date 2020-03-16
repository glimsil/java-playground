package com.glimsil.poc.reactor.service;

import org.springframework.stereotype.Service;

import com.glimsil.poc.reactor.Message;
import com.glimsil.poc.reactor.cache.Cache;
import com.glimsil.poc.reactor.db.dao.MessageDao;

import reactor.core.publisher.Mono;

@Service
public class MessageService {
	
	private final MessageDao dao;
	private final Cache cache;
	
	public MessageService(final MessageDao dao, final Cache cache) {
		this.dao = dao;
		this.cache = cache;
	}
	
	public String getHelloWorld() {
		return "Hello World!";
	}

	public Message handleMessage(String message) {
		final Message msg = new Message();
		msg.setMessage("Response: " + message);
		return msg;
	}
	
	public com.glimsil.poc.reactor.db.entity.Message findMessage(String message) {
		return dao.findByMessage(message);
	}
	
	public Mono<String> getFromCache(String key) {
		return cache.get(key);
	}

}
