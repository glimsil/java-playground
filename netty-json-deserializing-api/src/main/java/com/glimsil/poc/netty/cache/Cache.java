package com.glimsil.poc.netty.cache;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;

public class Cache {
	
	// REACTIVE
	// private static final RedisReactiveCommands<String, String> commands;
	
	// ASYNC
	// private static final RedisAsyncCommands<String, String> commands;
	
	// SYNC
	private static final RedisCommands<String, String> commands;
	
	static {
		// REACTIVE
		// commands = RedisClient.create("redis://password@localhost:6379/0").connect().reactive();
		
		// ASYNC
		// commands = RedisClient.create("redis://password@localhost:6379/0").connect().async();
		
		// SYNC
		commands = RedisClient.create("redis://password@localhost:6379/0").connect().sync();
	}
	
	
	/*
	 * REACTIVE
	 * 
	public static Mono<String> get(String key) {
		return commands.get(key);
	}*/
	
	/*
	 * ASYNC
	 * 
	public static RedisFuture<String> get(String key) {
		return commands.get(key);
	}*/
	
	/*
	 * SYNC
	 */
	public static String get(String key) {
		return commands.get(key);
	}

}
