package com.example.taskmanager.repository;

import com.example.taskmanager.domain.address.Address;
import com.example.taskmanager.domain.person.Person;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Test for Person Repository")
class PersonRepositoryTest {

    @Autowired
    PersonRepository repository;

    @Test
    @DisplayName("Given that all fields are correct When persisting the Person entity Then the transaction will be successful")
    void savePersistsPersonWhenSuccessful(){
        Person personToBeSave = createPerson();

        Person personSaved = this.repository.save(personToBeSave);

        Assertions.assertThat(personSaved).isNotNull();
        Assertions.assertThat(personSaved.getId()).isNotNull();
        Assertions.assertThat(personSaved.getName()).isEqualTo(personToBeSave.getName());
    }

    @Test
    @DisplayName("Given that all fields are correct When update the Person entity Then the transaction will be successful")
    void saveUpdatePersonWhenSuccessful(){
        Person personToBeSave = createPerson();
        Person personSaved = this.repository.save(personToBeSave);

        personSaved.setName("Changed Name");

        Person personUpdated = this.repository.save(personSaved);

        Assertions.assertThat(personUpdated).isNotNull();
        Assertions.assertThat(personUpdated.getId()).isNotNull();
        Assertions.assertThat(personUpdated.getName()).isEqualTo(personSaved.getName());
    }

    @Test
    @DisplayName("Given that all fields are correct When delete the Person entity Then the transaction will be successful")
    void deleteRemovePersonWhenSuccessful(){
        Person personToBeSave = createPerson();
        Person personSaved = this.repository.save(personToBeSave);

        this.repository.delete(personSaved);

        Optional<Person> personOptional = this.repository.findById(personSaved.getId());

        Assertions.assertThat(personOptional).isEmpty();
    }

    @Test
    @DisplayName("Given the ID field is correct When getReferenceByID Then return a Person entity wil be successful")
    void getReferenceByIdReturnPersonWhenSuccessful(){
        Person personToBeSaved = createPerson();
        Person personSaved = this.repository.save(personToBeSaved);

        Person findedPerson = this.repository.getReferenceById(personSaved.getId());

        Assertions.assertThat(findedPerson).isNotNull();
        Assertions.assertThat(findedPerson.getId()).isEqualTo(personSaved.getId());
    }

    @Test
    @DisplayName("Given the Name field is correct When findByName Then return a Person entity wil be successful")
    void findByNameReturnPersonWhenSuccessful(){
        Person personToBeSaved = createPerson();
        Person personSaved = this.repository.save(personToBeSaved);

        Person findedPerson = this.repository.findByName(personSaved.getName());

        Assertions.assertThat(findedPerson).isNotNull();
        Assertions.assertThat(findedPerson.getId()).isEqualTo(personSaved.getId());
        Assertions.assertThat(findedPerson.getName()).isEqualTo(personSaved.getName());
    }

    @Test
    @DisplayName("Given the Name field is correct but not exists in the list of person When findByName Then return a null person")
    void findByNameReturnNullPersonWhenPersonNotFound(){
        Person findedPerson = this.repository.findByName("Aaaaa Example");

        Assertions.assertThat(findedPerson).isNull();
    }

    @Test
    @DisplayName("Given the E-Mail field is correct When findByEmail Then return a Person entity wil be successful")
    void findByEmailReturnPersonWhenSuccessful(){
        Person personToBeSaved = createPerson();
        Person personSaved = this.repository.save(personToBeSaved);

        Person findedPerson = this.repository.findByEmail(personSaved.getEmail());

        Assertions.assertThat(findedPerson).isNotNull();
        Assertions.assertThat(findedPerson.getId()).isEqualTo(personSaved.getId());
        Assertions.assertThat(findedPerson.getEmail()).isEqualTo(personSaved.getEmail());
    }

    @Test
    @DisplayName("Given the Email field is correct but not exists in the list of person When findByEmail Then return a null person")
    void findByEmailReturnNullPersonWhenPersonNotFound(){
        Person findedPerson = this.repository.findByEmail("aaaaa@email.com");

        Assertions.assertThat(findedPerson).isNull();
    }

