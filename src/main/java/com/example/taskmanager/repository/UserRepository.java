package com.example.taskmanager.repository;


import com.example.taskmanager.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByName(String username);
}
