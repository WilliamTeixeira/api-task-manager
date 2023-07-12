package com.example.taskmanager.controller;

import com.example.taskmanager.domain.person.PersonCreateDTO;
import com.example.taskmanager.domain.person.PersonDetailDTO;
import com.example.taskmanager.domain.task.TaskCreateDTO;
import com.example.taskmanager.domain.task.TaskDetailDTO;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid TaskCreateDTO taskCreateDTO, UriComponentsBuilder uriBuilder){
        TaskDetailDTO  taskDetailDTO = taskService.save(taskCreateDTO);

        var uri = uriBuilder.path("tasks/{id}").buildAndExpand(taskDetailDTO.id()).toUri();

        return ResponseEntity.created(uri).body(taskDetailDTO);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
