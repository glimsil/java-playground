package br.com.poc.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class RabbitMqConfig implements RabbitListenerConfigurer {

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter rabbitJsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(rabbitJsonMessageConverter);
        return template;
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("test-exchange");
    }

    @Bean
    public Queue userQueue(){
        return QueueBuilder.durable("user-test-queue")
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "user-test-queue-dead-letter-queue")
                .build();
    }

    @Bean
    public Queue userDeadLetterQueue(){
        return QueueBuilder
                .durable("user-test-queue-dead-letter-queue")
                .withArgument("x-queue-mode", "lazy")
                .build();
    }

    @Bean
    public Binding userBinding(){
        return BindingBuilder
                .bind(userQueue())
                .to(exchange())
                .with("user");
    }

    @Bean
    public Queue transactionQueue(){
        return QueueBuilder.durable("transaction-test-queue")
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "transaction-test-queue-dead-letter-queue")
                .build();
    }

    @Bean
    public Queue transactionDeadLetterQueue(){
        return QueueBuilder
                .durable("transaction-test-queue-dead-letter-queue")
                .withArgument("x-queue-mode", "lazy")
                .build();
    }

    @Bean
    public Binding transactionBinding(){
        return BindingBuilder
                .bind(transactionQueue())
                .to(exchange())
                .with("transaction");
    }
}