package com.emna.produit.Controller;

import com.emna.jwt_service.Service.ServiceImpl.JwtServiceImpl;
import com.emna.produit.DAO.CategoryResquest;
import com.emna.produit.DTO.UserActionHistory;
import com.emna.produit.DTO.UserDTO;
import com.emna.produit.Entities.Category;
import com.emna.produit.FeignClient.UserClient;
import com.emna.produit.Service.CategoryService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class CategoryController {

    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private UserClient userClient;

    private final CategoryService categoryService;

    public CategoryController( CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    @PostMapping("/createcategory")
    public ResponseEntity<?> createClient(@RequestBody CategoryResquest categoryResquest, HttpServletRequest request) {
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


            Category savedClient = categoryService.CreateCategory(categoryResquest);
            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a ajouté la categorie: " + categoryResquest.getName(),
                        "/createCategory",
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
                    .body("Category creation failed: " + e.getMessage());
        }
    }
}
