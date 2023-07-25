package com.example.taskmanager.mock;

import com.example.taskmanager.domain.comment.Comment;
import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.status.Status;
import com.example.taskmanager.domain.status.StatusHistory;
import com.example.taskmanager.domain.task.Task;
import com.example.taskmanager.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class TaskRepositoryMock {

    public static Task createTaskToBeSave(User user, Person person) {
        Task task = new Task(
                null,
                user,
                user,
                user,
                person,
                "Simple task to test",
                "Big description",
                null,
                Status.PENDING,
                null,
                LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
                null,
                null
        );

        return task;
    }

    public static Task createValidTask() {
        List<Comment> comments = Collections.emptyList();
        List<StatusHistory> statusHistories = Collections.emptyList();

        return new Task(
                1l,
                UserRepositoryMock.createValidUser(),
                UserRepositoryMock.createValidUser(),
                UserRepositoryMock.createValidUser(),
                PersonRepositoryMock.createValidPerson(),
                "Title example",
                "Description example",
                comments,
                Status.PENDING,
                statusHistories,
                LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
                null,
                null
        );
    }

    public static Page<Task> pageOfValidTasks(){
        var task1 = createValidTask();
        task1.setId(1l);

        var task2 = createValidTask();
        task2.setId(2l);

        return new PageImpl<>(List.of(task1, task2));
    }

}
