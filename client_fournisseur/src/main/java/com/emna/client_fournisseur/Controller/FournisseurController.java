package com.emna.client_fournisseur.Controller;

import com.emna.client_fournisseur.DTO.UserActionHistory;
import com.emna.client_fournisseur.DTO.UserDTO;
import com.emna.client_fournisseur.FeignClient.UserClient;
import com.emna.client_fournisseur.Service.FournisseurService;
import com.emna.client_fournisseur.entites.Fournisseur;
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

@RestController
@RequestMapping("/client")
public class FournisseurController {
private final FournisseurService fournisseurService;
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private UserClient userClient;


    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @PostMapping ("/createFournisseur")
    ResponseEntity <?> createFournisseur (@RequestBody Fournisseur fournisseur, HttpServletRequest request)

    {
        try {
        String token = jwtService.getTokenFromRequest(request);
        if (token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing Token");
        }
        String username = jwtService.extractUserName(token);
        if (username == null || username.isEmpty())
        {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token : cannot extract user.");}
        UserDTO userDTO = null;
       try {
             userDTO = userClient.getUserByEmail(username);
        System.out.println("Received User from Feign: " + userDTO);}
        catch (Exception e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found in micro_service1.");
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDTO.getEmail(),
                "", // No password needed for validation
                authorities
        );
        if (! jwtService.isTokenValid(token,userDetails)) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
        }

        if (userDTO == null || userDTO.getEmail() == null || userDTO.getRole() == null)
        {return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user data received from micro_service1.");}

               Fournisseur savedFournisseur = fournisseurService.createFournisseur(fournisseur);

                try {
                    UserActionHistory userActionHistory = new UserActionHistory (username,
                            username + "a ajouter un nouveau fournisseur " + fournisseur.getFirstName() + " " + fournisseur.getLastName() ,
                            "POST" ,
                            "createFournisseur");
                    System.out.println("Sending User Action History: " + userActionHistory);
                    userClient.saveUserAction(userActionHistory);
                } catch (Exception e) {
                    System.out.println("Cannot log userHistory: " + e.getMessage());
                }

                return ResponseEntity.status(HttpStatus.CREATED).body(savedFournisseur);

    } catch (
    ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token has expired, please log in again.");
    } catch (
    MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid token format.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Fournisseur creation failed: " + e.getMessage());
    }}

   @GetMapping ("/getfournisseur/{id}")
    ResponseEntity <?> getFournisseur (@PathVariable Integer id, HttpServletRequest request)
    {
        try {
            String token = jwtService.getTokenFromRequest(request);
            if (token == null){
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid  token");
            }
         String username = jwtService.extractUserName(token);
            if (username == null || username.isEmpty())
            {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token :  cannot extract user");
            }
            UserDTO userDTO = null;

            try {
                userDTO = userClient.getUserByEmail(username);
                System.out.println("Received User from Feign: " + userDTO);}
            catch (Exception e) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("user not found in micro_service1.");
            }

            if (userDTO == null || userDTO.getEmail() == null|| userDTO.getRole() == null)
                { return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user data received from micro_service1.");}
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    userDTO.getEmail(),
                    "", // No password needed for validation
                    authorities
            );
            if (!jwtService.isTokenValid(token,userDetails)){
                return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Invalid");
            }

       Fournisseur fournisseur = fournisseurService.getFournisseurByID(id);
       if (fournisseur == null){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fournisseur not found");
       }
            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,username + "à visiter le profil du fournisseur  " + fournisseur.getFirstName() + " " +fournisseur.getLastName(),
                        "/GET", "getFournisseur");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
        return ResponseEntity.ok(fournisseur);
    } catch (
                ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (
                MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Get fournisseur failed: " + e.getMessage());
        }}
@GetMapping ("/getfournisseurs")
    ResponseEntity<?> GetAllFournissuers (HttpServletRequest request) {
    try {
        String token = jwtService.getTokenFromRequest(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }
        String username = jwtService.extractUserName(token);
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token : cannot extract username");
        }
        UserDTO userDTO = null;
        try {
            userDTO = userClient.getUserByEmail(username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Admin user not found in micro_service1.");
        }
        if (userDTO == null || userDTO.getEmail() == null || userDTO.getRole() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user data received from micro_service1.");
        }
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDTO.getEmail(),
                "", // No password needed for validation
                authorities
        );
        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");
        }
        List<Fournisseur> fournisseurs = fournisseurService.getFournisseur();
        try {
            UserActionHistory userActionHistory = new UserActionHistory(
                    username,
                    username + " à visiter la liste des fournisseurs",
                    "GET",
                    "GetAllFournissuers");
            System.out.println("Sending User Action History: " + userActionHistory); // Debugging Log
            userClient.saveUserAction(userActionHistory);
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error logging user history");
        }

