package com.emna.produit.Controller;

import com.emna.jwt_service.Service.ServiceImpl.JwtServiceImpl;

import com.emna.produit.DTO.UserActionHistory;
import com.emna.produit.DTO.UserDTO;
import com.emna.produit.Entities.Attribute;

import com.emna.produit.FeignClient.UserClient;
import com.emna.produit.Service.AttributeService;

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





@RestController
@RequestMapping("/product")
public class AttributeController {
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private UserClient userClient;

    private final AttributeService attributeService;


    public AttributeController( AttributeService attributeService) {
        this.attributeService = attributeService;
;
    }

    @PostMapping("/createattribute")
    public ResponseEntity<?> createAttribute(@RequestBody Attribute AttributeResquest, HttpServletRequest request) {
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


            Attribute savedAttribute = attributeService.createattribute(AttributeResquest);
            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a ajouté l'attribut : " + AttributeResquest.getName(),
                        "/createAttribute",
                        "POST");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(AttributeResquest);


        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Attribute creation failed: " + e.getMessage());
        }
    }



    @DeleteMapping("/deleteatt/{id}")
    public ResponseEntity<?> deleteattribute(@PathVariable Integer id, HttpServletRequest request) {
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
            String attributename = attributeService.getattribute(id).getName();

            attributeService.deleteattribute(id);
            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a supprimer l'attribut: " + attributename,
                        "/deleteAttribute",
                        "DELET");

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
                    .body("Attribute delete failed: " + e.getMessage());
        }
    }
    @GetMapping("/getatt/{id}")
    public ResponseEntity<?> getAttribute(@PathVariable Integer id, HttpServletRequest request) {
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
            Attribute att = attributeService.getattribute(id);

            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a affiché l'attribut : " + att.getName(),
                        "/getAttribute",
                        "GET");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(att);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Category get failed: " + e.getMessage());
        }
    }

    @GetMapping("/getatt")
    public ResponseEntity<?> getAllAttributes( HttpServletRequest request) {
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

            List<Attribute> attributes = attributeService.getattributes();

            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a affiché tous les attributs",
                        "/getALLAttributes",
                        "GET");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(attributes);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("All Categories get failed: " + e.getMessage());
        }
    }

    @PutMapping("/updateatt/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody Attribute attributeResquest,@PathVariable Integer id, HttpServletRequest request) {


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
            Attribute existingAtt = attributeService.getattribute(id);
            if (existingAtt == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Category not found");}


            Attribute updatedattribute = attributeService.updateattribute(attributeResquest, id);
            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a modifié l'attribut: " + attributeResquest.getName(),
                        "/updateAttribute",
                        "PUT");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(updatedattribute);


        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Attribute update failed: " + e.getMessage());
        }
    }

}
