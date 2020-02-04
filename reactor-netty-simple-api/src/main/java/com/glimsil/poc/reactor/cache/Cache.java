package com.glimsil.poc.reactor.cache;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Mono;

public class Cache {
	
	private static final RedisReactiveCommands<String, String> commands;
	
	static {
		commands = RedisClient.create("redis://password@localhost:6379/0").connect().reactive();
	}
	
	public static Mono<String> get(String key) {
		return commands.get(key);
	}
	
}
