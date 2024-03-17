package com.ivarc29.pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class IvarPizzaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(IvarPizzaBackendApplication.class, args);
	}

}
