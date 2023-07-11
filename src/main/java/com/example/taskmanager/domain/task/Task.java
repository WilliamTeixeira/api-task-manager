package com.example.taskmanager.domain.task;


import com.example.taskmanager.domain.comment.Comment;
import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.status.Status;
import com.example.taskmanager.domain.status.StatusHistory;
import com.example.taskmanager.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "tasks")
@Entity(name = "Task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userCreator;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    private User userTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person personRequesting;

    private String title;

    private String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<StatusHistory> statusHistories  = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private LocalDateTime updatedAt;

}
