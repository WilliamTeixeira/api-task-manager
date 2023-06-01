package com.example.taskmanager.domain.person;

import com.example.taskmanager.domain.address.AddressDTO;

public record PersonDetailDTO(Long id, String name, String email, AddressDTO address) {
    public PersonDetailDTO(Person person){
        this(person.getId(), person.getName(), person.getEmail(), new AddressDTO(person.getAddress()));
    }
}
