package com.example.taskmanager.repository;

import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.user.User;
import com.example.taskmanager.mock.PersonRepositoryMock;
import com.example.taskmanager.mock.TaskRepositoryMock;
import com.example.taskmanager.mock.UserRepositoryMock;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("Test for Task Repository")
class TaskRepositoryTest {
    @Autowired
    TaskRepository repository;
    @Autowired
    TestEntityManager em;


    @Test
    @DisplayName("Given that all fields are correct When persisting the Task entity Then the transaction will be successful")
    void savePersistsTaskWhenSuccessful(){
        //Given
        var toBeSave = TaskRepositoryMock.createTaskToBeSave(createUser(), createPerson());

        //When
        var saved = this.repository.save(toBeSave);

        //Then
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isNotNull();
    }




    private Person createPerson() {
       var person = PersonRepositoryMock.createPersonToBeSaved();
        em.persist(person);
        return person;
    }

    private User createUser() {
        var user = UserRepositoryMock.createUserToBeSave();
        em.persist(user);
        return user;
    }

}