package com.example.taskmanager.mock;

import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

public class PersonServiceMock {

    public static PersonDetailDTO save() {
        return new PersonDetailDTO(PersonRepositoryMock.createValidPerson());
    }
    public static PersonDetailDTO replace(){
        return new PersonDetailDTO(PersonRepositoryMock.createValidPerson());
    }

    public static PersonDetailDTO findBy() {
        return new PersonDetailDTO(PersonRepositoryMock.createValidPerson());
    }

    public static Page<PersonDetailDTO> listAll(){
        PageImpl<Person> personPage = new PageImpl<>(PersonRepositoryMock.createListOfValidPerson());
        return personPage.map(PersonDetailDTO::new);
    }
    public static Page<PersonDetailDTO> listEmpty(){
        PageImpl<Person> personPage = new PageImpl<>(Collections.emptyList());
        return personPage.map(PersonDetailDTO::new);
    }

}
