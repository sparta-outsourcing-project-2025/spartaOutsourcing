package com.example.spartaoutsourcing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpartaOutsourcingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpartaOutsourcingApplication.class, args);
	}

}
