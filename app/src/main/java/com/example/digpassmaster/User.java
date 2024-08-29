package com.example.digpassmaster;

import java.util.UUID;

public class User {
    private String id;
    private String name;
    private String email;
    private String dept;

    private String post;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String email,String dept,String post) {
        this.id = generateUserId();
        this.name = name;
        this.email = email;
        this.dept=dept;
        this.post=post;
    }

    private String generateUserId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDept() {
        return dept;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPost() {
        return post;
    }
}
