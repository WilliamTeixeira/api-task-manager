package com.example.taskmanager.service;

import com.example.taskmanager.domain.comment.Comment;
import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.status.Status;
import com.example.taskmanager.domain.status.StatusHistory;
import com.example.taskmanager.domain.task.Task;
import com.example.taskmanager.domain.task.TaskCreateDTO;
import com.example.taskmanager.domain.task.TaskDetailDTO;
import com.example.taskmanager.domain.task.TaskReplaceDTO;
import com.example.taskmanager.domain.user.User;
import com.example.taskmanager.repository.PersonRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    public TaskDetailDTO save(TaskCreateDTO dto) {
        var task = Task.builder()
                .userCreator(new User())
                .userFrom(new User())
                .userTo(new User())
                .personRequesting(new Person())
                .title(dto.title())
                .description(dto.description())
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .comments(Collections.emptyList())
                .statusHistories(Collections.emptyList())
                .build();

        return new TaskDetailDTO(task);
    }

    public void delete(Long id) {
        Task task = taskRepository.getReferenceById(id);
        taskRepository.delete(task);
    }
    public TaskDetailDTO replace(TaskReplaceDTO dto) {
        Task task = taskRepository.getReferenceById(dto.id());

        if (dto.userFromId() != null) {
            User userFrom = userRepository.getReferenceById(dto.userFromId());
            task.setUserFrom(userFrom);
        }

        if (dto.userToId() != null) {
            User userTo = userRepository.getReferenceById(dto.userToId());
            task.setUserTo(userTo);
        }

        if (dto.personRequestingId() != null) {
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
            task.setStatus(status);
        }

        task.setUpdatedAt(LocalDateTime.now());

        var taskReplaced = taskRepository.save(task);

        return new TaskDetailDTO(taskReplaced);
    }
}
