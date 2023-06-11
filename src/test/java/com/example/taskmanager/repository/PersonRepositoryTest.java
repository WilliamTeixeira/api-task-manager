package com.example.taskmanager.repository;

import com.example.taskmanager.domain.address.Address;
import com.example.taskmanager.domain.person.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Test for Person Repository")
class PersonRepositoryTest {

    @Autowired
    PersonRepository repository;

    @Test
    @DisplayName("Given that all fields are correct When persisting the Person entity Then the transaction will be successful ")
    void savePersonIfAllFieldsAreCorrect(){
        Person personToBeSave = createPerson();

        Person personSaved = this.repository.save(personToBeSave);

        Assertions.assertThat(personSaved).isNotNull();
        Assertions.assertThat(personSaved.getId()).isNotNull();
        Assertions.assertThat(personSaved.getName()).isEqualTo(personToBeSave.getName());
    }


    Person createPerson(){
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
}