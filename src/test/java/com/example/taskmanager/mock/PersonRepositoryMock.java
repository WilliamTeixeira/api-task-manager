package com.example.taskmanager.mock;

import com.example.taskmanager.domain.address.Address;
import com.example.taskmanager.domain.person.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class PersonRepositoryMock {
    public static Person createPersonToBeSaved(){
        return Person.builder()
                .name("Fulano Example")
                .email("fulano@mail.com")
                .address(
                        Address.builder()
                                .cep("99999999")
                                .street("Street A")
                                .number("123")
                                .complement("01")
                                .neighborhood("Downtown")
                                .city("Example City")
                                .uf("MG")
                                .ibge("123456")
                                .build()
                )
                .build();
    }
    public static Person createValidPerson(){
        return Person.builder()
                .id(1l)
                .name("Fulano Example")
                .email("fulano@mail.com")
                .address(
                        Address.builder()
                                .cep("99999999")
                                .street("Street A")
                                .number("123")
                                .complement("01")
                                .neighborhood("Downtown")
                                .city("Example City")
                                .uf("MG")
                                .ibge("123456")
                                .build()
                )
                .build();
    }

    public static List<Person> createListOfPerson(){
        return List.of(
                Person.builder()
                        .name("Aaaaaa Example")
                        .email("aaaaaa@mail.com")
                        .address(
                                Address.builder()
                                        .cep("99999999")
                                        .street("Street A")
                                        .number("123")
                                        .complement("01")
                                        .neighborhood("Downtown")
                                        .city("Example City")
                                        .uf("MG")
                                        .ibge("123456")
                                        .build()
                        )
                        .build(),

                Person.builder()
                        .name("Bbbbbb Example")
                        .email("bbbbbb@mail.com")
                        .address(
                                Address.builder()
                                        .cep("99999999")
                                        .street("Street A")
                                        .number("123")
                                        .complement("01")
                                        .neighborhood("Downtown")
                                        .city("Example City")
                                        .uf("MG")
                                        .ibge("123456")
                                        .build()
                        )
                        .build()
        );
    }

    public static List<Person> createListOfValidPerson(){
        return List.of(
                Person.builder()
                        .id(1l)
                        .name("Aaaaaa Example")
                        .email("aaaaaa@mail.com")
                        .address(
                                Address.builder()
                                        .cep("99999999")
                                        .street("Street A")
                                        .number("123")
                                        .complement("01")
                                        .neighborhood("Downtown")
                                        .city("Example City")
                                        .uf("MG")
                                        .ibge("123456")
                                        .build()
                        )
                        .build(),

                Person.builder()
                        .id(2l)
                        .name("Bbbbbb Example")
                        .email("bbbbbb@mail.com")
                        .address(
                                Address.builder()
                                        .cep("99999999")
                                        .street("Street A")
                                        .number("123")
                                        .complement("01")
                                        .neighborhood("Downtown")
                                        .city("Example City")
                                        .uf("MG")
                                        .ibge("123456")
                                        .build()
                        )
                        .build()
        );
    }

    public static Page<Person> createPageOfValidPerson(){
        return new PageImpl<>(createListOfValidPerson());
    }
}
