package com.example.taskmanager.exception;

import org.springframework.validation.FieldError;

public record ValidateExceptionDetailItemDTO(String field, String message) {
    public ValidateExceptionDetailItemDTO(FieldError error){
        this(error.getField(), error.getDefaultMessage());
    }
}
