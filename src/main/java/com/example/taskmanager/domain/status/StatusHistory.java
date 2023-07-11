package com.example.taskmanager.domain.status;

import com.example.taskmanager.domain.task.Task;
import com.example.taskmanager.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "status_histories")
@Entity(name = "History")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    @Column(name = "status_from")
    @Enumerated(EnumType.STRING)
    private Status statusFrom;

    @Column(name = "status_to")
    @Enumerated(EnumType.STRING)
    private Status statusTo;

    private LocalDateTime date;
}
