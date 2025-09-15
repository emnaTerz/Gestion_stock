package com.emna.micro_service1.controller;

import com.emna.jwt_service.Service.ServiceImpl.JwtServiceImpl;
import com.emna.micro_service1.DTO.UserDTO;
import com.emna.micro_service1.dao.request.SignUpRequest;
import com.emna.micro_service1.dao.request.SigninRequest;
import com.emna.micro_service1.dao.response.JwtAuthenticationResponse;
import com.emna.micro_service1.entities.User;
import com.emna.micro_service1.entities.UserActionHistory;
import com.emna.micro_service1.exeption.UserNotFoundException;
import com.emna.micro_service1.repository.UserActionHistoryRepository;
import com.emna.micro_service1.repository.UserRepository;
import com.emna.micro_service1.service.AuthenticationService;
import com.emna.micro_service1.service.UserService;
import com.emna.micro_service1.service.impl.TokenBlacklistService;
import com.emna.micro_service1.service.impl.UserActionService;
import com.emna.micro_service1.service.impl.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    private final UserActionService userActionService;
    private final UserActionHistoryRepository userActionHistoryRepository;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    private JwtServiceImpl jwtService;


/* @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody SignUpRequest request,
            HttpServletRequest httpRequest) {

        try {
            //  Extraire le token depuis l'en-tête Authorization
            String token = jwtService.getTokenFromRequest(httpRequest);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid token.");
            }

            // Extraire le username à partir du token
            String username = jwtService.extractUserName(token);
            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid token: cannot extract user.");
            }

            // Vérifier si le token est valide
            User adminUser = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException("Admin user not found."));
            if (!jwtService.isTokenValid(token, new UserDetailsImpl(username))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Invalid token.");
            }


            // Créer le nouvel utilisateur
            authenticationService.signup(request);
            System.out.println("User registration successful.");

            userActionService.logUserAction(username, "l'utilisateur:" + username + " a créer l'utilisateur: " + request.getEmail(), "/createUser" , "POST");

            //  Retourner la réponse
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User created successfully.");

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed: " + e.getMessage());
        }
    }*/


    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody SignUpRequest request,
            HttpServletRequest httpRequest) {

        try {


            // Créer le nouvel utilisateur
            authenticationService.signup(request);
            System.out.println("User registration successful.");

           // userActionService.logUserAction(username, "l'utilisateur:" + username + " a créer l'utilisateur: " + request.getEmail(), "/createUser" , "POST");

            //  Retourner la réponse
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User created successfully.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed: " + e.getMessage());
        }
    }
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
            String UserName = user.getUsername();
            // Supprimer l'utilisateur
            userService.getUserById(id);
            userActionService.logUserAction(username, "l'utilisateur:" + username + " a visiter le profil de l'utilisateur: " + UserName, "/getuser" + id, "GET");

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
           userActionService.logUserAction(username, "Afficher tt les utilisateurs", "/getAllUsers", "GET");
       return ResponseEntity.ok(users);
       } catch (ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired, please log in again.");
    } catch (MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }}

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
          String modifiedUserName = existingUser.get().getUsername();
          userActionService.logUserAction(username, "l'utilisateur:" + username + " a modifié l'utilisateur: " + modifiedUserName, "/updateUser" + id, "UPDATE");
      return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    } catch (ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired, please log in again.");
    } catch (MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }}


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Integer id,
            HttpServletRequest request) {
        try {
            // Extraire le token depuis l'en-tête Authorization
            String token = jwtService.getTokenFromRequest(request);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid token.");
            }

            // Extraire le username à partir du token
            String username = jwtService.extractUserName(token);
            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid token: cannot extract user.");
            }

            // Vérifier si le token est valide
            User adminUser = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException("Admin user not found."));
            if (!jwtService.isTokenValid(token, new UserDetailsImpl(username))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Invalid token.");
            }

            // Vérifier si l'utilisateur à supprimer existe
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found.");
            }
           String deletedUserName = userRepository.findById(id).get().getUsername();
            // Supprimer l'utilisateur
            userService.deleteUser(id);
            userActionService.logUserAction(username, "l'utilisateur:" + username + " a Supprimé l'utilisateur: " + deletedUserName, "/deleteuser" + id, "DELETE");

            return ResponseEntity.noContent().build();


        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = jwtService.getTokenFromRequest(request);
        if (token != null && !token.isEmpty()) {
            tokenBlacklistService.addTokenToBlacklist(token);
        }
        return ResponseEntity.ok("Logged out successfully");
    }
    @GetMapping("/action-history")
    public ResponseEntity<?> getActionHistory(
            HttpServletRequest request) {

        try {
            // Extraire le token depuis l'en-tête Authorization
            String token = jwtService.getTokenFromRequest(request);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid token.");
            }

            // Extraire le username à partir du token
            String username = jwtService.extractUserName(token);
            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid token: cannot extract user.");
            }

            // Vérifier si le token est valide
            User adminUser = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException("Admin user not found."));
            if (!jwtService.isTokenValid(token, new UserDetailsImpl(username))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Invalid token.");
            }

            // Fetch the action history if the token is valid and authorized
            List<UserActionHistory> history = userActionHistoryRepository.findAll();
            return ResponseEntity.ok(history);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(u -> ResponseEntity.ok(new UserDTO(
                        u.getEmail(), u.getFirstName(), u.getLastName(), u.getRole())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


  /*  @PostMapping("/historique/add")
    public ResponseEntity<?> createUserHistory(@RequestBody UserActionHistory userActionHistory) {
        System.out.println("Received UserActionHistory: " + userActionHistory.getAction() + ""
                + userActionHistory.getEndpoint() + "" + userActionHistory.getUsername() + "" + userActionHistory.getMethod()); // Debugging Log

        if (userActionHistory.getUsername() == null || userActionHistory.getAction() == null ||
                userActionHistory.getEndpoint() == null || userActionHistory.getMethod() == null) {
            return ResponseEntity.badRequest().body("Invalid request: Some fields are null.");
        }

        userActionService.logUserAction(
                userActionHistory.getUsername(),
                userActionHistory.getAction(),
                userActionHistory.getEndpoint(),
                userActionHistory.getMethod()
        );

        return ResponseEntity.ok("User history logged successfully.");
    }*/
  @PostMapping("/historique/add")
  public ResponseEntity<Void> createUserHistory(@RequestBody UserActionHistory userActionHistory) {
      System.out.println("Received UserActionHistory: " + userActionHistory.getAction());

      if (userActionHistory.getUsername() == null || userActionHistory.getAction() == null ||
              userActionHistory.getEndpoint() == null || userActionHistory.getMethod() == null) {
          return ResponseEntity.badRequest().build(); // No body
      }

      userActionService.logUserAction(
              userActionHistory.getUsername(),
              userActionHistory.getAction(),
              userActionHistory.getEndpoint(),
              userActionHistory.getMethod()
      );

      return ResponseEntity.ok().build(); // No body
  }



}
