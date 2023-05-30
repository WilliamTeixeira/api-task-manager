package com.example.taskmanager.controller;

import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import com.example.taskmanager.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<List<PersonDetailDTO>> list(){
        return ResponseEntity.ok(personService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDetailDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid PersonCreateDTO personCreatDTO, UriComponentsBuilder uriBuilder){
        Person  person = personService.save(personCreatDTO);
        var uri =uriBuilder.path("/person/{id}").buildAndExpand(person.getId()).toUri();

        var personDetailDTO = personService.findById(person.getId());
        return ResponseEntity.created(uri).body(personDetailDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        personService.delete(id);
        return ResponseEntity.noContent().build();

    }

}
