package com.emna.client_fournisseur.config;

import com.emna.client_fournisseur.FeignClient.UserClient;
import com.emna.client_fournisseur.DTO.UserDTO;
import com.emna.jwt_service.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserClient userClient;  // Feign Client pour appeler le 1er microservice

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
                // 1️⃣ Call Feign Client to get UserDTO from microservice1
                UserDTO userDTO = userClient.getUserByEmail(userEmail);
                System.out.println("Received UserDTO from Feign: " + userDTO);

                // 2️⃣ Convert UserDTO to UserDetails
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()));

                UserDetails userDetails = new User(
                        userDTO.getEmail(),
                        "", // No password needed for authentication
                        authorities
                );

                // 3️⃣ Validate JWT with new UserDetails
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("✅ Authenticated user " + userEmail + ", setting security context");
                } else {
                    System.out.println("❌ Invalid token for user: " + userEmail);
                }
            } catch (Exception e) {
                System.out.println("❌ Error fetching user from micro_service1: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
