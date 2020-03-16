package com.glimsil.poc.netty;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

@Component
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
	
	private final HttpServerCodec codec;
	private final HttpObjectAggregator aggregator;
	private final ServerHandler handler;
	
	public ServerInitializer(final HttpServerCodec codec, final HttpObjectAggregator aggregator, final ServerHandler handler) {
		this.codec = codec;
		this.aggregator = aggregator;
		this.handler = handler;
	}
	
    @Override
    public void initChannel(final SocketChannel ch) {
        final ChannelPipeline p = ch.pipeline();
        p.addLast(codec);
        p.addLast(aggregator);
        p.addLast(handler);
    }
}
