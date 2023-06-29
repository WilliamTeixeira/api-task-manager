package com.example.taskmanager.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateDTO(@NotBlank String username,@NotBlank String password,@NotBlank String roles) {
}
