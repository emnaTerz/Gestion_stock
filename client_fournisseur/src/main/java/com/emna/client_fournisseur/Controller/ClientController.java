package com.emna.client_fournisseur.Controller;

import com.emna.client_fournisseur.DTO.UserActionHistory;
import com.emna.client_fournisseur.DTO.UserDTO;
import com.emna.client_fournisseur.FeignClient.UserClient;
import com.emna.client_fournisseur.Service.ClientService;
import com.emna.client_fournisseur.dao.Request.ClientRequest;
import com.emna.client_fournisseur.entites.Client;
import com.emna.jwt_service.Service.ServiceImpl.JwtServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/client")
public class ClientController {
    private final ClientService clientService;
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private UserClient userClient;
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


   @PostMapping("/createClient")
   public ResponseEntity<?> createClient(@RequestBody ClientRequest clientRequest, HttpServletRequest request) {
       try {

           String token = jwtService.getTokenFromRequest(request);

           if (token == null) {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body("Missing or invalid token.");
           }

           String username = jwtService.extractUserName(token);
           if (username == null || username.isEmpty()) {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                       .body("Invalid token: cannot extract user.");
           }
           UserDTO userDTO;
           try {
               userDTO = userClient.getUserByEmail(username);
               System.out.println("Received User from Feign: " + userDTO);
           } catch (Exception e) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("user not found in micro_service1.");
           }

           if (userDTO == null || userDTO.getEmail() == null || userDTO.getRole() == null) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("Invalid user data received from micro_service1.");
           }

           List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

           UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                   userDTO.getEmail(),
                   "", // No password needed for validation
                   authorities
           );

           if (!jwtService.isTokenValid(token, userDetails)) {
               return ResponseEntity.status(HttpStatus.FORBIDDEN)
                       .body("Invalid token.");
           }


           Client savedClient = clientService.AddClient(clientRequest);
           try {
               UserActionHistory actionHistory = new UserActionHistory(
                       username,
                       "L'utilisateur: " + username + " a ajouté le client: " + clientRequest.getFirstName() + " " + clientRequest.getLastName(),
                       "/createClient",
                       "POST");

               System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

               userClient.saveUserAction(actionHistory);

           } catch (Exception e) {
               System.out.println("Error logging user action: " + e.getMessage());
           }
           return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);


       } catch (ExpiredJwtException e) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                   .body("Token has expired, please log in again.");
       } catch (MalformedJwtException e) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN)
                   .body("Invalid token format.");
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Client creation failed: " + e.getMessage());
       }
   }
