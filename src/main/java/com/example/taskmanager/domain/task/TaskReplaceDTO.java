package com.example.taskmanager.domain.task;

import com.example.taskmanager.domain.status.Status;
import jakarta.validation.constraints.NotNull;

public record TaskReplaceDTO(
        @NotNull Long id,
        Long userFromId,
        Long userToId,
        Long personRequestingId,
        String title,
        String description,
        Status status ) {
}
