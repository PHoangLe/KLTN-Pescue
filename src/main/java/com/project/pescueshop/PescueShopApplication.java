package com.project.pescueshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.project.pescueshop.model.entity")
public class PescueShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PescueShopApplication.class, args);
	}

}
