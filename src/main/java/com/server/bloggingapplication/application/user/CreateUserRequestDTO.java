package com.server.bloggingapplication.application.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CreateUserRequestDTO {

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    private String bio;

    public CreateUserRequestDTO() {
    }

    public CreateUserRequestDTO(@NotBlank String username, @Email String email, @NotBlank String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public CreateUserRequestDTO(@NotBlank String username, @Email String email, @NotBlank String password,
            @NotBlank String firstname, @NotBlank String lastname, String bio) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
