package com.staynest.staynest_backend.filters;

import com.staynest.staynest_backend.security.JwtService;
import com.staynest.staynest_backend.services.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;  // service to validate credentials

    private final UserServiceImpl userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
     String request_header_token =    request.getHeader("Authorization");

     log.info("request_header_token: {}", request_header_token);

        if(request_header_token == null || !request_header_token.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = request_header_token.substring(7);

        String email = jwtService.extractUsername(token);

        UserDetails user = userService.loadUserByUsername(email);

        log.info("user: {}", user);

        if(jwtService.isTokenValid(token, user)) {

            log.info("token is valid");
            // SecurityContext
         if( SecurityContextHolder.getContext().getAuthentication() == null){
             UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
             authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

             log.info("Setting authentication in security context");
             SecurityContextHolder.getContext().setAuthentication(authToken);
         }


        }


        filterChain.doFilter(request, response);
    }
}
