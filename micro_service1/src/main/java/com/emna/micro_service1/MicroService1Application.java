package com.emna.micro_service1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.emna.micro_service1", "com.emna.jwt_service"})
public class MicroService1Application {

	public static void main(String[] args) {
		SpringApplication.run(MicroService1Application.class, args);
	}

}