@GetMapping("/allClients")

    public ResponseEntity<?> getallclients (HttpServletRequest request) {

    try {

        String token = jwtService.getTokenFromRequest(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid token.");
        }

        String username = jwtService.extractUserName(token);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token: cannot extract user.");
        }
        UserDTO userDTO;
        try {
            userDTO = userClient.getUserByEmail(username);
            System.out.println("Received User from Feign: " + userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin user not found in micro_service1.");
        }

        if (userDTO == null || userDTO.getEmail() == null || userDTO.getRole() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid user data received from micro_service1.");
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDTO.getEmail(),
                "", // No password needed for validation
                authorities
        );

        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token.");
        }


        List <Client> clients = clientService.getAllClients();

        try {
            UserActionHistory actionHistory = new UserActionHistory(
                    username,
                    "L'utilisateur: " + username + " a Affiché tt les clients: " ,
                    "/AllClients",
                    "Get");

            System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

            userClient.saveUserAction(actionHistory);

        } catch (Exception e) {
            System.out.println("Error logging user action: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(clients);


    } catch (ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token has expired, please log in again.");
    } catch (MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid token format.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Client creation failed: " + e.getMessage());
    }
}
@GetMapping("/getClient/{id}")
ResponseEntity <?> getClient (@PathVariable Integer id , HttpServletRequest request) {

    try {

        String token = jwtService.getTokenFromRequest(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid token.");
        }

        String username = jwtService.extractUserName(token);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token: cannot extract user.");
        }
        UserDTO userDTO;
        try {
            userDTO = userClient.getUserByEmail(username);
            System.out.println("Received User from Feign: " + userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin user not found in micro_service1.");
        }

        if (userDTO == null || userDTO.getEmail() == null || userDTO.getRole() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid user data received from micro_service1.");
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDTO.getEmail(),
                "", // No password needed for validation
                authorities
        );

        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token.");
        }


        Client client = clientService.getClientById(id);

        try {
            UserActionHistory actionHistory = new UserActionHistory(
                    username,
                    "L'utilisateur: " + username + " a Affiché  le client: " + client.getFirstName() + " " + client.getLastName(),
                    "/getClient",
                    "Get");

            System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

            userClient.saveUserAction(actionHistory);

        } catch (Exception e) {
            System.out.println("Error logging user action: " + e.getMessage());
        }
        return ResponseEntity.ok(client);

    } catch (ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token has expired, please log in again.");
    } catch (MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid token format.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("get client failed: " + e.getMessage());
    }
}
@PutMapping("/updateClient/{id}")
    ResponseEntity<?> updateClient (@PathVariable Integer id, @RequestBody  Client newClient, HttpServletRequest request) {
    try {

        String token = jwtService.getTokenFromRequest(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid token.");
        }

        String username = jwtService.extractUserName(token);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token: cannot extract user.");
        }
        UserDTO userDTO;
        try {
            userDTO = userClient.getUserByEmail(username);
            System.out.println("Received User from Feign: " + userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin user not found in micro_service1.");
        }

        if (userDTO == null || userDTO.getEmail() == null || userDTO.getRole() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid user data received from micro_service1.");
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDTO.getEmail(),
                "", // No password needed for validation
                authorities
        );

        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token.");
        }

            // Vérifier si l'utilisateur existe avant de le mettre à jour
        Client existingClient = clientService.getClientById(id);
    if (existingClient == null){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Client not found");}

  clientService.updateClient(id,newClient);

        try {
            UserActionHistory actionHistory = new UserActionHistory(
                    username,
                    "L'utilisateur: " + username + " a modifier  le client: " + existingClient.getFirstName() + " " + existingClient.getLastName(),
                    "/updateClient",
                    "UPDATE");

            System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

            userClient.saveUserAction(actionHistory);

        } catch (Exception e) {
            System.out.println("Error logging user action: " + e.getMessage());
        }
    return ResponseEntity.ok(existingClient);
    } catch (ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token has expired, please log in again.");
    } catch (MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid token format.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Client creation failed: " + e.getMessage());
    }
}

@DeleteMapping ("/deleteClient/{id}")

    ResponseEntity<?> deleteClient (@PathVariable Integer id, HttpServletRequest request)
{

    try {

        String token = jwtService.getTokenFromRequest(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid token.");
        }

        String username = jwtService.extractUserName(token);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token: cannot extract user.");
        }
        UserDTO userDTO;
        try {
            userDTO = userClient.getUserByEmail(username);
            System.out.println("Received User from Feign: " + userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin user not found in micro_service1.");
        }

        if (userDTO == null || userDTO.getEmail() == null || userDTO.getRole() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid user data received from micro_service1.");
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDTO.getEmail(),
                "", // No password needed for validation
                authorities
        );

        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token.");
        }
Client c = clientService.getClientById(id);
        if (c == null){
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Client not found");
}
clientService.deleteClient(id);

        try {
            UserActionHistory actionHistory = new UserActionHistory(
                    username,
                    "L'utilisateur: " + username + " a supprimer  le client: " + c.getFirstName() + " " + c.getLastName(),
                    "/deleteClient",
                    "DELETE");

            System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

            userClient.saveUserAction(actionHistory);

        } catch (Exception e) {
            System.out.println("Error logging user action: " + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    } catch (ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token has expired, please log in again.");
    } catch (MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid token format.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Client creation failed: " + e.getMessage());
    }
}
}

