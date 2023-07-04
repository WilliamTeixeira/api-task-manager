package com.example.taskmanager.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserReplaceDTO(@NotBlank Long id, @NotBlank String username, @NotBlank String password, @NotBlank String roles) {
    public UserReplaceDTO(User user) {
        this(user.getId(), user.getUsername(), user.getPassword(), user.getRoles());
    }
}
