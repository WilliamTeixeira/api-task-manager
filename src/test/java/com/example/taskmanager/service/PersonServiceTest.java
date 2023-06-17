package com.example.taskmanager.service;

import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import com.example.taskmanager.mock.PersonRepositoryMock;
import com.example.taskmanager.mock.PersonRepositoryMock;
import com.example.taskmanager.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DisplayName("Test for Person Service")
@ExtendWith(SpringExtension.class)
class PersonServiceTest {
    @InjectMocks
    private PersonService personService;
    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setup() {
        Mockito.when(personRepository.save(ArgumentMatchers.any()))
                .thenReturn(PersonRepositoryMock.createValidPerson());

        Mockito.doNothing().when(personRepository).delete(ArgumentMatchers.any());

        Mockito.when(personRepository.getReferenceById(ArgumentMatchers.anyLong()))
                .thenReturn(PersonRepositoryMock.createValidPerson());

        Mockito.when(personRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(PersonRepositoryMock.createValidPerson());

        Mockito.when(personRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(PersonRepositoryMock.createValidPerson());

        Mockito.when(personRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(PersonRepositoryMock.createPageOfValidPerson());

        Mockito.when(personRepository.findByNameContainingIgnoreCase(ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(PersonRepositoryMock.createPageOfValidPerson());

        Mockito.when(personRepository.findByEmailContainingIgnoreCase(ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(PersonRepositoryMock.createPageOfValidPerson());

    }

    @Test
    @DisplayName("Given that all fields are correct When save method is called Then the transaction will be successful")
    void savePersistsPersonWhenSuccessful() throws Exception {
        var personToBeSave = new PersonCreateDTO(PersonRepositoryMock.createPersonToBeSaved());
        var personToBeCompare = new PersonDetailDTO(PersonRepositoryMock.createValidPerson());

        var personSaved = personService.save(personToBeSave);

        Assertions.assertThat(personSaved)
                .isNotNull()
                .isInstanceOf(PersonDetailDTO.class);

        Assertions.assertThat(personSaved.name()).isEqualTo(personToBeCompare.name());
    }
    @Test
    @DisplayName("Given that Id field is correct When delete method is called Then the transaction will be successful")
    void deletePersistsPersonWhenSuccessful() throws Exception {
        Assertions.assertThatCode(() -> personService.delete(1l))
                        .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Given that all fields are correct When replace method is called Then the transaction will be successful")
    void replacePersistsPersonWhenSuccessful() throws Exception {
        var personToBeSave = new PersonDetailDTO(PersonRepositoryMock.createValidPerson());
        var personToBeCompare = new PersonDetailDTO(PersonRepositoryMock.createValidPerson());

        var personSaved = personService.replace(personToBeSave);

        Assertions.assertThat(personSaved)
                .isNotNull()
                .isInstanceOf(PersonDetailDTO.class);

        Assertions.assertThat(personSaved.name()).isEqualTo(personToBeCompare.name());
    }

    @Test
    @DisplayName("Given that Id field exist When findById method is called Then  the transaction will be successful")
    void findByIdFindPersonWhenSuccessful() throws Exception {
        var personToBeCompare = new PersonDetailDTO(PersonRepositoryMock.createValidPerson());

        var personFound = personService.findById(personToBeCompare.id());

        Assertions.assertThat(personFound)
                .isNotNull()
                .isInstanceOf(PersonDetailDTO.class);

        Assertions.assertThat(personFound.name()).isEqualTo(personToBeCompare.name());
    }

    @Test
    @DisplayName("Given that Name field exist When findByName method is called Then the transaction will be successful")
    void findByNameFindPersonWhenSuccessful() throws Exception {
        var personToBeCompare = new PersonDetailDTO(PersonRepositoryMock.createValidPerson());

        var personFound = personService.findByName(personToBeCompare.name());

        Assertions.assertThat(personFound)
                .isNotNull()
                .isInstanceOf(PersonDetailDTO.class);

        Assertions.assertThat(personFound.name()).isEqualTo(personToBeCompare.name());
    }

    @Test
    @DisplayName("Given that Name field are not found When findByName method is called Then returns http not found status 404")
    void findByNameReturnNotFoundWhenNameNotFound() throws Exception {
        Mockito.when(personRepository.findByName(ArgumentMatchers.anyString()))
                .thenThrow(new EntityNotFoundException("Person Not Found"));

        Assertions.assertThatThrownBy(() -> personService.findByName(ArgumentMatchers.anyString()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Person Not Found");
    }

    @Test
    @DisplayName("Given that Email field exist When findByEmail method is called Then the transaction will be successful")
    void findByEmailFindPersonWhenSuccessful() throws Exception {
        var personToBeCompare = new PersonDetailDTO(PersonRepositoryMock.createValidPerson());

        var personFound = personService.findByEmail(personToBeCompare.email());

        Assertions.assertThat(personFound)
                .isNotNull()
                .isInstanceOf(PersonDetailDTO.class);

        Assertions.assertThat(personFound.name()).isEqualTo(personToBeCompare.name());
    }

    @Test
    @DisplayName("Given that Email fields are not found When findByEmail method is called Then returns http not found status 404")
    void findByEmailReturnNotFoundWhenNameNotFound() throws Exception {
        Mockito.when(personRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenThrow(new EntityNotFoundException("Person Not Found"));

        Assertions.assertThatThrownBy(() -> personService.findByEmail(ArgumentMatchers.anyString()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Person Not Found");
    }

    @Test
    @DisplayName("Given that persons exists in database When listAll method is called Then the transaction will be successful")
    void listAllReturnsAllPersonWhenSuccessful() throws Exception {
        var personPageCompare = personService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(personPageCompare)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }


    @Test
    @DisplayName("Given that persons exists in database When listAllByName method is called Then the transaction will be successful")
    void listAllByNameReturnsAllPersonWhenSuccessful() throws Exception {
        var personPageCompare = personService.listAllByName("name", PageRequest.of(1,1));

        Assertions.assertThat(personPageCompare)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @DisplayName("Given that persons exists in database When listAllByEmail method is called Then the transaction will be successful")
    void listAllByEmailReturnsAllPersonWhenSuccessful() throws Exception {
        var personPageCompare = personService.listAllByEMail("email", PageRequest.of(1,1));

        Assertions.assertThat(personPageCompare)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

}