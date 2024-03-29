package com.example.taskmanager.service;

import com.example.taskmanager.domain.task.TaskCreateDTO;
import com.example.taskmanager.domain.task.TaskDetailDTO;
import com.example.taskmanager.domain.task.TaskDetailWithCommentsDTO;
import com.example.taskmanager.domain.task.TaskReplaceDTO;
import com.example.taskmanager.mock.PersonRepositoryMock;
import com.example.taskmanager.mock.TaskRepositoryMock;
import com.example.taskmanager.mock.UserRepositoryMock;
import com.example.taskmanager.repository.PersonRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("Test for Task Service")
@ExtendWith(SpringExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setup() {
        Mockito.when(taskRepository.save(ArgumentMatchers.any()))
                .thenReturn(TaskRepositoryMock.createValidTask());

        Mockito.doNothing().when(taskRepository).delete(ArgumentMatchers.any());

        Mockito.when(taskRepository.getReferenceById(ArgumentMatchers.anyLong()))
                .thenReturn(TaskRepositoryMock.createValidTask());

        Mockito.when(taskRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                        .thenReturn(TaskRepositoryMock.pageOfValidTasks());

        Mockito.when(userRepository.getReferenceById(ArgumentMatchers.anyLong()))
                .thenReturn(UserRepositoryMock.createValidUser());

        Mockito.when(userRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(UserRepositoryMock.createValidUser());

        Mockito.when(userRepository.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(true);

        Mockito.when(personRepository.getReferenceById(ArgumentMatchers.anyLong()))
                .thenReturn(PersonRepositoryMock.createValidPerson());

        Mockito.when(personRepository.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(true);
    }

    @Test
    @WithMockUser(username = "admin@email.com", authorities = { "ADMIN", "USER" })
    @DisplayName("Given that all fields are correct When save method is called Then the transaction will be successful")
    void savePersistTaskWhenSuccessful() {
        var toBeSave = new TaskCreateDTO(TaskRepositoryMock.createValidTask());
        var toBeCompare = new TaskDetailDTO(TaskRepositoryMock.createValidTask());

        var saved = taskService.save(toBeSave);

        Assertions.assertThat(saved)
                .isNotNull()
                .isInstanceOf(TaskDetailDTO.class);

        Assertions.assertThat(saved.id()).isEqualTo(toBeCompare.id());
    }

    @Test
    @WithMockUser(username = "admin@email.com", authorities = { "ADMIN", "USER" })
    @DisplayName("Given that UserFrom field are not found When save method is called Then returns http not found status 404")
    void saveReturnNotFoundWhenUserFromNotFound() throws Exception {
        Mockito.when(userRepository.existsById(99l))
                .thenThrow(new EntityNotFoundException("UserFrom Not Found"));

        var task = TaskRepositoryMock.createValidTask();
        task.getUserFrom().setId(99l);

        var toBeSave = new TaskCreateDTO(task);

        Assertions.assertThatThrownBy(() -> taskService.save(toBeSave))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("UserFrom Not Found");
    }
    @Test
    @WithMockUser(username = "admin@email.com", authorities = { "ADMIN", "USER" })
    @DisplayName("Given that UserTo field are not found When save method is called Then returns http not found status 404")
    void saveReturnNotFoundWhenUserToNotFound() throws Exception {
        Mockito.when(userRepository.existsById(99l))
                .thenThrow(new EntityNotFoundException("UserTo Not Found"));

        var task = TaskRepositoryMock.createValidTask();
        task.getUserTo().setId(99l);

        var toBeSave = new TaskCreateDTO(task);

        Assertions.assertThatThrownBy(() -> taskService.save(toBeSave))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("UserTo Not Found");
    }

    @Test
    @WithMockUser(username = "admin@email.com", authorities = { "ADMIN", "USER" })
    @DisplayName("Given that PersonRequesting field are not found When save method is called Then returns http not found status 404")
    void saveReturnNotFoundWhenPersonRequestingNotFound() throws Exception {
        Mockito.when(personRepository.existsById(99l))
                .thenThrow(new EntityNotFoundException("PersonRequesting Not Found"));

        var task = TaskRepositoryMock.createValidTask();
        task.getPersonRequesting().setId(99l);

        var toBeSave = new TaskCreateDTO(task);

        Assertions.assertThatThrownBy(() -> taskService.save(toBeSave))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("PersonRequesting Not Found");
    }
    @Test
    @DisplayName("Given that Id field is correct When delete method is called Then the transaction will be successful")
    void deletePersistTaskWhenSuccessful() {
        Assertions.assertThatCode(()-> taskService.delete(1l))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Given thad all fields are correct When replace method is called Then the transaction will be successful ")
    void replacePersistTaskWhenSuccessful() {
        var toBeSave = new TaskReplaceDTO(TaskRepositoryMock.createValidTask());
        var toBeCompare = new TaskDetailDTO(TaskRepositoryMock.createValidTask());

        var saved = taskService.replace(toBeSave);

        Assertions.assertThat(saved)
                .isNotNull()
                .isInstanceOf(TaskDetailDTO.class);

        Assertions.assertThat(saved.title()).isEqualTo(toBeCompare.title());
    }

    @Test
    @DisplayName("Given thad all fields are correct When replace method is called Then the transaction will be successful ")
    void replacePersistTaskWithNullFieldsWhenSuccessful() {
        var toBeSave = new TaskReplaceDTO(1l,null,null,null,"changed title","",null);
        var toBeCompare = new TaskDetailDTO(TaskRepositoryMock.createValidTask());

        var saved = taskService.replace(toBeSave);

        Assertions.assertThat(saved)
                .isNotNull()
                .isInstanceOf(TaskDetailDTO.class);

        Assertions.assertThat(saved.title()).isEqualTo(toBeCompare.title());
    }

    @Test
    @DisplayName("Given that tasks exists in database When listAll method is called Then the transaction will be successful")
    void listAllReturnsAllTasksWhenSuccessful() throws Exception {
        var pageCompare = taskService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(pageCompare)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @DisplayName("Given that Id field exist When findById method is called Then  the transaction will be successful")
    void findByIdFindTaskWhenSuccessful() throws Exception {
        var toBeCompare = new TaskDetailWithCommentsDTO(TaskRepositoryMock.createValidTask());

        var found = taskService.findById(toBeCompare.id());

        Assertions.assertThat(found)
                .isNotNull()
                .isInstanceOf(TaskDetailWithCommentsDTO.class);

        Assertions.assertThat(found.id()).isEqualTo(toBeCompare.id());
        Assertions.assertThat(found.status()).isEqualTo(toBeCompare.status());
    }
}