package com.server.bloggingapplication.domain.user;

import java.util.Optional;

import com.server.bloggingapplication.application.user.CreateUserRequestDTO;

public interface UserDAO {

    Optional<User> findByUserName(String username);

    Integer saveUser(CreateUserRequestDTO user);

}
