package br.com.poc.rabbitmq.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AmqpConnection {

    private Connection connection;

    private Channel channel;

    public AmqpConnection() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        try {
            this.connection = connectionFactory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            this.connection = null;
        }
    }

    public Channel getChannel() throws IOException {
        if(channel == null || !channel.isOpen()) {
            this.channel = getConnection().createChannel();
        }
        return channel;
    }

    public Connection getConnection() {
        return connection;
    }

}