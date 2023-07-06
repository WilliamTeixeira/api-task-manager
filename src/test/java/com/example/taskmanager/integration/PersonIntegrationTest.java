package com.example.taskmanager.integration;

import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import com.example.taskmanager.mock.PersonRepositoryMock;
import com.example.taskmanager.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Integrations Test for Person Repository")
public class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<PersonCreateDTO> personCreateDTOJason;
    @Autowired
    private JacksonTester<PersonDetailDTO> personDetailDTOJason;
    @Autowired
    PersonRepository personRepository;

    @AfterEach
    void down(){
        personRepository.deleteAll();
    }
    @Test
    @WithMockUser
    @DisplayName("Given that all fields are correct When perform a http post request for persons endpoint Then the transaction will be successful")
    void createPersistsPersonWhenSuccessful() throws Exception {
        //Given
        var toBeSend = personCreateDTOJason.write(new PersonCreateDTO(PersonRepositoryMock.createPersonToBeSaved())).getJson();

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/persons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toBeSend))
                .andReturn().getResponse();

        var personDetailDTO = new PersonDetailDTO(personRepository.findByName("Fulano Example"));
        var toBeCompare = personDetailDTOJason.write(personDetailDTO).getJson();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that any  fields are incorrect When perform a http post request for persons endpoint Then returns http bad request status 400")
    void createReturnBadRequestWhenAnyFieldAreIncorrect() throws Exception {
        //Given
        //When
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/persons"))
                .andReturn().getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field is correct When perform a http delete request for persons endpoint Then the transaction will be successful")
    void deletePersistsPersonWhenSuccessful() throws Exception {
        //Given
        Person toBeDelete = personRepository.save(PersonRepositoryMock.createValidPerson());

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/persons/" + toBeDelete.getId()))
                .andReturn().getResponse();

        Person personNotFound = personRepository.findByName("Fulano Example");

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        Assertions.assertThat(personNotFound).isNull();
    }

    @Test
    @WithMockUser
    @DisplayName("Given that all fields are correct When perform a http put request for persons endpoint Then the transaction will be successful")
    void updatePersistsPersonWhenSuccessful() throws Exception {
        //Given
        var toBeUpdate = personRepository.save(PersonRepositoryMock.createValidPerson());
        toBeUpdate.setName("Fulano Updated");

        var toBeSend = personDetailDTOJason.write(new PersonDetailDTO(toBeUpdate)).getJson();

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/persons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toBeSend))
                .andReturn().getResponse();

        var personUpdated = personRepository.findByName("Fulano Updated");
        var toBeCompare = personDetailDTOJason.write(new PersonDetailDTO(personUpdated)).getJson();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that any fields are incorrect When perform a http put request for persons endpoint Then returns http bad request status 400")
    void updateReturnBadRequestWhenAnyFieldAreIncorrect() throws Exception {
        //Given
        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/persons"))
                .andReturn()
                .getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field exist When perform a http get request for persons/id endpoint Then the transaction will be successful")
    void findByIdFindPersonWhenSuccessful() throws Exception {
        //Given
        var toBeFound = personRepository.save(PersonRepositoryMock.createValidPerson());

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/id/" + toBeFound.getId()))
                .andReturn()
                .getResponse();

        var toBeCompare = personDetailDTOJason.write(new PersonDetailDTO(toBeFound)).getJson();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field are not found When perform a http get request for persons/id endpoint Then returns http not found status 404")
    void findByIdReturnNotFoundWhenIdNotFound() throws Exception {
        //Given
        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/id/999"))
                .andReturn()
                .getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Name field exist When perform a http get request for persons/name endpoint Then the transaction will be successful")
    void findByNameFindPersonWhenSuccessful() throws Exception {
        //Given
        var toBeFound = personRepository.save(PersonRepositoryMock.createValidPerson());

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/name/" + toBeFound.getName()))
                .andReturn()
                .getResponse();

        var toBeCompare = personDetailDTOJason.write(new PersonDetailDTO(toBeFound)).getJson();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);

    }

    @Test
    @WithMockUser
    @DisplayName("Given that Name field are not found When perform a http get request for persons/name endpoint Then returns http not found status 404")
    void findByNameReturnNotFoundWhenNameNotFound() throws Exception {
        //Given
        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/name/notfound"))
                .andReturn()
                .getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Email field exist When perform a http get request for persons/email endpoint Then the transaction will be successful")
    void findByEmailFindPersonWhenSuccessful() throws Exception {
        //Given
        var toBeFound = personRepository.save(PersonRepositoryMock.createValidPerson());

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/email/" + toBeFound.getEmail()))
                .andReturn()
                .getResponse();

        var toBeCompare = personDetailDTOJason.write(new PersonDetailDTO(toBeFound)).getJson();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Email fields are not found When perform a http get request for persons/email endpoint Then returns http not found status 404")
    void findByEmailReturnNotFoundWhenNameNotFound() throws Exception {
        //Given
        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/email/notfound@mail.com"))
                .andReturn()
                .getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that persons exists in database When perform a http get request for persons/all endpoint Then the transaction will be successful")
    void listAllReturnsAllPersonWhenSuccessful() throws Exception {
        //Given
        var personList = personRepository.saveAll(PersonRepositoryMock.createListOfPerson());
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var toBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all"))
                .andReturn()
                .getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that no persons exists in database When perform a http get request for persons/all endpoint Then returns a empty list")
    void listAllReturnEmptyListWhenNoPersonIsFound() throws Exception {
        //Given
        List<Person> personList = Collections.emptyList();
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var toBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all"))
                .andReturn()
                .getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that persons exists in database When perform a http get request for persons/all/name endpoint Then the transaction will be successful")
    void listAllByNameReturnsAllPersonWhenSuccessful() throws Exception {
        //Given
        var personListSaved = personRepository.saveAll(PersonRepositoryMock.createListOfPerson());

        List<Person> personList = personListSaved.stream().filter(p -> p.getName().contains("Bbbbbb")).toList();
        Pageable pageable = PageRequest.of(0,10, Sort.by("name"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var toBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/name/Bbbbbb"))
                .andReturn()
                .getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);

    }

    @Test
    @WithMockUser
    @DisplayName("Given that no persons exists in database When perform a http get request for persons/all/name endpoint Then returns a empty list")
    void listAllByNameReturnEmptyListWhenNoPersonIsFound() throws Exception {
        //Given
        List<Person> personList = Collections.emptyList();
        Pageable pageable = PageRequest.of(0,10, Sort.by("name"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var toBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/name/notfound"))
                .andReturn()
                .getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that persons exists in database When perform a http get request for persons/all/email endpoint Then the transaction will be successful")
    void listAllByEmailReturnsAllPersonWhenSuccessful() throws Exception {
        //Given
        var personListSaved = personRepository.saveAll(PersonRepositoryMock.createListOfPerson());

        List<Person> personList = personListSaved.stream().filter(p -> p.getEmail().contains("bbbbbb")).toList();
        Pageable pageable = PageRequest.of(0,10, Sort.by("email"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var toBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/email/bbbbbb"))
                .andReturn()
                .getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that no persons exists in database When perform a http get request for persons/all/email endpoint Then returns a empty list")
    void listAllByEmailReturnEmptyListWhenNoPersonIsFound() throws Exception {
        //Given
        List<Person> personList = Collections.emptyList();
        Pageable pageable = PageRequest.of(0,10, Sort.by("email"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var toBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/email/notfound"))
                .andReturn()
                .getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }
}
