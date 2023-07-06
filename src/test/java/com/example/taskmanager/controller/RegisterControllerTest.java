package com.example.taskmanager.controller;

import com.example.taskmanager.domain.user.UserCreateDTO;
import com.example.taskmanager.domain.user.UserDetailDTO;
import com.example.taskmanager.mock.UserRepositoryMock;
import com.example.taskmanager.mock.UserServiceMock;
import com.example.taskmanager.service.UserService;
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
@DisplayName("Test for Register Controller Endpoints")
//@WebMvcTest(RegisterController.class)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JacksonTester<UserCreateDTO> userCreateDTOJson;
    @Autowired
    JacksonTester<UserDetailDTO> userDetailDTOJson;
    @MockBean
    private UserService userService;

    @BeforeEach
    void setup(){
        Mockito.when(userService.save(ArgumentMatchers.any()))
                .thenReturn(UserServiceMock.save());
    }
    @Test
    @WithMockUser
    @DisplayName("Given that any fields are correct When create method is called Then de transaction will be successful")
    void createPersistUsesWhenSuccessful() throws Exception {
        var jsonToBeSend = userCreateDTOJson.write(new UserCreateDTO(UserRepositoryMock.createUserToBeSave())).getJson();
        var jsonToBeCompare = userDetailDTOJson.write(new UserDetailDTO(UserRepositoryMock.createValidUser())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToBeSend))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonToBeCompare);

    }

    @Test
    @WithMockUser
    @DisplayName("Given that any fields are incorrect When create method is called Then returns http bad request status 400")
    void createReturnBadRequestWhenAnyFieldAreIncorrect() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/register"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}