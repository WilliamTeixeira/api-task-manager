package com.example.taskmanager.domain.comment;

import java.time.LocalDateTime;

public record CommentDetailDTO(Long id, Long userId, Long taskId, String comment, LocalDateTime date) {
    public CommentDetailDTO(Comment comment){
        this(comment.getId(), comment.getUser().getId(), comment.getTask().getId(), comment.getComment(), comment.getDate());
    }
}


