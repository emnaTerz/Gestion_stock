package com.emna.micro_service1.controller;

import com.emna.micro_service1.dao.request.SignUpRequest;
import com.emna.micro_service1.dao.request.SigninRequest;
import com.emna.micro_service1.dao.response.JwtAuthenticationResponse;
import com.emna.micro_service1.entities.User;
import com.emna.micro_service1.service.AuthenticationService;
import com.emna.micro_service1.service.UserService;
import com.emna.micro_service1.service.impl.JwtServiceImpl;
import com.emna.micro_service1.service.impl.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    private JwtServiceImpl jwtService;
  
   @PostMapping("/signup")
   public ResponseEntity<String> signup(@RequestBody SignUpRequest request) {
       System.out.println("Attempting to register user with email: " + request.getEmail());
       try {
           authenticationService.signup(request);
           System.out.println("User registration successful.");
           return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
       } catch (Exception e) {
           System.out.println("User registration failed: " + e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
       }
   }



    @PostMapping("/signin")
   public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
       System.out.println("Attempting to authenticate user with email: " + request.getEmail());
       System.out.println("Attempting to authenticate user with email: " + request.getPassword());
       try {
           JwtAuthenticationResponse response = authenticationService.signin(request);
           System.out.println("Authentication successful, token generated: " + response.getToken());
           return ResponseEntity.ok(response);
       } catch (Exception e) {
           System.out.println("Authentication failed: " + e.getMessage());
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
       }
   }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        System.out.println("user" +user);
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = jwtService.getTokenFromRequest(request);
        if (token != null && !token.isEmpty()) {
            tokenBlacklistService.addTokenToBlacklist(token);
        }
        return ResponseEntity.ok("Logged out successfully");
    }


}
