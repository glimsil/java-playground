package com.glimsil.poc.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpServerCodec());
        p.addLast(new ServerHandler());
    }
}
