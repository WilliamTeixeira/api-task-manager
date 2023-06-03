package com.example.taskmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundHandler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        var errorList = ex.getFieldErrors()
                .stream()
                .map(ValidateExceptionDetailItemDTO::new)
                .toList();

        var responseError = ValidateExceptionDetailDTO.builder()
                .title("Bad Request Exception, Invalid Fields")
                .status(ex.getStatusCode().value())
                .details("Check the field(s) error")
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .fields(errorList)
                .build();

        return ResponseEntity.badRequest().body(responseError);
    }
}
