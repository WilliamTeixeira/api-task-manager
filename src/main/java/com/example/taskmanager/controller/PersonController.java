package com.example.taskmanager.controller;

import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import com.example.taskmanager.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@RestController
@RequestMapping("persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid PersonCreateDTO personCreateDTO, UriComponentsBuilder uriBuilder){
        PersonDetailDTO  person = personService.save(personCreateDTO);

        var uri = uriBuilder.path("person/{id}").buildAndExpand(person.id()).toUri();

        return ResponseEntity.created(uri).body(person);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id){
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid PersonDetailDTO personDetailDTO){
        PersonDetailDTO  person = personService.replace(personDetailDTO);
        return ResponseEntity.ok(person);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PersonDetailDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(personService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PersonDetailDTO> findByName(@PathVariable String name){
        return ResponseEntity.ok(personService.findByName(name));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PersonDetailDTO> findByEmail(@PathVariable String email){
        return ResponseEntity.ok(personService.findByEmail(email));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PersonDetailDTO>> listAll(@PageableDefault(size = 10, sort = {"id"})Pageable pageable){
        return ResponseEntity.ok(personService.listAll(pageable));
    }

    @GetMapping("/all/name/{name}")
    public ResponseEntity<Page<PersonDetailDTO>> listAllByName(@PathVariable String name, @PageableDefault(size = 10, sort = {"name"})Pageable pageable){
        return ResponseEntity.ok(personService.listAllByName(name, pageable));
    }

    @GetMapping("/all/email/{email}")
    public ResponseEntity<Page<PersonDetailDTO>> listAllByEmail(@PathVariable String email, @PageableDefault(size = 10, sort = {"email"})Pageable pageable){
        return ResponseEntity.ok(personService.listAllByEMail(email, pageable));
    }


}
