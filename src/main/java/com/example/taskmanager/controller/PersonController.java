package com.example.taskmanager.controller;

import com.example.taskmanager.domain.address.AddressDTO;
import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("persons")
public class PersonController {

    @GetMapping
    public PersonDetailDTO list(){
        var adress = new AddressDTO("36015000","Rua Espirito Santo","Centro","Juiz de Fora","MG",null,"",397);
        var dto = new PersonDetailDTO(1l,"William Teixeira","email@email.com.br", adress);

        return dto;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid PersonCreateDTO dto, UriComponentsBuilder uriBuilder){
        Person person = new Person(dto);
        //repository.save(person);

        var uri =uriBuilder.path("/person/{id}").buildAndExpand(person.getId()).toUri();

        return ResponseEntity.created(uri).body(new PersonDetailDTO(person));
    }
}
