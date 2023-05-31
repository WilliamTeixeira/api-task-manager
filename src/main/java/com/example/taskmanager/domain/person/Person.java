package com.example.taskmanager.domain.person;

import com.example.taskmanager.domain.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "persons")
@Entity(name = "Person")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @Embedded
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

