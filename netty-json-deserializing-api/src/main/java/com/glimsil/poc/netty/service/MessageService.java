package com.glimsil.poc.netty.service;

import org.springframework.stereotype.Service;

import com.glimsil.poc.netty.Message;
import com.glimsil.poc.netty.cache.Cache;
import com.glimsil.poc.netty.db.dao.MessageDao;

@Service
public class MessageService {
	
	private final MessageDao dao;
	private final Cache cache;
	
	public MessageService(final MessageDao dao, final Cache cache) {
		this.dao = dao;
		this.cache = cache;
	}
	
	public String getResult() {
		return "Hello World!";
	}

	public Message handleMessage(String message) {
		final Message msg = new Message();
		msg.setMessage("Response: " + message);
		return msg;
	}
	
	public com.glimsil.poc.netty.db.entity.Message findMessage(String message) {
		return dao.findByMessage(message);
	}
	
	/*
	 * REACTIVE
	 * 
	public Mono<String> getFromCache(String key) {
		return cache.get(key);
	}*/
	
	/*
	 * ASYNC
	 * 
	public RedisFuture<String> getFromCache(String key) {
		return cache.get(key);
	}*/
	
	/*
	 * SYNC
	 */
	public String getFromCache(String key) {
		return cache.get(key);
	}
}
