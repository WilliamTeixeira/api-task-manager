package com.example.taskmanager.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Person {
    protected Long id;
    protected String name;
    protected String email;
    private Address address;

    public Person(String name){
        this.name = name;
    }

    public Person(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
