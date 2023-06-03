package com.example.taskmanager.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ValidateExceptionDetailDTO {
    private String title;
    private int status;
    private String details;
    private String developerMessage;
    private LocalDateTime timestamp;
    private List<ValidateExceptionDetailItemDTO> fields;
}
