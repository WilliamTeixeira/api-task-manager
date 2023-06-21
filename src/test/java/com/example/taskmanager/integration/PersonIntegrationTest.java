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
    @DisplayName("Given that all fields are correct When perform a http post request for persons endpoint Then the transaction will be successful")
    void createPersistsPersonWhenSuccessful() throws Exception {
        var jsonToBeSend = personCreateDTOJason.write(new PersonCreateDTO(PersonRepositoryMock.createPersonToBeSaved())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/persons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonToBeSend))
                .andReturn().getResponse();

        var personDetailDTO = new PersonDetailDTO(personRepository.findByName("Fulano Example"));
        var jsonToBeCompare = personDetailDTOJason.write(personDetailDTO).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }

    @Test
    @DisplayName("Given that any  fields are incorrect When perform a http post request for persons endpoint Then returns http bad request status 400")
    void createReturnBadRequestWhenAnyFieldAreIncorrect() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/persons"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Given that Id field is correct When perform a http delete request for persons endpoint Then the transaction will be successful")
    void deletePersistsPersonWhenSuccessful() throws Exception {
        Person personToBeDeleted = personRepository.save(PersonRepositoryMock.createValidPerson());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/persons/" + personToBeDeleted.getId()))
                .andReturn().getResponse();

        Person personNotFound = personRepository.findByName("Fulano Example");

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        Assertions.assertThat(personNotFound).isNull();
    }

    @Test
    @DisplayName("Given that all fields are correct When perform a http put request for persons endpoint Then the transaction will be successful")
    void updatePersistsPersonWhenSuccessful() throws Exception {
        var personToBeUpdate = personRepository.save(PersonRepositoryMock.createValidPerson());
        personToBeUpdate.setName("Fulano Updated");

        var jsonToBeSend = personDetailDTOJason.write(new PersonDetailDTO(personToBeUpdate)).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/persons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonToBeSend))
                .andReturn().getResponse();

        var personUpdated = personRepository.findByName("Fulano Updated");
        var jsonToBeCompare = personDetailDTOJason.write(new PersonDetailDTO(personUpdated)).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }

    @Test
    @DisplayName("Given that any fields are incorrect When perform a http put request for persons endpoint Then returns http bad request status 400")
    void updateReturnBadRequestWhenAnyFieldAreIncorrect() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/persons"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Given that Id field exist When perform a http get request for persons/id endpoint Then the transaction will be successful")
    void findByIdFindPersonWhenSuccessful() throws Exception {
        var personToBeFound = personRepository.save(PersonRepositoryMock.createValidPerson());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/id/" + personToBeFound.getId()))
                .andReturn()
                .getResponse();

        var jsonToBeCompare = personDetailDTOJason.write(new PersonDetailDTO(personToBeFound)).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }

    @Test
    @DisplayName("Given that Id field are not found When perform a http get request for persons/id endpoint Then returns http not found status 404")
    void findByIdReturnNotFoundWhenIdNotFound() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/id/999"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Given that Name field exist When perform a http get request for persons/name endpoint Then the transaction will be successful")
    void findByNameFindPersonWhenSuccessful() throws Exception {
        var personToBeFound = personRepository.save(PersonRepositoryMock.createValidPerson());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/name/" + personToBeFound.getName()))
                .andReturn()
                .getResponse();

        var jsonToBeCompare = personDetailDTOJason.write(new PersonDetailDTO(personToBeFound)).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);

    }

    @Test
    @DisplayName("Given that Name field are not found When perform a http get request for persons/name endpoint Then returns http not found status 404")
    void findByNameReturnNotFoundWhenNameNotFound() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/name/notfound"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Given that Email field exist When perform a http get request for persons/email endpoint Then the transaction will be successful")
    void findByEmailFindPersonWhenSuccessful() throws Exception {
        var personToBeFound = personRepository.save(PersonRepositoryMock.createValidPerson());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/email/" + personToBeFound.getEmail()))
                .andReturn()
                .getResponse();

        var jsonToBeCompare = personDetailDTOJason.write(new PersonDetailDTO(personToBeFound)).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }

    @Test
    @DisplayName("Given that Email fields are not found When perform a http get request for persons/email endpoint Then returns http not found status 404")
    void findByEmailReturnNotFoundWhenNameNotFound() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/email/notfound@mail.com"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    //TODO: o page retornado no response não fica igual ao json criado para a comparação
    @Test
    @DisplayName("Given that persons exists in database When perform a http get request for persons/all endpoint Then the transaction will be successful")
    void listAllReturnsAllPersonWhenSuccessful() throws Exception {
        var personList = personRepository.saveAll(PersonRepositoryMock.createListOfPerson());
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var jsonToBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }

    @Test
    @DisplayName("Given that no persons exists in database When perform a http get request for persons/all endpoint Then returns a empty list")
    void listAllReturnEmptyListWhenNoPersonIsFound() throws Exception {
        List<Person> personList = Collections.emptyList();
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var jsonToBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }

    @Test
    @DisplayName("Given that persons exists in database When perform a http get request for persons/all/name endpoint Then the transaction will be successful")
    void listAllByNameReturnsAllPersonWhenSuccessful() throws Exception {
        var personListSaved = personRepository.saveAll(PersonRepositoryMock.createListOfPerson());

        List<Person> personList = personListSaved.stream().filter(p -> p.getName().contains("Bbbbbb")).toList();
        Pageable pageable = PageRequest.of(0,10, Sort.by("name"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var jsonToBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/name/Bbbbbb"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);

    }

    @Test
    @DisplayName("Given that no persons exists in database When perform a http get request for persons/all/name endpoint Then returns a empty list")
    void listAllByNameReturnEmptyListWhenNoPersonIsFound() throws Exception {
        List<Person> personList = Collections.emptyList();
        Pageable pageable = PageRequest.of(0,10, Sort.by("name"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var jsonToBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/name/notfound"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }

    @Test
    @DisplayName("Given that persons exists in database When perform a http get request for persons/all/email endpoint Then the transaction will be successful")
    void listAllByEmailReturnsAllPersonWhenSuccessful() throws Exception {
        var personListSaved = personRepository.saveAll(PersonRepositoryMock.createListOfPerson());

        List<Person> personList = personListSaved.stream().filter(p -> p.getEmail().contains("bbbbbb")).toList();
        Pageable pageable = PageRequest.of(0,10, Sort.by("email"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var jsonToBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/email/bbbbbb"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }

    @Test
    @DisplayName("Given that no persons exists in database When perform a http get request for persons/all/email endpoint Then returns a empty list")
    void listAllByEmailReturnEmptyListWhenNoPersonIsFound() throws Exception {
        List<Person> personList = Collections.emptyList();
        Pageable pageable = PageRequest.of(0,10, Sort.by("email"));
        PageImpl<Person> personPage = new PageImpl<>(personList, pageable,personList.size());
        var jsonToBeCompare = new ObjectMapper().writeValueAsString(personPage.map(PersonDetailDTO::new));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/email/notfound"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }
}
