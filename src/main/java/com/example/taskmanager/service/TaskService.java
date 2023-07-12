package com.example.taskmanager.service;

import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.status.Status;
import com.example.taskmanager.domain.task.Task;
import com.example.taskmanager.domain.task.TaskCreateDTO;
import com.example.taskmanager.domain.task.TaskDetailDTO;
import com.example.taskmanager.domain.user.User;
import com.example.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

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
        Task task = repository.getReferenceById(id);
        repository.delete(task);
    }
}
