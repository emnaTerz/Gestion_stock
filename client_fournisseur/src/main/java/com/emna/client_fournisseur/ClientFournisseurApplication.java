package com.emna.client_fournisseur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients

@SpringBootApplication(scanBasePackages = {"com.emna.client_fournisseur", "com.emna.jwt_service"})

public class ClientFournisseurApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientFournisseurApplication.class, args);
	}

}
