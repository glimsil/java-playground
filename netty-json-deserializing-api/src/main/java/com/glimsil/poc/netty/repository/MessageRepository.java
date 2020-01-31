package com.glimsil.poc.netty.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.glimsil.poc.netty.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
	
	@Query("SELECT message FROM message m WHERE m.message = :message")
	Message findByMessage(@Param("message") String message);
	
}
