package com.example.taskmanager.repository;

import com.example.taskmanager.domain.person.Person;
import io.micrometer.observation.ObservationFilter;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByName(String name);
    Person findByEmail(String email);

    @Query("SELECT p FROM Person p WHERE UPPER(p.name) LIKE %:name%")
    Page<Person> searchByNameLike(String name, Pageable pageable);
    Page<Person> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Person> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
