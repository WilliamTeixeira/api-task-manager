package com.example.taskmanager.domain.comment;

import com.example.taskmanager.domain.task.Task;
import com.example.taskmanager.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Table(name = "comments")
@Entity(name = "Comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    private String comment;

    private LocalDateTime date;

}
