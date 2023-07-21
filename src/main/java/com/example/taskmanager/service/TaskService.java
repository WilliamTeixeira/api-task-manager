package com.example.taskmanager.service;

import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.status.Status;
import com.example.taskmanager.domain.task.*;
import com.example.taskmanager.domain.user.User;
import com.example.taskmanager.repository.PersonRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    public TaskDetailDTO save(TaskCreateDTO dto) {
        var userCreator = getUserContext();

        if (userCreator == null) {
            throw new EntityNotFoundException("UserCreator not found!");
        }

        if(!userRepository.existsById(dto.userFromId())) {
            throw new EntityNotFoundException("UserFrom not found!");
        }

        if(!userRepository.existsById(dto.userToId())) {
            throw new EntityNotFoundException("UserTo not found!");
        }

        if (!personRepository.existsById(dto.personRequestingId())) {
            throw new EntityNotFoundException("PersonRequesting not found!");
        }

        var userFrom = userRepository.getReferenceById(dto.userFromId());
        var userTo = userRepository.getReferenceById(dto.userToId());
        var personRequesting = personRepository.getReferenceById(dto.personRequestingId());

        var task = Task.builder()
                .userCreator(userCreator)
                .userFrom(userFrom)
                .userTo(userTo)
                .personRequesting(personRequesting)
                .title(dto.title())
                .description(dto.description())
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        var taskSaved = taskRepository.save(task);

        return new TaskDetailDTO(taskSaved);
    }

    public void delete(Long id) {
        Task task = taskRepository.getReferenceById(id);
        taskRepository.delete(task);
    }
    public TaskDetailDTO replace(TaskReplaceDTO dto) {
        Task task = taskRepository.getReferenceById(dto.id());

        if (task.getId() == null) {
            throw new EntityNotFoundException("Task not found!");
        }

        if (dto.userFromId() != null) {
            if(!userRepository.existsById(dto.userFromId())) {
                throw new EntityNotFoundException("User From not found!");
            }

            User userFrom = userRepository.getReferenceById(dto.userFromId());
            task.setUserFrom(userFrom);
        }

        if (dto.userToId() != null) {
            if(!userRepository.existsById(dto.userToId())) {
                throw new EntityNotFoundException("User From not found!");
            }

            User userTo = userRepository.getReferenceById(dto.userToId());
            task.setUserTo(userTo);
        }

        if (dto.personRequestingId() != null) {
            if (!personRepository.existsById(dto.personRequestingId())) {
                throw new EntityNotFoundException("Person Requesting not found!");
            }

            Person personRequesting = personRepository.getReferenceById(dto.personRequestingId());
            task.setPersonRequesting(personRequesting);
        }

        if (dto.title() != null && !dto.title().isEmpty()){
            String title = dto.title();
            task.setTitle(title);
        }

        if (dto.description() != null && !dto.description().isEmpty()) {
            String description = dto.description();
            task.setDescription(description);
        }

        if (dto.status() != null) {
            Status status = dto.status();
            task.updateStatus(status);
        }

        task.setUpdatedAt(LocalDateTime.now());

        var taskReplaced = taskRepository.save(task);

        return new TaskDetailDTO(taskReplaced);
    }

    public Page<TaskDetailDTO> listAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(TaskDetailDTO::new);
    }

    public TaskDetailWithCommentsDTO findById(Long id) {
        Task task = taskRepository.getReferenceById(id);

        return new TaskDetailWithCommentsDTO(task);
    }


    private User getUserContext() {
        User user = null;
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String userName = ((UserDetails) principal).getUsername();
            user = userRepository.findByName(userName);
        }
        return user;
    }
}
