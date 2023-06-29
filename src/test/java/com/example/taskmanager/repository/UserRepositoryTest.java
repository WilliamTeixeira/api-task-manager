package com.example.taskmanager.repository;

import com.example.taskmanager.domain.user.User;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("Test for User Repository")
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    @DisplayName("Given the Name field is correct When findByName Then return a User entity wil be successful")
    void findByNameReturnUserWhenSuccessful(){
        User userToBeSaved = new User(1l, "user@email.com", new BCryptPasswordEncoder().encode("password"), "ROLE_USER,ROLE_ADMIN");
        User userSaved = this.repository.save(userToBeSaved);

        User findedUser = this.repository.findByName(userSaved.getUsername());

        Assertions.assertThat(findedUser).isNotNull();
        Assertions.assertThat(findedUser.getId()).isEqualTo(userSaved.getId());
        Assertions.assertThat(findedUser.getUsername()).isEqualTo(userSaved.getUsername());
    }



}