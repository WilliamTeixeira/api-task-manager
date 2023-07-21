package com.example.taskmanager.repository;

import com.example.taskmanager.domain.comment.Comment;
import com.example.taskmanager.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTask(Task task);
}
