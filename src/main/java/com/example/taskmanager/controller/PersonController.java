package com.example.taskmanager.controller;

import com.example.taskmanager.domain.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("persons")
public class PersonController {

    @GetMapping
    public List<Person> list(){
        return List.of(new Person("Fulano"), new Person("Ciclano"));
    }
}
