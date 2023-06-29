package com.example.taskmanager.domain.user;

public record UserDetailDTO (Long id, String username, String roles) {
   public UserDetailDTO(User user) {
       this(user.getId(), user.getUsername(), user.getRoles());
    }

}
