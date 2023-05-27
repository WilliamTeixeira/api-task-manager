package com.example.taskmanager.domain;


import com.example.taskmanager.domain.person.Person;

import java.time.LocalDateTime;
import java.util.List;

public class Task {
    private Long id;
    private Person company;
    private Person personFrom;
    private Person personTo;
    private String description;
    private List<Comment> comments;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

}
