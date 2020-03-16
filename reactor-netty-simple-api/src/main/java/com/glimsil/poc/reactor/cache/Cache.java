package com.glimsil.poc.reactor.cache;

import org.springframework.stereotype.Component;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Mono;

@Component
public class Cache {
	
	private final RedisReactiveCommands<String, String> commands = RedisClient
			.create("redis://localhost:6379/0")
			.connect()
			.reactive();
	
	public Mono<String> get(String key) {
		return commands.get(key);
	}
	
}
