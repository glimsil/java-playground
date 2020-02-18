package com.glimsil.poc.lettuce;

import com.glimsil.poc.lettuce.model.Person;
import com.glimsil.poc.lettuce.service.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunnerMapper(ApplicationContext ctx) {
    return args -> {
        PersonService personService = ctx.getBean(PersonService.class);
        System.out.println(personService.create(new Person("hey")));
        Person person = new Person("Second Person");
        person.setId("123");
        personService.create(person);
        System.out.println("Person: " + personService.get("123"));
        System.out.println("All Person: " + personService.getAll());
        };
    }

}
