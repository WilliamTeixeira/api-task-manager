package com.example.taskmanager.mock;

import com.example.taskmanager.domain.user.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserRepositoryMock {

    public static User createUserToBeSave(){
        return new User(null, "email@email.com", "secret", "ROLE_USER");
    }

    public static User createValidUser() {
        return new User(1l, "email@email.com", crypt("secret"), "ROLE_USER");
    }

    public static User createValidReplacerUser() {
        return new User(1l, "email@email.com", "secret", "ROLE_USER");
    }

    private static String crypt(String password){
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.encode(password);
    }
}
