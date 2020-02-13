package com.glimsil.poc.netty.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.glimsil.poc.netty.db.entity.Message;

public class MessageDao {
	
	private static final JdbcTemplate jdbcTemplate;
	
	static {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql:benchmark");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres");
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public static Message findByMessage(String message) {
		RowMapper<Message> mapper = (rs, rowNum) -> new Message(rs.getInt("id"), rs.getString("message"));
		return jdbcTemplate.queryForObject("SELECT * FROM message m WHERE m.message = ?", mapper, message);
	}
	
}
