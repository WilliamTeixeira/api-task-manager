package com.example.taskmanager.service;

import com.example.taskmanager.domain.user.User;
import com.example.taskmanager.domain.user.UserCreateDTO;
import com.example.taskmanager.domain.user.UserDetailDTO;
import com.example.taskmanager.domain.user.UserReplaceDTO;
import com.example.taskmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    public UserDetailDTO findById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        return new UserDetailDTO(user);
    }

    public UserDetailDTO findByUsername(String username) {
        User user = repository.findByName(username);

        if (user == null) {
            throw new EntityNotFoundException("User Not Found");
        }

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
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        repository.delete(user);
    }

    public UserDetailDTO replace(UserReplaceDTO userReplaceDTO) {
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
