package com.example.taskmanager.domain.person;

import com.example.taskmanager.domain.address.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonCreateDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull @Valid AddressDTO address) {
}
