package com.example.taskmanager.domain.person;

import com.example.taskmanager.domain.address.Address;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "persons")
@Entity(name = "Person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Person {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}

