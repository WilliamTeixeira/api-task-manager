package com.example.taskmanager.controller;

import com.example.taskmanager.domain.task.TaskCreateDTO;
import com.example.taskmanager.domain.task.TaskDetailDTO;
import com.example.taskmanager.domain.task.TaskDetailWithCommentsDTO;
import com.example.taskmanager.domain.task.TaskReplaceDTO;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TaskDetailDTO> update(@RequestBody @Valid TaskReplaceDTO taskReplaceDTO){
        TaskDetailDTO taskDetailDTO = taskService.replace(taskReplaceDTO);
        return ResponseEntity.ok(taskDetailDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<TaskDetailDTO>> listAll(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(taskService.listAll(pageable));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<TaskDetailWithCommentsDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

}
