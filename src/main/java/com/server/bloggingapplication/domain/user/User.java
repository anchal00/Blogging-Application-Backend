package com.server.bloggingapplication.domain.user;

public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String userName;
    private String bio;
    private String email;
    // private String password;

    public User(Integer id, String firstName, String lastName, String userName, String bio, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.bio = bio;
        this.email = email;
    }

    public User(String firstName, String lastName, String userName, String bio, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.userName = userName;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}
