package com.example.taskmanager.repository;

import com.example.taskmanager.domain.person.Person;

import java.util.List;

public interface PersonRepository  {
    List<Person> listAll();
}
