package com.emna.produit.Controller;

import com.emna.jwt_service.Service.ServiceImpl.JwtServiceImpl;
import com.emna.produit.DAO.ProductResponse;
import com.emna.produit.DAO.ProductResquest;
import com.emna.produit.DTO.UserActionHistory;
import com.emna.produit.DTO.UserDTO;
import com.emna.produit.Entities.Product;
import com.emna.produit.Entities.ProductAttribute;
import com.emna.produit.Entities.SousCategory;
import com.emna.produit.FeignClient.UserClient;
import com.emna.produit.Repository.ProductAttributeRepository;
import com.emna.produit.Service.ProductService;
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
public class ProductController {
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private UserClient userClient;

    private final SousCategoryService souscategoryService;
    private final ProductService productService;
    private final ProductAttributeRepository productAttributeRepository;



    public ProductController(SousCategoryService souscategoryService, ProductService productService, ProductAttributeRepository productAttributeRepository) {
        this.souscategoryService = souscategoryService;
        this.productService = productService;
        this.productAttributeRepository = productAttributeRepository;
    }


    @PostMapping("/createproduct")
    public ResponseEntity<?> createProduct(@RequestBody ProductResquest productResquest, HttpServletRequest request) {
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


            Product savedProduct = productService.createProduct(productResquest);
            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a ajouté la sous-categorie: " + savedProduct.getName(),
                        "/createProduct",
                        "POST");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);


        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Product creation failed: " + e.getMessage());
        }
    }
    @DeleteMapping("/deleteproduct/{id}")
    public ResponseEntity<?> deleteproduct(@PathVariable Integer id, HttpServletRequest request) {
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
            String productName = productService.getProduct(id).getName();

          List<ProductAttribute>   productAttribute = productAttributeRepository.findByProductId(id);
            for (ProductAttribute productAttributes : productAttribute) {
                productAttributeRepository.delete(productAttributes);
            }
            productService.deleteProduct(id);

            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a supprimer le produit: " + productName,
                        "/deleteProduct",
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
                    .body("Procuct delete failed: " + e.getMessage());
        }
    }
    @GetMapping("/getproduct/{id}")
    public ResponseEntity<?> getproduct(@PathVariable Integer id, HttpServletRequest request) {
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

            ProductResponse product = productService.getProduct(id);

            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a affiché le produit : " + product.getName(),
                        "/getProduct",
                        "GET");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(product);

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

    @GetMapping("/getproduct")
    public ResponseEntity<?> getAllProducts( HttpServletRequest request) {
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


            List<ProductResponse> products = productService.getProducts();

            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a affiché tous les produits",
                        "/getALLProducts",
                        "GET");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(products);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("All Products get failed: " + e.getMessage());
        }
    }

    @PutMapping("/updateproduct/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody ProductResquest productResquest,@PathVariable Integer id, HttpServletRequest request) {


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

            ProductResponse existingrod = productService.getProduct(id);
            if (existingrod == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Category not found");}


            Product updateproduct = productService.updateProduct(productResquest, id);
            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a modifié le produit: " + updateproduct.getName(),
                        "/updateProduct",
                        "PUT");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(updateproduct);


        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Product update failed: " + e.getMessage());
        }
    }


    @GetMapping("/getproductsou/{id}")
    public ResponseEntity<?> getAllProductsbysou( HttpServletRequest request,@PathVariable Integer id) {
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

            SousCategory sou = souscategoryService.GetCategory(id);
            List<ProductResponse> products = productService.getProductsBySousCategory(sou);

            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a affiché tous les produits " + sou.getName(),
                        "/getALLProducts",
                        "GET");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(products);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("All Products get failed: " + e.getMessage());
        }
    }

}



