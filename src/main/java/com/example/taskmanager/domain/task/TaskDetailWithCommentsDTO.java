package com.example.taskmanager.domain.task;

import com.example.taskmanager.domain.comment.Comment;
import com.example.taskmanager.domain.comment.CommentDetailDTO;
import com.example.taskmanager.domain.status.Status;

import java.time.LocalDateTime;
import java.util.List;

public record TaskDetailWithCommentsDTO(
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
        List<CommentDetailDTO> commentList
) {
    public TaskDetailWithCommentsDTO(Task task){
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
                task.getComments().stream().map(CommentDetailDTO::new).toList()
        );
    }
}
