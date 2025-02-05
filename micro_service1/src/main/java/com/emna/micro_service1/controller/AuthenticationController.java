package com.emna.micro_service1.controller;

import com.emna.micro_service1.dao.request.SignUpRequest;
import com.emna.micro_service1.dao.request.SigninRequest;
import com.emna.micro_service1.dao.response.JwtAuthenticationResponse;
import com.emna.micro_service1.entities.User;
import com.emna.micro_service1.exeption.UserNotFoundException;
import com.emna.micro_service1.repository.UserRepository;
import com.emna.micro_service1.service.AuthenticationService;
import com.emna.micro_service1.service.UserService;
import com.emna.micro_service1.service.impl.JwtServiceImpl;
import com.emna.micro_service1.service.impl.TokenBlacklistService;
import com.emna.micro_service1.service.impl.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;
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



  /*  @PostMapping("/signin")
   public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
       try {
           JwtAuthenticationResponse response = authenticationService.signin(request);
           System.out.println("Authentication successful, token generated: " + response.getToken());
           return ResponseEntity.ok(response);
       } catch (Exception e) {
           System.out.println("Authentication failed: " + e.getMessage());
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
       }
   }*/
  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody SigninRequest request) {
      try {
          JwtAuthenticationResponse response = authenticationService.signin(request);
          System.out.println("Authentication successful, token generated: " + response.getToken());
          return ResponseEntity.ok(response);
      } catch (IllegalArgumentException e) {
          System.out.println("Authentication failed: Invalid email or password.");
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                  .body(Map.of("error", "Invalid email or password"));
      } catch (Exception e) {
          System.out.println("Authentication failed: " + e.getMessage());
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(Map.of("error", "An unexpected error occurred. Please try again later."));
      }
  }





    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        try {
            // Extraire le token via JwtServiceImpl
            String token = jwtService.getTokenFromRequest(request);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
            }

            // Extraire et valider le username
            String username = jwtService.extractUserName(token);
            if (!jwtService.isTokenValid(token, new UserDetailsImpl(username))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
            }

            // If the token is valid, proceed to get the user by ID
            User user = userService.getUserById(id);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // User not found
            }

            return ResponseEntity.ok(user);  // Return the user if valid

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

   @GetMapping
   public  ResponseEntity<?>  getAllUsers(HttpServletRequest request) {

       try {
           // Extraire le token via JwtServiceImpl
           String token = jwtService.getTokenFromRequest(request);
           if (token == null) {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
           }

           // Extraire et valider le username
           String username = jwtService.extractUserName(token);
           if (!jwtService.isTokenValid(token, new UserDetailsImpl(username))) {
               return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
           }

       // If the token is valid, fetch all users
       List<User> users = userService.getAllUsers();
       return ResponseEntity.ok(users);
       } catch (ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired, please log in again.");
    } catch (MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }}

  /*  @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user ) {
        System.out.println("user" +user);
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }*/
  @PutMapping("/{id}")
  public ResponseEntity<?> updateUser(
          @PathVariable Integer id,
          @RequestBody User updatedUser,
           HttpServletRequest request) {
      try {
          // Extraire le token via JwtServiceImpl
          String token = jwtService.getTokenFromRequest(request);
          if (token == null) {
              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token.");
          }

          // Extraire et valider le username
          String username = jwtService.extractUserName(token);
          if (!jwtService.isTokenValid(token, new UserDetailsImpl(username))) {
              return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
          }
          // Vérifier si l'utilisateur existe avant de le mettre à jour
          Optional<User> existingUser = userRepository.findById(id);
          if (existingUser.isEmpty()) {
              throw new UserNotFoundException("User not found.");
          }
      return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    } catch (ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired, please log in again.");
    } catch (MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }}

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
