package com.example.taskmanager.service;

import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import com.example.taskmanager.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public PersonDetailDTO save(PersonCreateDTO personCreateDTO) {
        Person newPerson = repository.save(new Person(personCreateDTO));

        return new PersonDetailDTO(newPerson);
    }
    public void delete(Long id) {
        Person person = repository.getReferenceById(id);
        repository.delete(person);
    }
    public PersonDetailDTO replace(PersonDetailDTO personDetailDTO){
        Person savedPerson = repository.getReferenceById(personDetailDTO.id());

        Person personToSave = new Person(personDetailDTO);
        personToSave.setId(savedPerson.getId());

        Person newPersonSaved = repository.save(personToSave);

        return new PersonDetailDTO(newPersonSaved);
    }

    public PersonDetailDTO findById(Long id) {
        return new PersonDetailDTO(repository.getReferenceById(id));
    }
    public PersonDetailDTO findByName(String name) {
        Person personFound = repository.findByName(name);

        if(personFound == null)
            throw new EntityNotFoundException("Person Not Found");

        return new PersonDetailDTO(personFound);
    }
    public PersonDetailDTO findByEmail(String email) {
        Person personFound = repository.findByEmail(email);

        if(personFound == null)
            throw new EntityNotFoundException("Person Not Found");

        return new PersonDetailDTO(personFound);
    }
    public Page<PersonDetailDTO> listAll(Pageable pageable){
        return repository.findAll(pageable)
                .map(PersonDetailDTO::new);
    }
    public Page<PersonDetailDTO> listAllByName(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable)
                .map(PersonDetailDTO::new);
    }
    public Page<PersonDetailDTO> listAllByEMail(String email, Pageable pageable) {
        return repository.findByEmailContainingIgnoreCase(email, pageable)
                .map(PersonDetailDTO::new);
    }

}