        return ResponseEntity.ok(fournisseurs);


    } catch (ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token has expired, please log in again.");
    } catch (MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid token format.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("get Fournisseurs failed: " + e.getMessage());
    }

}
@PutMapping("/updateFournisseur/{id}")
    ResponseEntity<?> updateFournisseur (@PathVariable Integer id,@RequestBody Fournisseur fournisseur, HttpServletRequest request)
{
    try {
        String token = jwtService.getTokenFromRequest(request);
        if (token == null) {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or Invalid Token");}
        String username = jwtService.extractUserName(token);
        if (username == null || username.isEmpty()) {ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token : cannot extrat username");}
        UserDTO userDTO = null;
         try {
             userDTO = userClient.getUserByEmail(username);
             System.out.println("UserDTO : " + userDTO);
         } catch (Exception e) {return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin user not found in micro_service1.");}
         if (userDTO == null || userDTO.getEmail() == null || userDTO.getRole() == null) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user data received from micro_service1.");}
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDTO.getEmail(),
                "", // No password needed for validation
                authorities
        );
    if (!jwtService.isTokenValid(token,userDetails)) {ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");}

        Fournisseur existingFournisseur = fournisseurService.getFournisseurByID(id);
    if (existingFournisseur == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fournisseur Not found");
    }
        existingFournisseur = fournisseurService.updateFournisseur(fournisseur,id);
        try {
            UserActionHistory userActionHistory = new UserActionHistory(
                    username,
                    username + " à modifier le compte du fournisseur " + existingFournisseur.getFirstName() + " " + existingFournisseur.getLastName(),
                    "PUT" ,
                    "updateFournisseur"); // Debugging Log
            userClient.saveUserAction(userActionHistory);
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error logging user history");
        }

   return ResponseEntity.ok(existingFournisseur);

} catch (ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token has expired, please log in again.");
    } catch (MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid token format.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("update Fournisseurs failed: " + e.getMessage());
    }}


    @DeleteMapping ("/Delete/{id}")

    ResponseEntity<?> DeletFournisseur (@PathVariable Integer id, HttpServletRequest request)
    {
        try{
            String token = jwtService.getTokenFromRequest(request);
            if (token == null) {ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or Invalid token");}
            String username = jwtService.extractUserName(token);
            if (username == null || username.isEmpty()) { ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token : cannot extract username");}
            UserDTO userDTO = null;
            try {
                userDTO = userClient.getUserByEmail(username);
                System.out.println("UserDTO : " + userDTO );

            }catch (Exception e) {return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin user not found in micro_service1.");}
            if (userDTO == null || userDTO.getEmail() == null|| userDTO.getRole() == null ) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user data received from micro_service1.");}
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    userDTO.getEmail(),
                    "", // No password needed for validation
                    authorities
            );
            if (!jwtService.isTokenValid(token,userDetails)) { return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");}

            Fournisseur existingFournisseur = fournisseurService.getFournisseurByID(id);

            if (existingFournisseur == null) {ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fournisseur not found");}

            fournisseurService.DeleteFournisseur(id);
            try {
                UserActionHistory userActionHistory = new UserActionHistory(
                        username,
                        username + " à supprimer le compte du fournisseur " + existingFournisseur.getFirstName() + " " + existingFournisseur.getLastName(),
                        "DELETE" ,
                        "DeletFournisseur");
                userClient.saveUserAction(userActionHistory);
            } catch (Exception e) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error logging user history");
            }
            return ResponseEntity.ok("Fournisseur deleted succefully");

        }catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Delete Fournisseurs failed: " + e.getMessage());
        }}


}
