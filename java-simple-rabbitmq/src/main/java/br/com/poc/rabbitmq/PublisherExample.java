package br.com.poc.rabbitmq;

import br.com.poc.rabbitmq.config.AmqpConnection;
import br.com.poc.rabbitmq.dto.TransactionDto;
import br.com.poc.rabbitmq.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeoutException;

public class PublisherExample {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        AmqpConnection amqpConnection = new AmqpConnection();
        //send user to 'test-exchange.user'
        amqpConnection
                .getChannel()
                .basicPublish("test-exchange", "user",
                        null, objectMapper.writeValueAsBytes(simpleUser()));

        //send transaction to 'test-exchange.transaction'
        amqpConnection
                .getChannel()
                .basicPublish("test-exchange", "transaction",
                        null, objectMapper.writeValueAsBytes(simpleTransaction()));

        try {
            amqpConnection.getChannel().close();
            amqpConnection.getConnection().close();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static UserDto simpleUser() {
        UserDto userDto = new UserDto();
        userDto.setId("001");
        userDto.setName("John");
        userDto.setAge(32);
        return userDto;
    }

    public static TransactionDto simpleTransaction() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId("000001");
        transactionDto.setFromAccount("A");
        transactionDto.setToAccount("B");
        transactionDto.setAmount(new BigDecimal(355.5));
        return transactionDto;
    }

}
