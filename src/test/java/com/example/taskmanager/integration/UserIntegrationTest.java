package com.example.taskmanager.integration;

import com.example.taskmanager.domain.user.UserDetailDTO;
import com.example.taskmanager.domain.user.UserReplaceDTO;
import com.example.taskmanager.mock.UserRepositoryMock;
import com.example.taskmanager.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Integrations Test for User")
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JacksonTester<UserReplaceDTO> userReplaceJson;
    @Autowired
    JacksonTester<UserDetailDTO> userDetailJson;

    @Autowired
    UserRepository repository;
    
    @AfterEach
    void down(){
        repository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Given that Id field is correct When delete method is called Then the transaction will be successful")
    void deletePersistUserWhenSuccessful() throws Exception {
        var toBeDelete = repository.save(UserRepositoryMock.createUserToBeSave());

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + toBeDelete.getId()))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Given that role user is not ADMIN When delete method is called Then returns http forbidden status")
    void deleteReturnForbiddenWhenRoleIsNotAdmin() throws Exception {
        var toBeDelete = repository.save(UserRepositoryMock.createUserToBeSave());

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + toBeDelete.getId()))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Given that Id fields are not found When delete method is called Then returns http not found status 404")
    void deleteReturnNotFoundWhenIdNotFound() throws Exception {
        var toBeDelete = repository.save(UserRepositoryMock.createUserToBeSave());
        var idNotFound = toBeDelete.getId() + 1l;

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + idNotFound))
                .andReturn().getResponse();


        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that all fields are correct When update method is called Then the transaction will be successful")
    void updatePersistsUserWhenSuccessful() throws Exception {
       //Given
        var user = repository.save(UserRepositoryMock.createUserToBeSave());
        user.setRoles("ROLE_USER, ROLE_ADMIN");
        var toBeSend = userReplaceJson.write(new UserReplaceDTO(user)).getJson();

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toBeSend))
                .andReturn().getResponse();

        //Then
        var toBeCompare = userDetailJson.write(new UserDetailDTO(user)).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that any fields are incorrect When update method is called Then returns http bad request status 400")
    void updateReturnBadRequestWhenAnyFieldAreIncorrect() throws Exception {
        //Given
        var user = repository.save(UserRepositoryMock.createUserToBeSave());

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/users"))
                .andReturn().getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field exist When findById method is called Then  the transaction will be successful")
    void findByIdFindUserWhenSuccessful() throws Exception {
        //Given
        var user = repository.save(UserRepositoryMock.createValidUser());

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/id/" + user.getId()))
                .andReturn().getResponse();

        //Then
        var toBeCompare = userDetailJson.write(new UserDetailDTO(user)).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field are not found When findById method is called Then returns http not found status 404")
    void findByIdReturnNotFoundWhenIdNotFound() throws Exception {
        //Given
        var user = repository.save(UserRepositoryMock.createValidUser());
        var idNotFound = user.getId() + 1l;

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/id/"+ idNotFound))
                .andReturn().getResponse();

        //Then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
    @Test
    @WithMockUser
    @DisplayName("Given that Username field exist When findByUsername method is called Then the transaction will be successful")
    void findByUsernameFindUserWhenSuccessful() throws Exception {
        //Given
        var user = repository.save(UserRepositoryMock.createUserToBeSave());

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/username/" + user.getUsername()))
                .andReturn().getResponse();

        //Then
        var toBeCompare = userDetailJson.write(new UserDetailDTO(user)).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Username field are not found When findByUsername method is called Then returns http not found status 404")
    void findByUsernameReturnNotFoundWhenUsernameNotFound() throws Exception {
        //Given
        var user = repository.save(UserRepositoryMock.createUserToBeSave());

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/username/notfound@email.com"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
