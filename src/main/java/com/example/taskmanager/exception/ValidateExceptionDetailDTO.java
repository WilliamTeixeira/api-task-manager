package com.example.taskmanager.exception;

import org.springframework.validation.FieldError;

public record ValidateExceptionDetailDTO(String field, String message) {
    public ValidateExceptionDetailDTO(FieldError error){
        this(error.getField(), error.getDefaultMessage());
    }
}
