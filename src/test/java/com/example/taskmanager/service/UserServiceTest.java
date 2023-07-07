package com.example.taskmanager.service;

import com.example.taskmanager.domain.user.UserCreateDTO;
import com.example.taskmanager.domain.user.UserDetailDTO;
import com.example.taskmanager.domain.user.UserReplaceDTO;
import com.example.taskmanager.mock.UserRepositoryMock;
import com.example.taskmanager.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@DisplayName("Test for User Service")
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        Mockito.when(userRepository.save(ArgumentMatchers.any()))
                .thenReturn(UserRepositoryMock.createValidUser());

        Mockito.doNothing().when(userRepository).delete(ArgumentMatchers.any());

        Mockito.when(userRepository.getReferenceById(ArgumentMatchers.anyLong()))
                .thenReturn(UserRepositoryMock.createValidUser());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                        .thenReturn(Optional.of(UserRepositoryMock.createValidUser()));

        Mockito.when(userRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(UserRepositoryMock.createValidUser());

        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(UserRepositoryMock.createValidUser());
    }

    @Test
    @DisplayName("Given that Username field exist When loadUserByUsername method is called Then  the transaction will be successful")
    void loadUserByUsernameFindUserWhenSuccessful() throws Exception {
        var toBeCompare = UserRepositoryMock.createValidUser();

        var founded = userService.loadUserByUsername(toBeCompare.getUsername());

        Assertions.assertThat(founded)
                .isNotNull()
                .isInstanceOf(UserDetails.class);

        Assertions.assertThat(founded.getUsername()).isEqualTo(toBeCompare.getUsername());
    }

    @Test
    @DisplayName("Given that Id field exist When findById method is called Then  the transaction will be successful")
    void findByIdFindUserWhenSuccessful() throws Exception {
        var toBeCompare = new UserDetailDTO(UserRepositoryMock.createValidUser());

        var founded = userService.findById(toBeCompare.id());

        Assertions.assertThat(founded)
                .isNotNull()
                .isInstanceOf(UserDetailDTO.class);

        Assertions.assertThat(founded.username()).isEqualTo(toBeCompare.username());
    }


    @Test
    @DisplayName("Given that Username field exist When findByUsername method is called Then  the transaction will be successful")
    void findByUsernameFindUserWhenSuccessful() throws Exception {
        var toBeCompare = new UserDetailDTO(UserRepositoryMock.createValidUser());

        var founded = userService.findByUsername(toBeCompare.username());

        Assertions.assertThat(founded)
                .isNotNull()
                .isInstanceOf(UserDetailDTO.class);

        Assertions.assertThat(founded.username()).isEqualTo(toBeCompare.username());
    }

    @Test
    @DisplayName("Given that all fields are correct When save methods is called Then the transaction will be successful")
    void savePersistUserWhenSuccessful() throws Exception {
        var toBeSave = new UserCreateDTO(UserRepositoryMock.createUserToBeSave());
        var toBeCompare = new UserDetailDTO((UserRepositoryMock.createValidUser()));

        var saved = userService.save(toBeSave);

        Assertions.assertThat(saved)
                        .isNotNull()
                        .isInstanceOf(UserDetailDTO.class);

        Assertions.assertThat(saved.username()).isEqualTo(toBeCompare.username());
    }

    @Test
    @DisplayName("Given that Id field is correct When delete method is called Then the transaction will be successful")
    void deletePersistsUserWhenSuccessful() throws Exception {
        Assertions.assertThatCode(() -> userService.delete(1l))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Given that all fields are correct When replace method is called Then the transaction will be successful")
    void replaceUserPersonWhenSuccessful() throws Exception {
        var toBeSave = new UserReplaceDTO(UserRepositoryMock.createValidUser());
        var toBeCompare = new UserDetailDTO(UserRepositoryMock.createValidUser());

        var saved = userService.replace(toBeSave);

        Assertions.assertThat(saved)
                .isNotNull()
                .isInstanceOf(UserDetailDTO.class);

        Assertions.assertThat(saved.username()).isEqualTo(toBeCompare.username());
    }
}