package com.glimsil.poc.netty.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import com.glimsil.poc.netty.db.entity.Message;

@Component
public class MessageDao {
	
	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<Message> mapper;
	
	public MessageDao() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql:benchmark");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres");
		jdbcTemplate = new JdbcTemplate(dataSource);
		mapper = (rs, rowNum) -> new Message(rs.getInt("id"), rs.getString("message"));
	}
	
	public Message findByMessage(String message) {
		return jdbcTemplate.queryForObject("SELECT * FROM message m WHERE m.message = ?", mapper, message);
	}
	
}
