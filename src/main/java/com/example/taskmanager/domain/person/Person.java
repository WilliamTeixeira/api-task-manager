package com.example.taskmanager.domain.person;

import com.example.taskmanager.domain.address.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Long id;
    private String name;
    private String email;
    private Address address;

    public Person(PersonCreateDTO dto){
        this.name = dto.name();
        this.email = dto.email();
        this.address = new Address(dto.address());
    }
    public Person(PersonDetailDTO dto){
        this.id = dto.id();
        this.name = dto.name();
        this.email = dto.email();
        this.address = new Address(dto.address());
    }
    public void setNexId(Long nexId) {
        this.id = nexId + 1l;
    }
}
