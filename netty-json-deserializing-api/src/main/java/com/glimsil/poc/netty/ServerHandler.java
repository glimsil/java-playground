package com.glimsil.poc.netty;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glimsil.poc.netty.service.MessageService;

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

@Component
public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

	private final MessageService svc;
    private final ObjectMapper mapper;
    
    public ServerHandler(final MessageService svc, final ObjectMapper mapper) {
		this.svc = svc;
		this.mapper = mapper;
	}

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest req) throws Exception {
        final URI uri = new URI(req.uri());
        if("/hello/world".equals(uri.getPath()) && req.method() == HttpMethod.GET) {
            if (HttpUtil.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(svc.getResult().getBytes()));
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
            Message message = mapper.readValue(json, Message.class);
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(mapper.writeValueAsString(svc.handleMessage(message.getMessage())).getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else if (uri.getPath().startsWith("/json/message/") && req.method() == HttpMethod.GET) {
        	if (HttpUtil.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
        	String message = uri.getPath().substring("/json/message/".length());
        	FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(mapper.writeValueAsString(svc.findMessage(message)).getBytes()));
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
				svc.getFromCache(key).subscribe(value -> {
				FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes()));
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
				ctx.writeAndFlush(response);
				ctx.close();
			});*/
        	
        	/*
        	 * ASYNC
        	 * 
				svc.getFromCache(key).thenAccept(value -> {
        		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes()));
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
				ctx.writeAndFlush(response);
				ctx.close();
        	});*/
        	
        	/*
        	 * SYNC
        	 */
        	FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(svc.getFromCache(key).getBytes()));
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    	LOGGER.error(cause.getMessage(), cause);
        ctx.close();
    }

}
