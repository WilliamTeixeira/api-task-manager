package com.example.taskmanager.controller;

import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import com.example.taskmanager.mock.PersonRepositoryMock;
import com.example.taskmanager.mock.PersonServiceMock;
import com.example.taskmanager.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@DisplayName("Test for Person Controller Endpoints")
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<PersonCreateDTO> personCreateDTOJson;
    @Autowired
    private JacksonTester<PersonDetailDTO> personDetailDTOJson;
    @MockBean
    private PersonService personService;

    @BeforeEach
    void setup() {
        Mockito.when(personService.save(ArgumentMatchers.any()))
                .thenReturn(PersonServiceMock.save());

        Mockito.doNothing().when(personService).delete(ArgumentMatchers.anyLong());

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
    @WithMockUser
    @DisplayName("Given that all fields are correct When create method is called Then the transaction will be successful")
    void createPersistsPersonWhenSuccessful() throws Exception {
        var toBeSend = personCreateDTOJson.write(new PersonCreateDTO(PersonRepositoryMock.createPersonToBeSaved())).getJson();
        var toBeCompare = personDetailDTOJson.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/persons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toBeSend))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that any fields are incorrect When create method is called Then returns http bad request status 400")
    void createReturnBadRequestWhenAnyFieldAreIncorrect() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/persons"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field is correct When delete method is called Then the transaction will be successful")
    void deletePersistsPersonWhenSuccessful() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/persons/1"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id fields are not found When delete method is called Then returns http not found status 404")
    void deleteReturnNotFoundWhenIdNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Person Not Found"))
                .when(personService).delete(ArgumentMatchers.anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/persons/999"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that all fields are correct When update method is called Then the transaction will be successful")
    void updatePersistsPersonWhenSuccessful() throws Exception {
        var toBeSend = personDetailDTOJson.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();
        var toBeCompare = personDetailDTOJson.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/persons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toBeSend))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that any fields are incorrect When update method is called Then returns http bad request status 400")
    void updateReturnBadRequestWhenAnyFieldAreIncorrect() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/persons"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field exist When findById method is called Then  the transaction will be successful")
    void findByIdFindPersonWhenSuccessful() throws Exception {
        var toBeCompare = personDetailDTOJson.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/id/1"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field are not found When findById method is called Then returns http not found status 404")
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
    @WithMockUser
    @DisplayName("Given that Name field exist When findByName method is called Then the transaction will be successful")
    void findByNameFindPersonWhenSuccessful() throws Exception {
        var toBeCompare = personDetailDTOJson.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/name/Fulano Example"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Name field are not found When findByName method is called Then returns http not found status 404")
    void findByNameReturnNotFoundWhenNameNotFound() throws Exception {
        Mockito.when(personService.findByName(ArgumentMatchers.anyString()))
                .thenThrow(new EntityNotFoundException("Person Not Found"));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/name/notfound"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Email field exist When findByEmail method is called Then the transaction will be successful")
    void findByEmailFindPersonWhenSuccessful() throws Exception {
        var toBeCompare = personDetailDTOJson.write(new PersonDetailDTO(PersonRepositoryMock.createValidPerson())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/email/fulano@mail.com"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    @DisplayName("Given that persons exists in database When listAll method is called Then the transaction will be successful")
    void listAllReturnsAllPersonWhenSuccessful() throws Exception {
        String toBeCompare = new ObjectMapper().writeValueAsString(PersonServiceMock.listAll());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that no persons exists in database When listAll method is called Then returns a empty list")
    void listAllReturnEmptyListWhenNoPersonIsFound() throws Exception {
        Mockito.when(personService.listAll(ArgumentMatchers.any()))
                .thenReturn(PersonServiceMock.listEmpty());

        String toBeCompare = new ObjectMapper().writeValueAsString(PersonServiceMock.listEmpty());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that persons exists in database When listAllByName method is called Then the transaction will be successful")
    void listAllByNameReturnsAllPersonWhenSuccessful() throws Exception {
        String toBeCompare = new ObjectMapper().writeValueAsString(PersonServiceMock.listAll());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/name/success"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that no persons exists in database When listAllByName method is called Then returns a empty list")
    void listAllByNameReturnEmptyListWhenNoPersonIsFound() throws Exception {
        Mockito.when(personService.listAllByName(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(PersonServiceMock.listEmpty());

        String toBeCompare = new ObjectMapper().writeValueAsString(PersonServiceMock.listEmpty());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/name/notfound"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that persons exists in database When listAllByEmail method is called Then the transaction will be successful")
    void listAllByEmailReturnsAllPersonWhenSuccessful() throws Exception {
        String toBeCompare = new ObjectMapper().writeValueAsString(PersonServiceMock.listAll());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/email/success"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that no persons exists in database When listAllByEmail method is called Then returns a empty list")
    void listAllByEmailReturnEmptyListWhenNoPersonIsFound() throws Exception {
        Mockito.when(personService.listAllByEMail(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(PersonServiceMock.listEmpty());

        String toBeCompare = new ObjectMapper().writeValueAsString(PersonServiceMock.listEmpty());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/persons/all/email/notfound"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }
}