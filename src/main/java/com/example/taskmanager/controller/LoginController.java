package com.example.taskmanager.controller;

import com.example.taskmanager.configuration.security.TokenJwtDTO;
import com.example.taskmanager.domain.user.User;
import com.example.taskmanager.domain.user.UserLoginDTO;
import com.example.taskmanager.service.TokenService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/login")
public class LoginController {

   @Autowired
    private AuthenticationManager manager;

   @Autowired
   private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid UserLoginDTO data){
        try{
            log.info(data);
            var authenticationToken = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var authentication = manager.authenticate(authenticationToken);
            var jwtToken = tokenService.generateToken((User) authentication.getPrincipal());

            return ResponseEntity.ok(new TokenJwtDTO(jwtToken));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
}
