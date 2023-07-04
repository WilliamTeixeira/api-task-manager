package com.example.taskmanager.mock;

import com.example.taskmanager.domain.user.UserDetailDTO;

public class UserServiceMock {

    public static UserDetailDTO save() {
        return new UserDetailDTO(UserRepositoryMock.createValidUser());
    }
    public static UserDetailDTO replace(){
        return new UserDetailDTO(UserRepositoryMock.createValidUser());
    }

    public static UserDetailDTO findBy() {
        return new UserDetailDTO(UserRepositoryMock.createValidUser());
    }



}
