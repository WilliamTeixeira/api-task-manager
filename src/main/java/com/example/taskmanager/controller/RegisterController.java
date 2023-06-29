package com.example.taskmanager.controller;

import com.example.taskmanager.domain.user.UserCreateDTO;
import com.example.taskmanager.domain.user.UserDetailDTO;
import com.example.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@RestController
@RequestMapping("register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid UserCreateDTO userCreateDTO, UriComponentsBuilder uriBuilder){
        UserDetailDTO user = userService.save(userCreateDTO);

        var uri = uriBuilder.path("user/id/{id}").buildAndExpand(user.id()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

}
