package com.glimsil.poc.lettuce.repository;

import com.glimsil.poc.lettuce.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {}

