package com.emna.client_fournisseur.FeignClient;

import com.emna.client_fournisseur.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice1", url = "http://localhost:8080")
public interface UserClient {
    @GetMapping("/api/email/{email}")
    UserDTO getUserByEmail(@PathVariable String email);

}