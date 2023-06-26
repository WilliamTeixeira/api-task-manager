package com.example.taskmanager.domain.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(@NotBlank String username, @NotBlank String password) {
}
