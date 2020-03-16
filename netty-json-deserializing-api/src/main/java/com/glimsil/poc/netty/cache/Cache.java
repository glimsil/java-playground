package com.glimsil.poc.netty.cache;

import org.springframework.stereotype.Component;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;

@Component
public class Cache {
	
	// REACTIVE
	// private final RedisReactiveCommands<String, String> commands;
	
	// ASYNC
	// private final RedisAsyncCommands<String, String> commands;
	
	// SYNC
	private final RedisCommands<String, String> commands;
	
	{
		// REACTIVE
		// commands = RedisClient.create("redis://localhost:6379/0").connect().reactive();
		
		// ASYNC
		// commands = RedisClient.create("redis://localhost:6379/0").connect().async();
		
		// SYNC
		commands = RedisClient.create("redis://localhost:6379/0").connect().sync();
	}
	
	
	/*
	 * REACTIVE
	 * 
	public Mono<String> get(String key) {
		return commands.get(key);
	}*/
	
	/*
	 * ASYNC
	 * 
	public RedisFuture<String> get(String key) {
		return commands.get(key);
	}*/
	
	/*
	 * SYNC
	 */
	public String get(String key) {
		return commands.get(key);
	}

}
