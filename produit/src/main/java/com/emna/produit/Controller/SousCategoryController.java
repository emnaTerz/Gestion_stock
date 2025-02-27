package com.emna.produit.Controller;

import com.emna.jwt_service.Service.ServiceImpl.JwtServiceImpl;
import com.emna.produit.DAO.CategoryResquest;
import com.emna.produit.DAO.SousCategoryRequest;
import com.emna.produit.DTO.UserActionHistory;
import com.emna.produit.DTO.UserDTO;
import com.emna.produit.Entities.Category;
import com.emna.produit.Entities.SousCategory;
import com.emna.produit.FeignClient.UserClient;
import com.emna.produit.Service.CategoryService;
import com.emna.produit.Service.SousCategoryService;
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
public class SousCategoryController {
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private UserClient userClient;

    private final SousCategoryService souscategoryService;
    private final CategoryService categoryService;


    public SousCategoryController(SousCategoryService souscategoryService, CategoryService categoryService) {
        this.souscategoryService = souscategoryService;
        this.categoryService = categoryService;
    }


    @PostMapping("/createsouscategory")
    public ResponseEntity<?> createSousCategory(@RequestBody SousCategoryRequest sousCategoryResquest, HttpServletRequest request) {
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


            SousCategory savedSousCategory = souscategoryService.CreateCategory(sousCategoryResquest);
            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a ajouté la sous-categorie: " + sousCategoryResquest.getName(),
                        "/createSousCategory",
                        "POST");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSousCategory);


        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Sous Category creation failed: " + e.getMessage());
        }
    }
    @DeleteMapping("/deleteSous/{id}")
    public ResponseEntity<?> deleteSouscategory(@PathVariable Integer id, HttpServletRequest request) {
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
            String souscategoryName = souscategoryService.GetCategory(id).getName();
            souscategoryService.deleteCategory(id);
            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a supprimer la Sous categorie: " + souscategoryName,
                        "/deleteSousCategory",
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
                    .body("Sous Category delete failed: " + e.getMessage());
        }
    }
    @GetMapping("/getsous/{id}")
    public ResponseEntity<?> getsousCategory(@PathVariable Integer id, HttpServletRequest request) {
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
            SousCategory sous_category = souscategoryService.GetCategory(id);

            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a affiché la sous categorie: " + sous_category.getName(),
                        "/getsousCategory",
                        "GET");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(sous_category);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Sous Category get failed: " + e.getMessage());
        }
    }

    @GetMapping("/getsous")
    public ResponseEntity<?> getAllsousCategories( HttpServletRequest request) {
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
            List<SousCategory> souscategories = souscategoryService.GetAllCategories();

            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a affiché tous les sous categories",
                        "/getsousALLCategory",
                        "GET");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(souscategories);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("All Sous Categories get failed: " + e.getMessage());
        }
    }
    @GetMapping("/getsousbyid/{id}")
    public ResponseEntity<?> getAllsousCategoriesbyid( HttpServletRequest request,@PathVariable Integer id) {
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
            List<SousCategory> souscategories = souscategoryService.GetAllsousCategoriesbyid(id);

            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a affiché tous les sous categories de la catégorie  " ,
                        "/getsousALLCategory",
                        "GET");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(souscategories);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("All Sous Categories get failed: " + e.getMessage());
        }
    }

    @PutMapping("/updatesouscategory/{id}")
    public ResponseEntity<?> updateSousCategory(@RequestBody SousCategoryRequest categoryResquest, @PathVariable Integer id, HttpServletRequest request) {
        System.out.println("La nouvelle categorie des le debut du controlleur" + " " + categoryResquest.getName() );

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

            SousCategory existingCategory = souscategoryService.GetCategory(id);
            if (existingCategory == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sous Category not found");}
            System.out.println("la nouvelle categorie juste avant l'appelle du service  " + categoryResquest.getName());
            SousCategory updatedSouscategory = souscategoryService.UpdateCategory(categoryResquest,id);
            System.out.println("la nouvelle categorie apres l'appelle du service  " + updatedSouscategory.getName());
            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a modifié la sous categorie: " + categoryResquest.getName(),
                        "/updateCategory",
                        "PUT");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(updatedSouscategory);


        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Category update failed: " + e.getMessage());
        }
    }

}
