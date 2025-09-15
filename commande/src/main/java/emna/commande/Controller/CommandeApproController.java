package emna.commande.Controller;


import com.emna.jwt_service.Service.ServiceImpl.JwtServiceImpl;
import emna.commande.DTO.UserActionHistory;
import emna.commande.DTO.UserDTO;
import emna.commande.FeignClient.UserClient;
import emna.commande.Service.CommandeApproService;
import emna.commande.dao.CommandeApproRequest;
import emna.commande.dao.CommandeApproResponse;
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
@RequestMapping("/commande")
public class CommandeApproController {
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private UserClient userClient;
    private final CommandeApproService commandeApproService;

    public CommandeApproController(CommandeApproService commandeApproService) {
        this.commandeApproService = commandeApproService;
    }

    @PostMapping("/createCommande")
    ResponseEntity<?> createCommande (@RequestBody CommandeApproRequest commandeAppro, HttpServletRequest request)

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

            CommandeApproResponse commande  = commandeApproService.save(commandeAppro, token);

            try {
                UserActionHistory userActionHistory = new UserActionHistory (username,
                        username + "a ajouter une nouvelle commande du " + commande.getFournisseur() + "d'un montant " + commande.getPrixTotal() ,
                        "POST" ,
                        "createCommande");
                System.out.println("Sending User Action History: " + userActionHistory);
                userClient.saveUserAction(userActionHistory);
            }catch (Exception e) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found in micro_service1.");
            }


            return ResponseEntity.status(HttpStatus.CREATED).body(commande);

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
                    .body("Commande creation failed: " + e.getMessage());
        }}

    @GetMapping("/getCommandeAppro/{id}")
    ResponseEntity <?> getCommandeAppro (@PathVariable Integer id , HttpServletRequest request) {

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


            CommandeApproResponse commande = commandeApproService.getById(id,token);

            try {
                UserActionHistory actionHistory = new UserActionHistory(
                        username,
                        "L'utilisateur: " + username + " a Affiché  la commande du fournisseur : " + commande.getFournisseur() ,
                        "/getCommandeAppro",
                        "Get");

                System.out.println("Sending User Action History: " + actionHistory); // Debugging Log

                userClient.saveUserAction(actionHistory);

            } catch (Exception e) {
                System.out.println("Error logging user action: " + e.getMessage());
            }
            return ResponseEntity.ok(commande);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token has expired, please log in again.");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid token format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("get commande failed: " + e.getMessage());
        }
    }
}
