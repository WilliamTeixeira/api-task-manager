package com.example.taskmanager.controller;

import com.example.taskmanager.domain.user.UserDetailDTO;
import com.example.taskmanager.domain.user.UserReplaceDTO;
import com.example.taskmanager.mock.UserRepositoryMock;
import com.example.taskmanager.mock.UserServiceMock;
import com.example.taskmanager.service.UserService;
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
@DisplayName("Test for User Controller Endpoint")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<UserDetailDTO> userDetailJson;
    @Autowired
    private JacksonTester<UserReplaceDTO> userReplaceJson;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setup(){
        Mockito.doNothing().when(userService).delete(ArgumentMatchers.anyLong());

        Mockito.when(userService.replace(ArgumentMatchers.any()))
                .thenReturn(UserServiceMock.replace());

        Mockito.when(userService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(UserServiceMock.findBy());

        Mockito.when(userService.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(UserServiceMock.findBy());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Given that Id field is correct When delete method is called Then the transaction will be successful")
    void deletePersistUserWhenSuccessful() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Given that role user is not ADMIN When delete method is called Then returns http forbidden status")
    void deleteReturnForbiddenWhenRoleIsNotAdmin() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Given that Id fields are not found When delete method is called Then returns http not found status 404")
    void deleteReturnNotFoundWhenIdNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("User Not Found"))
                .when(userService).delete(ArgumentMatchers.anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/users/999"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that all fields are correct When update method is called Then the transaction will be successful")
    void updatePersistsUserWhenSuccessful() throws Exception {
        var toBeSend = userReplaceJson.write(new UserReplaceDTO(UserRepositoryMock.createValidReplacerUser())).getJson();
        var toBeCompare = userDetailJson.write(new UserDetailDTO(UserRepositoryMock.createValidUser())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/users")
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
                        MockMvcRequestBuilders.put("/users"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field exist When findById method is called Then  the transaction will be successful")
    void findByIdFindUserWhenSuccessful() throws Exception {
        var toBeCompare = userDetailJson.write(new UserDetailDTO(UserRepositoryMock.createValidUser())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/id/1"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field are not found When findById method is called Then returns http not found status 404")
    void findByIdReturnNotFoundWhenIdNotFound() throws Exception {
        Mockito.when(userService.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new EntityNotFoundException("User Not Found"));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/id/999"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
    @Test
    @WithMockUser
    @DisplayName("Given that Username field exist When findByUsername method is called Then the transaction will be successful")
    void findByUsernameFindUserWhenSuccessful() throws Exception {
        var toBeCompare = userDetailJson.write(new UserDetailDTO(UserRepositoryMock.createValidUser())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/username/email@email.com"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Username field are not found When findByUsername method is called Then returns http not found status 404")
    void findByUsernameReturnNotFoundWhenUsernameNotFound() throws Exception {
        Mockito.when(userService.findByUsername(ArgumentMatchers.anyString()))
                .thenThrow(new EntityNotFoundException("User Not Found"));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/username/notfound"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}