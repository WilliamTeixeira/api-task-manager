package com.example.taskmanager.controller;

import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import com.example.taskmanager.mock.PersonRepositoryMock;
import com.example.taskmanager.mock.PersonServiceMock;
import com.example.taskmanager.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureJsonTesters
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<PersonCreateDTO> personCreateDTOJason;

    @Autowired
    private JacksonTester<PersonDetailDTO> personDetailDTOJason;

    @MockBean
    private PersonService personService;

    @BeforeEach
    void setup(){

        Mockito.when(personService.save(ArgumentMatchers.any()))
                        .thenReturn(PersonServiceMock.save());

        //Mockito.doNothing().when(personService.delete(ArgumentMatchers.any()));

        Mockito.when(personService.replace(ArgumentMatchers.any()))
                        .thenReturn(PersonServiceMock.replace());

        Mockito.when(personService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(PersonServiceMock.findBy());

        Mockito.when(personService.findByName(ArgumentMatchers.anyString()))
                        .thenReturn(PersonServiceMock.findBy());

        Mockito.when(personService.findByEmail(ArgumentMatchers.anyString()))
                        .thenReturn(PersonServiceMock.findBy());

        Mockito.when(personService.listAll(ArgumentMatchers.any()))
                .thenReturn(PersonServiceMock.listAll());

        Mockito.when(personService.listAllByName(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(PersonServiceMock.listAll());

        Mockito.when(personService.listAllByEMail(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(PersonServiceMock.listAll());

    }

    @Test
    @DisplayName("Given that all fields are correct When create method is called Then the transaction will be successful")
    void createPersistsPersonWhenSuccessful() throws Exception {
        var jsonToBeSend = personCreateDTOJason.write(new PersonCreateDTO(PersonRepositoryMock.createPersonToBeSaved())).getJson();
        var jsonToBeCompare = personDetailDTOJason.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToBeSend))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }
    @Test
    @DisplayName("Given that any  fields are incorrect When create method is called Then returns http bad request status 400")
    void createReturnBadRequestWhenAnyFieldAreIncorrect() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/persons"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void delete() {
    }

    @Test
    @DisplayName("Given that all fields are correct When update method is called Then the transaction will be successful")
    void updatePersistsPersonWhenSuccessful() throws Exception {
        var jsonToBeSend = personDetailDTOJason.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();
        var jsonToBeCompare = personDetailDTOJason.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/persons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonToBeSend))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }
    @Test
    @DisplayName("Given that any fields are incorrect When update method is called Then returns http bad request status 400")
    void updateReturnBadRequestWhenAnyFieldAreIncorrect() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/persons"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    @Test
    @DisplayName("Given that the field Id exist When findById method is called Then  the transaction will be successful")
    void findByIdFindPersonWhenSuccessful() throws Exception {
        var jsonToBeCompare = personDetailDTOJason.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/persons/id/1"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }
    @Test
    @DisplayName("Given that Id fields are not found When findById method is called Then returns http not found status 404")
    void findByIdReturnNotFoundWhenIdNotFound() throws Exception {
        Mockito.when(personService.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new EntityNotFoundException("Person Not Found"));

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/persons/id/999"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
    @Test
    @DisplayName("Given that the field Name exist When findByName method is called Then the transaction will be successful")
    void findByNameFindPersonWhenSuccessful() throws Exception {
        var jsonToBeCompare = personDetailDTOJason.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/name/Fulano Example"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }
    @Test
    @DisplayName("Given that Name fields are not found When findByName method is called Then returns http not found status 404")
    void findByNameReturnNotFoundWhenNameNotFound() throws Exception {
        Mockito.when(personService.findByName(ArgumentMatchers.anyString()))
                .thenThrow(new EntityNotFoundException("Person Not Found"));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/name/999"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
    @Test
    @DisplayName("Given that the field Email exist When findByEmail method is called Then the transaction will be successful")
    void findByEmailFindPersonWhenSuccessful() throws Exception {
        var jsonToBeCompare = personDetailDTOJason.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/email/fulano@mail.com"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);
    }
    @Test
    @DisplayName("Given that Email fields are not found When findByEmail method is called Then returns http not found status 404")
    void findByEmailReturnNotFoundWhenNameNotFound() throws Exception {
        Mockito.when(personService.findByEmail(ArgumentMatchers.anyString()))
                .thenThrow(new EntityNotFoundException("Person Not Found"));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/email/notfound@mail.com"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void listAll() {
    }

    @Test
    void listAllByName() {
    }

    @Test
    void listAllByEmail() {
    }
}