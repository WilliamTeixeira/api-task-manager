package com.example.taskmanager.repository;

import com.example.taskmanager.domain.status.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
}
