package com.example.taskmanager.integration;

import com.example.taskmanager.domain.user.UserCreateDTO;
import com.example.taskmanager.domain.user.UserDetailDTO;
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
@DisplayName("Integrations Test for Register User")
public class RegisterIntegationTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JacksonTester<UserCreateDTO> userCreateDTOJson;
    @Autowired
    JacksonTester<UserDetailDTO> userDetailDTOJson;

    @Autowired
    UserRepository repository;
    
    @AfterEach
    void down(){
        repository.deleteAll();
    }
    @Test
    @WithMockUser
    @DisplayName("Given that any fields are correct When create method is called Then de transaction will be successful")
    void createPersistUsesWhenSuccessful() throws Exception {
        //Given
        var toBeSend = userCreateDTOJson.write(new UserCreateDTO(UserRepositoryMock.createUserToBeSave())).getJson();

        //When
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toBeSend))
                .andReturn().getResponse();

        var toBeCompare = userDetailDTOJson.write(new UserDetailDTO(repository.findByName("email@email.com"))).getJson();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);

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
