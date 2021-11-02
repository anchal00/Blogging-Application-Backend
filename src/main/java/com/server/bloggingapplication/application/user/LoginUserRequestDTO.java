package com.server.bloggingapplication.application.user;

import javax.validation.constraints.NotBlank;

public class LoginUserRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public LoginUserRequestDTO() {
    }

    public LoginUserRequestDTO(@NotBlank String username, @NotBlank String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
