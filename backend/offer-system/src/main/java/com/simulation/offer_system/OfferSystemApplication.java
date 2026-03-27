package com.simulation.offer_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages= {"com.simulation.offer_system"})
@EnableJpaRepositories(basePackages= {"com.simulation.offer_system"})
@EntityScan(basePackages= {"com.simulation.offer_system"})
@SpringBootApplication
public class OfferSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfferSystemApplication.class, args);
	}

}
