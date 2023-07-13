package com.example.taskmanager.domain.task;

import com.example.taskmanager.domain.comment.CommentDetailDTO;
import com.example.taskmanager.domain.status.Status;
import com.example.taskmanager.domain.status.StatusHistoryDetailDTO;

import java.time.LocalDateTime;
import java.util.List;

public record TaskDetailDTO(
        Long id,
        Long userCreatorId,
        Long userFromId,
        Long userToId,
        Long personRequestingId,
        String title,
        String description,
        Enum<Status> status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime completedAt,
        List<CommentDetailDTO> comments,
        List<StatusHistoryDetailDTO> statusHistory

        ) {
    public TaskDetailDTO(Task task){
        this(
                task.getId(),
                task.getUserCreator().getId(),
                task.getUserFrom().getId(),
                task.getUserTo().getId(),
                task.getPersonRequesting().getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getCompletedAt(),
                (List<CommentDetailDTO>) task.getComments().stream().map(CommentDetailDTO::new),
                (List<StatusHistoryDetailDTO>) task.getStatusHistories().stream().map(StatusHistoryDetailDTO::new)
        );
    }
}
