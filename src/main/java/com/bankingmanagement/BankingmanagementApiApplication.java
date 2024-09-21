package com.bankingmanagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Slf4j
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.bankingmanagement.repository") // JPA repositories
@EnableMongoRepositories(basePackages = "com.bankingmanagement.mongoRepository") // MongoDB repositories
public class BankingmanagementApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(BankingmanagementApiApplication.class, args);
		log.info("Banking Management Application has been started");
	}

}
