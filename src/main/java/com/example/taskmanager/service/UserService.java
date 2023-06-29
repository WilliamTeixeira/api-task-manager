package com.example.taskmanager.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import com.example.taskmanager.domain.user.User;
import com.example.taskmanager.domain.user.UserCreateDTO;
import com.example.taskmanager.domain.user.UserDetailDTO;
import com.example.taskmanager.domain.user.UserReplaceDTO;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    public UserDetailDTO findById(Long id) {
        return new UserDetailDTO(repository.getReferenceById(id));
    }

    public UserDetailDTO findByUsername(String username){
        return new UserDetailDTO(repository.findByName(username));
    }

    public UserDetailDTO save(UserCreateDTO userCreateDTO) {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        var password = bcrypt.encode(userCreateDTO.password());

        var user = new User(userCreateDTO);
        user.setPassword(password);

        User newUser = repository.save(user);

        return new UserDetailDTO(newUser);
    }

    public void delete(Long id) {
        User user = repository.getReferenceById(id);
        repository.delete(user);
    }

    public UserDetailDTO replace(UserReplaceDTO userReplaceDTO){
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        User savedUser = repository.getReferenceById(userReplaceDTO.id());

        User userToSave = new User(userReplaceDTO);
        userToSave.setId(savedUser.getId());

        var password = bcrypt.encode(userReplaceDTO.password());
        userToSave.setPassword(password);

        User newUserSaved = repository.save(userToSave);

        return new UserDetailDTO(newUserSaved);
    }


}
