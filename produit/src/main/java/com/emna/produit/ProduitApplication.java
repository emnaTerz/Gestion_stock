package com.emna.produit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EntityScan("com.emna.produit.Entities")
@SpringBootApplication(scanBasePackages = {"com.emna.produit", "com.emna.jwt_service"})
public class ProduitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProduitApplication.class, args);
	}

}
