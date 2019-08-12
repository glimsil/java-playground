package br.com.poc.jms.consumer;

import br.com.poc.jms.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer implements ApplicationRunner {

    @Autowired
    private JmsTemplate jmsTemplate;


    @JmsListener(destination = "user", containerFactory = "myFactory")
    public void onReceiverTopic(String userDto) {
        System.out.println("topic user: " + userDto);
    }


    @Override
    public void run(ApplicationArguments args) {
        UserDto userDto = new UserDto();
        userDto.setId("0001");
        userDto.setIdade(28);
        userDto.setNome("Joao");
        jmsTemplate.convertAndSend("user", userDto);
    }
}