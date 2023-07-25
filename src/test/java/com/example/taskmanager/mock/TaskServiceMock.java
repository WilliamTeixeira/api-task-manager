package com.example.taskmanager.mock;

import com.example.taskmanager.domain.task.Task;
import com.example.taskmanager.domain.task.TaskDetailDTO;
import com.example.taskmanager.domain.task.TaskDetailWithCommentsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

public class TaskServiceMock {

    public static TaskDetailDTO save() {
        return new TaskDetailDTO(TaskRepositoryMock.createValidTask());
    }
    public static TaskDetailDTO replace() {
        return new TaskDetailDTO(TaskRepositoryMock.createValidTask());
    }

    public static TaskDetailWithCommentsDTO findBy() {
        return new TaskDetailWithCommentsDTO(TaskRepositoryMock.createValidTask());
    }

    public static Page<TaskDetailDTO> listAll(){
        Page<Task> taskPage = TaskRepositoryMock.pageOfValidTasks();
        return taskPage.map(TaskDetailDTO::new);
    }
    public static Page<TaskDetailDTO> listEmpty(){
        PageImpl<Task> taskPage = new PageImpl<>(Collections.emptyList());
        return taskPage.map(TaskDetailDTO::new);
    }

}
