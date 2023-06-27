package com.example.taskmanager.configuration.security;

import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //get the token passed in the request
        var tokenJWT = retrieveToken(request);

        //authentication if token exists
        if(tokenJWT != null){
            //extracts the username present in the token
            var username = tokenService.getSubject(tokenJWT);
            //recover user by username
            var user = userRepository.findByUsername(username);
            //create authentication instance
            var authentication = new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
            //force authentication in spring
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        filterChain.doFilter(request, response);
    }

    private String retrieveToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", "").trim();
        }

        return null;
    }
}
