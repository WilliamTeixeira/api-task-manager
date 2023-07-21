package com.example.taskmanager.domain.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskCreateDTO(
        @NotNull Long userCreatorId,
        @NotNull Long userFromId,
        @NotNull Long userToId,
        @NotNull Long personRequestingId,
        @NotBlank String title,
        @NotBlank String description) {

    public TaskCreateDTO(Task task){
        this(task.getUserCreator().getId(),
                task.getUserFrom().getId(),
                task.getUserTo().getId(),
                task.getPersonRequesting().getId(),
                task.getTitle(),
                task.getDescription());
    }
}


