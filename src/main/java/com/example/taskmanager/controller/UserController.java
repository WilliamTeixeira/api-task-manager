package com.example.taskmanager.controller;

import com.example.taskmanager.domain.user.UserDetailDTO;
import com.example.taskmanager.domain.user.UserReplaceDTO;
import com.example.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid UserReplaceDTO userReplaceDTO){
        UserDetailDTO user = userService.replace(userReplaceDTO);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDetailDTO> findByIde(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDetailDTO> findByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }


}
