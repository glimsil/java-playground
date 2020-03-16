package com.glimsil.poc.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {
	
	private final static int PORT = 8080;
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	
	public static void main(String[] args) {
		final AnnotationConfigApplicationContext appCtxt = new AnnotationConfigApplicationContext(AppConfig.class);
		final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		final EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			final ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.DEBUG))
					.childHandler(appCtxt.getBean(ServerInitializer.class));
			final Channel ch = b.bind(PORT).sync().channel();
			LOGGER.info("Application served at port " + PORT);
			ch.closeFuture().sync();
		} catch (final InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			appCtxt.close();
			LOGGER.info("Application down");
		}
	}
	
}
