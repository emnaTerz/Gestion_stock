package com.emna.micro_service1.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api")
public class JwtController {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @GetMapping("/public-key")
    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(jwtSigningKey.getBytes());
    }
}
