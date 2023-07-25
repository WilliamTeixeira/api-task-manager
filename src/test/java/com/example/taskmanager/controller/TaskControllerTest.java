package com.example.taskmanager.controller;

import com.example.taskmanager.domain.task.TaskCreateDTO;
import com.example.taskmanager.domain.task.TaskDetailDTO;
import com.example.taskmanager.domain.task.TaskDetailWithCommentsDTO;
import com.example.taskmanager.mock.TaskRepositoryMock;
import com.example.taskmanager.mock.TaskServiceMock;
import com.example.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
@DisplayName("Test for Task Controller Endpoint")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<TaskCreateDTO> taskCreateDTOJson;
    @Autowired
    private JacksonTester<TaskDetailDTO> taskDetailDTOJson;
    @Autowired
    private JacksonTester<TaskDetailWithCommentsDTO> taskDetailWithCommentsDTOJson;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    void setup() {
        Mockito.when(taskService.save(ArgumentMatchers.any()))
                .thenReturn(TaskServiceMock.save());

        Mockito.doNothing().when(taskService).delete(ArgumentMatchers.anyLong());

        Mockito.when(taskService.replace(ArgumentMatchers.any()))
                .thenReturn(TaskServiceMock.replace());

        Mockito.when(taskService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(TaskServiceMock.findBy());

        Mockito.when(taskService.listAll(ArgumentMatchers.any()))
                .thenReturn(TaskServiceMock.listAll());
    }
    @Test
    @WithMockUser(username = "admin@email.com", authorities = { "ADMIN", "USER" })
    @DisplayName("Given that all fields are correct When create method is called Then the transaction will be successful")
    void createPersistsTaskWhenSuccessful() throws Exception {
        var toBeSend = taskCreateDTOJson.write(new TaskCreateDTO(TaskRepositoryMock.createValidTask())).getJson();
        var toBeCompare = taskDetailDTOJson.write(new TaskDetailDTO(TaskRepositoryMock.createValidTask())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/tasks")
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
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/tasks"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field is correct When delete method is called Then the transaction will be successful")
    void deletePersistsTaskWhenSuccessful() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/tasks/1"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id fields are not found When delete method is called Then returns http not found status 404")
    void deleteReturnNotFoundWhenIdNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Task Not Found"))
                .when(taskService).delete(ArgumentMatchers.anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/tasks/999"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @WithMockUser(username = "admin@email.com", authorities = { "ADMIN", "USER" })
    @DisplayName("Given that all fields are correct When update method is called Then the transaction will be successful")
    void updatePersistsTaskWhenSuccessful() throws Exception {
        var toBeSend = taskDetailDTOJson.write(new TaskDetailDTO(TaskRepositoryMock.createValidTask())).getJson();
        var toBeCompare = taskDetailDTOJson.write(new TaskDetailDTO(TaskRepositoryMock.createValidTask())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put("/tasks")
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
                        MockMvcRequestBuilders.put("/tasks"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field exist When findById method is called Then  the transaction will be successful")
    void findByIdFindTaskWhenSuccessful() throws Exception {
        var toBeCompare = taskDetailWithCommentsDTOJson.write(new TaskDetailWithCommentsDTO(TaskRepositoryMock.createValidTask())).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/tasks/id/1"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that Id field are not found When findById method is called Then returns http not found status 404")
    void findByIdReturnNotFoundWhenIdNotFound() throws Exception {
        Mockito.when(taskService.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new EntityNotFoundException("Task Not Found"));

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/tasks/id/999"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Given that tasks exists in database When listAll method is called Then the transaction will be successful")
    void listAllReturnsAllTasksWhenSuccessful() throws Exception {
        ObjectMapper mapper = new ObjectMapper()
                .findAndRegisterModules()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String toBeCompare = mapper.writeValueAsString(TaskServiceMock.listAll());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/tasks/all"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }

    @Test
    @WithMockUser
    @DisplayName("Given that no tasks exists in database When listAll method is called Then returns a empty list")
    void listAllReturnEmptyListWhenNoTaskIsFound() throws Exception {
        Mockito.when(taskService.listAll(ArgumentMatchers.any()))
                .thenReturn(TaskServiceMock.listEmpty());

        String toBeCompare = new ObjectMapper().writeValueAsString(TaskServiceMock.listEmpty());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/tasks/all"))
                .andReturn()
                .getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(toBeCompare);
    }
}