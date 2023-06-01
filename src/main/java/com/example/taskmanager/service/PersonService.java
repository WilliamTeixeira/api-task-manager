package com.example.taskmanager.service;

import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import com.example.taskmanager.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public Page<PersonDetailDTO> listAll(Pageable pagination){
        return repository.findAll(pagination)
                .map(PersonDetailDTO::new);
    }

    public PersonDetailDTO findById(Long id) {
        return repository.findById(id)
                .map(PersonDetailDTO::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person not found"));
    }

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

}
