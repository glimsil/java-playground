package br.com.poc.rabbitmq.consumer;

import br.com.poc.rabbitmq.config.ListenerProperties;
import br.com.poc.rabbitmq.config.ListenerPropertiesBuilder;
import br.com.poc.rabbitmq.config.QueueListener;
import br.com.poc.rabbitmq.dto.UserDto;

public class UserConsumer extends QueueListener<UserDto, Void> {

    @Override
    protected ListenerProperties getProperties() {
        return ListenerPropertiesBuilder.create()
                .exchange("test-exchange")
                .queue("user-test-queue")
                .routingKey("user")
                .build();
    }

    @Override
    protected Void run(UserDto data) {
        UserDto userDto = data;
        System.out.println(" . . . processing user " + userDto.getId());
        return null;
    }


}
