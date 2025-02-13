package com.emna.client_fournisseur.FeignClient;

import com.emna.client_fournisseur.DTO.UserDTO;
import com.emna.client_fournisseur.DTO.UserActionHistory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservice1", url = "http://localhost:8080")
public interface UserClient {
    @GetMapping("/api/email/{email}")
    UserDTO getUserByEmail(@PathVariable String email);

    @PostMapping("/api/historique/add")

    ResponseEntity<Void> saveUserAction(@RequestBody UserActionHistory userActionHistory);


}