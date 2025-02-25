package com.emna.produit.config;

import com.emna.produit.FeignClient.UserClient;
import com.emna.produit.DTO.UserActionHistory;
import com.emna.produit.DTO.UserDTO;
import com.emna.jwt_service.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserClient userClient;
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserClient userClient) {
        this.jwtService = jwtService;
        this.userClient = userClient;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String jwt = jwtService.getTokenFromRequest(request);
        final String userEmail;

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        userEmail = jwtService.extractUserName(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Fetch user details from client_fournisseur microservice
                UserDTO userDTO = userClient.getUserByEmail(userEmail);
                System.out.println("Received UserDTO from Feign: " + userDTO);

                // Convert UserDTO to UserDetails
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));
                User userDetails = new User(userDTO.getEmail(), "", authorities);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authenticated user " + userEmail + ", setting security context");

                    // Log user authentication success
                    userClient.saveUserAction(new UserActionHistory(
                            userEmail,
                            "Authentication Success",
                            request.getRequestURI(),
                            request.getMethod()
                    ));
                } else {
                    System.out.println("Invalid token for user: " + userEmail);
                }
            } catch (Exception e) {
                System.out.println("Error fetching user from client_fournisseur: " + e.getMessage());

                // Log user authentication failure
                userClient.saveUserAction(new UserActionHistory(
                        userEmail,
                        "Authentication Failure",
                        request.getRequestURI(),
                        request.getMethod()
                ));
            }
        }

        filterChain.doFilter(request, response);
    }
}
