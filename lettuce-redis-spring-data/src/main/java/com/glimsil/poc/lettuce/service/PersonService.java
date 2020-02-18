package com.glimsil.poc.lettuce.service;

import com.glimsil.poc.lettuce.model.Person;
import com.glimsil.poc.lettuce.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private RedisOperations<String,Object> redis;

    @Autowired
    private PersonRepository personRepository;


    public Person create(Person person) {
        return personRepository.save(person);
    }

    public Optional<Person> get(String id) {
        return personRepository.findById(id);
    }

    public Iterable<Person> getAll() {
        return personRepository.findAll();
    }

    /*private String getNextPublicationId() {
        return redis.opsForValue().increment("publicationIds").toString();
    }*/

}
