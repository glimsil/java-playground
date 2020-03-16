package com.glimsil.poc.reactor.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.glimsil.poc.reactor.db.entity.Message;

public class MessageDao {
	
	private static final JdbcTemplate jdbcTemplate;
	private static final RowMapper<Message> mapper;
	
	static {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql:benchmark");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres");
		jdbcTemplate = new JdbcTemplate(dataSource);
		mapper = (rs, rowNum) -> new Message(rs.getInt("id"), rs.getString("message"));
	}
	
	public static Message findByMessage(String message) {
		return jdbcTemplate.queryForObject("SELECT * FROM message m WHERE m.message = ?", mapper, message);
	}
	
}
