package br.com.poc.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public abstract class QueueListener<T, Y> {

    private ConsumerRegistry registry = ConsumerRegistry.getInstance();

    private AmqpConnection amqpConnection = new AmqpConnection();

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void register() {
        try {
            QueueListener<T, Y> queueListener = (QueueListener<T, Y>) this.getClass().newInstance();
            registry.register(queueListener);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private Class<T> getInputType() {
        try {
            return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (ClassCastException e) {
            return (Class<T>) ((ParameterizedType) getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
    }

    protected abstract ListenerProperties getProperties();

    protected abstract Y run(T data);

    public void start() throws IOException {
        final Channel channel = amqpConnection.getChannel();
        final ListenerProperties listenerProperties = getProperties();
        final String queueName = listenerProperties.getQueue();
        final String exchange = listenerProperties.getExchange();
        final String routingKey = listenerProperties.getRoutingKey();
        final String deadLetterQueue = queueName + "-dead-letter-queue";

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println("Receiveing message from exchange: " + listenerProperties.getExchange()
                            + ", routingKey: " + listenerProperties.getRoutingKey()
                            + ", correlationId: " + properties.getCorrelationId());
                    System.out.println("Converting message bytes to object.");
                    final T requestBody = objectMapper.readValue(body, getInputType());
                    System.out.println("Converted object: " + requestBody);
                    run(requestBody);
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                    channel.basicPublish("", deadLetterQueue, null, objectMapper.writeValueAsBytes(body));
                } finally {
                    System.out.println("Message received and processed successfully.");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.exchangeDeclare(exchange, "topic");
        channel.queueDeclare(queueName, true, false, false, deadletterConf(queueName));
        channel.queueDeclare(deadLetterQueue, true, false, false, lazyConf(queueName));
        channel.queueBind(queueName, exchange, routingKey);
        channel.basicQos(200);
        channel.basicConsume(queueName, false, consumer);
    }

    private Map<String, Object> deadletterConf(String queueName) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "");
        arguments.put("x-dead-letter-routing-key", queueName + "-dead-letter-queue");
        return arguments;
    }

    private Map<String, Object> lazyConf(String queueName) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-queue-mode", "lazy");
        return arguments;
    }
}

