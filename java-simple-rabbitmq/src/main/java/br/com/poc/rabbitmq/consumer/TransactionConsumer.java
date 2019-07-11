package br.com.poc.rabbitmq.consumer;

import br.com.poc.rabbitmq.config.ListenerProperties;
import br.com.poc.rabbitmq.config.ListenerPropertiesBuilder;
import br.com.poc.rabbitmq.config.QueueListener;
import br.com.poc.rabbitmq.dto.TransactionDto;

public class TransactionConsumer extends QueueListener<TransactionDto, Void> {

    @Override
    protected ListenerProperties getProperties() {
        return ListenerPropertiesBuilder.create()
                .exchange("test-exchange")
                .queue("transaction-test-queue")
                .routingKey("transaction")
                .build();
    }

    @Override
    protected Void run(TransactionDto data) {
        TransactionDto transactionDto = data;
        System.out.println(" . . . processing transaction " + transactionDto.getId());
        return null;
    }


}
