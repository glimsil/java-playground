package br.com.poc.rabbitmq.consumer;


import br.com.poc.rabbitmq.dto.TransactionDto;
import br.com.poc.rabbitmq.dto.UserDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitConsumer {

    @RabbitListener(queues = "user-test-queue")
    public void handleUserMessage(UserDto userDto) {
        System.out.println(" . . . processing user " + userDto.getId());
        System.out.println(userDto);
    }

    @RabbitListener(queues = "transaction-test-queue")
    public void handleTransactionMessage(TransactionDto transactionDto) {
        System.out.println(" . . . processing transaction " + transactionDto.getId());
        System.out.println(transactionDto);
    }
}