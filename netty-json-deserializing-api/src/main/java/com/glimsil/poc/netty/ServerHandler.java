package com.glimsil.poc.netty;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;

public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        URI uri = new URI(req.uri());
        if("/hello/world".equals(uri.getPath()) && req.method() == HttpMethod.GET) {
            if (HttpUtil.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(Service.getResult().getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else if ("/json/message".equals(uri.getPath()) && req.method() == HttpMethod.POST) {
            if (HttpUtil.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            ByteBuf byteBuf = req.content();
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            String json = new String(bytes);
            Message message = objectMapper.readValue(json, Message.class);
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(objectMapper.writeValueAsString(Service.handleMessage(message.getMessage())).getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else if (uri.getPath().startsWith("/json/message/") && req.method() == HttpMethod.GET) {
        	if (HttpUtil.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
        	String message = uri.getPath().substring("/json/message/".length());
        	FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(objectMapper.writeValueAsString(Service.findMessage(message)).getBytes()));
        	response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        	response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        	ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else if (uri.getPath().startsWith("/lettuce/") && req.method() == HttpMethod.GET) {
        	if (HttpUtil.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
        	String key = uri.getPath().substring("/lettuce/".length());
        	
        	/*
        	 * REACTIVE
        	 * 
				Service.getFromCache(key).subscribe(value -> {
				FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes()));
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
				ctx.writeAndFlush(response);
				ctx.close();
			});*/
        	
        	/*
        	 * ASYNC
        	 * 
				Service.getFromCache(key).thenAccept(value -> {
        		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes()));
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
				ctx.writeAndFlush(response);
				ctx.close();
        	});*/
        	
        	/*
        	 * SYNC
        	 */
        	FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(Service.getFromCache(key).getBytes()));
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
