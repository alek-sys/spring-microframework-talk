package com.example.demo;

import io.r2dbc.spi.ConnectionFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

	final ConnectionFactory connectionFactory;

	public DatabaseInitializer(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void run(String... args) {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(
				new ClassPathResource("schema.sql"),
				new ClassPathResource("data.sql")
		);

		databasePopulator.populate(connectionFactory).block();
	}
}
