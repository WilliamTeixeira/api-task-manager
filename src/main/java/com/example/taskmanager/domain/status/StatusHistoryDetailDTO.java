package com.example.taskmanager.domain.status;

import java.time.LocalDateTime;

public record StatusHistoryDetailDTO(Long id, Long userId, Long taskId, Status statusFrom, Status statusTo, LocalDateTime date) {

    public StatusHistoryDetailDTO(StatusHistory statusHistory){
        this(
                statusHistory.getId(),
                statusHistory.getUser().getId(),
                statusHistory.getTask().getId(),
                statusHistory.getStatusFrom(),
                statusHistory.getStatusTo(),
                statusHistory.getDate()
        );
    }
}