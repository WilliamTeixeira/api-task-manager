package com.example.taskmanager.domain.person;

import com.example.taskmanager.domain.address.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonDetailDTO(Long id, String name, String email, AddressDTO address) {
    public PersonDetailDTO(Person person){
        this(person.getId(), person.getName(), person.getEmail(), new AddressDTO(person.getAddress()));
    }
}
