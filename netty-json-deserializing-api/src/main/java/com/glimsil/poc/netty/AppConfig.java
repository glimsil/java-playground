package com.glimsil.poc.netty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glimsil.poc.netty.service.MessageService;

import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

@Configuration
@ComponentScan("com.glimsil.poc.netty")
public class AppConfig {
	
	@Bean
	@Scope("prototype")
	public HttpServerCodec codec() {
		return new HttpServerCodec();
	}
	
	@Bean
	@Scope("prototype")
	public HttpObjectAggregator aggregator() {
		return new HttpObjectAggregator(Short.MAX_VALUE);
	}
	
	@Bean
	@Scope("prototype")
	public ServerHandler handler(final MessageService svc, final ObjectMapper mapper) {
		return new ServerHandler(svc, mapper);
	}
	
	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}

}
