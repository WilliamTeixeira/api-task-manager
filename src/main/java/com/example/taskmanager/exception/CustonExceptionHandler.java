package com.example.taskmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustonExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entityNotFoundHandler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity fieldValidateHandler(MethodArgumentNotValidException ex){
        var errorList = ex.getFieldErrors()
                .stream()
                .map(ValidateExceptionDetailDTO::new)
                .toList();

        return ResponseEntity.badRequest().body(errorList);
    }
}
