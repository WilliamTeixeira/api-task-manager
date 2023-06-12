package com.example.taskmanager.repository;

import com.example.taskmanager.domain.person.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByName(String name);
    Person findByEmail(String email);

    @Query("SELECT p FROM Person p WHERE UPPER(p.name) LIKE %:name%")
    Page<Person> searchByNameLike(String name, Pageable pageable);
    Page<Person> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Person> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
