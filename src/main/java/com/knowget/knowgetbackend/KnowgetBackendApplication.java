package com.knowget.knowgetbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KnowgetBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowgetBackendApplication.class, args);
	}

}
