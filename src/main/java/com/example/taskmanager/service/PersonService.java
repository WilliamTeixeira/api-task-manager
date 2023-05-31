package com.example.taskmanager.service;

import com.example.taskmanager.domain.address.AddressDTO;
import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    private static List<PersonDetailDTO> personList;

    static {
        AddressDTO adress = new AddressDTO("36015000","Rua Espirito Santo","Centro","Juiz de Fora","MG",null,"","397");
        personList =  new ArrayList<>(List.of(
                new PersonDetailDTO(1l, "William Teixeira", "email@email.com.br", adress),
                new PersonDetailDTO(2l, "Flavia Carvalho", "email@email.com.br", adress)));
    }
    public List<PersonDetailDTO> listAll(){
        return personList;
    }

    public PersonDetailDTO findById(Long id) {
        return personList.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person not found"));

    }

    public Person save(PersonCreateDTO personCreateDTO) {
        Long nexId = (long) personList.size();

        Person newPerson = new Person(personCreateDTO);
        newPerson.setNexId(nexId);

        personList.add(new PersonDetailDTO(newPerson));

        return newPerson;
    }

    public void delete(Long id) {
        personList.remove(findById(id));
    }

}
