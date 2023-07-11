package com.example.taskmanager.mock;

import com.example.taskmanager.domain.person.Person;
import com.example.taskmanager.domain.status.Status;
import com.example.taskmanager.domain.task.Task;
import com.example.taskmanager.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.Collections;

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
                LocalDateTime.now(),
                null,
                null
        );

        return task;
    }

    public static Page<Task> pageOfValidTasks(){
        return new PageImpl<>(Collections.emptyList());
    }
}