    @Test
    @DisplayName("Given that a fraction of a Name field exists within the list of Person entity When findByNameContainingIgnoreCase Then returns a list of Person entity wil be successful")
    void findByNameContainingIgnoreCaseReturnsListOfPersonWhenSuccessful(){
        List<Person> personList = createListOfPerson();
        personList.forEach(personToBeSaved -> this.repository.save(personToBeSaved));

        Person personToCompare = personList.get(0);

        String fractionOfName = personToCompare.getName().substring(0, 4);
        Pageable pageable = Pageable.ofSize(10);

        Page<Person> pageOfPerson = this.repository.findByNameContainingIgnoreCase(fractionOfName,pageable);

        var personContent = pageOfPerson.getContent();
        var person = personContent.get(0);

        //If the list of Person is ok
        Assertions.assertThat(personContent).isNotEmpty();
        Assertions.assertThat(personContent.size()).isEqualTo(1);
        Assertions.assertThat(personContent).contains(personToCompare);

        //If the person entity returned is ok
        Assertions.assertThat(person).isNotNull();
        Assertions.assertThat(person.getName()).isEqualTo(personToCompare.getName());
    }

    @Test
    @DisplayName("Given that a fraction of a Name field not exists within the list of Person entity When findByNameContainingIgnoreCase Then returns a empty 'Page' list of Person entity")
    void findByNameContainingIgnoreCaseReturnsEmptyListOfPersonWhenPersonNotFound(){
        List<Person> personList = createListOfPerson();
        personList.forEach(personToBeSaved -> this.repository.save(personToBeSaved));

        String fractionOfName = "xxxx";
        Pageable pageable = Pageable.ofSize(10);

        Page<Person> pageOfPerson = this.repository.findByNameContainingIgnoreCase(fractionOfName,pageable);

        var personContent = pageOfPerson.getContent();

        //If the list of Person is ok
        Assertions.assertThat(personContent).isEmpty();
        Assertions.assertThat(personContent.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Given that a fraction of a Email field exists within the list of Person entity When findByNameContainingIgnoreCase Then returns a list of Person entity wil be successful")
    void findByEmailContainingIgnoreCaseReturnsListOfPersonWhenSuccessful(){
        List<Person> personList = createListOfPerson();
        personList.forEach(personToBeSaved -> this.repository.save(personToBeSaved));

        Person personToCompare = personList.get(0);

        String fractionOfEmail = personToCompare.getEmail().substring(0, 4);
        Pageable pageable = Pageable.ofSize(10);

        Page<Person> pageOfPerson = this.repository.findByEmailContainingIgnoreCase(fractionOfEmail,pageable);

        var personContent = pageOfPerson.getContent();
        var person = personContent.get(0);

        //If the list of Person is ok
        Assertions.assertThat(personContent).isNotEmpty();
        Assertions.assertThat(personContent.size()).isEqualTo(1);
        Assertions.assertThat(personContent).contains(personToCompare);

        //If the person entity returned is ok
        Assertions.assertThat(person).isNotNull();
        Assertions.assertThat(person.getEmail()).isEqualTo(personToCompare.getEmail());
    }

    @Test
    @DisplayName("Given that a fraction of a Email field not exists within the list of Persons entity When findByEmailContainingIgnoreCase Then returns a empty 'Page' list of Person entity")
    void findByEmailContainingIgnoreCaseReturnsEmptyListOfPersonWhenPersonNotFound(){
        List<Person> personList = createListOfPerson();
        personList.forEach(personToBeSaved -> this.repository.save(personToBeSaved));

        String fractionOfEmail = "xxxx";
        Pageable pageable = Pageable.ofSize(10);

        Page<Person> pageOfPerson = this.repository.findByEmailContainingIgnoreCase(fractionOfEmail,pageable);

        var personContent = pageOfPerson.getContent();

        //If the list of Person is ok
        Assertions.assertThat(personContent).isEmpty();
        Assertions.assertThat(personContent.size()).isEqualTo(0);
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
    List<Person> createListOfPerson(){
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
}