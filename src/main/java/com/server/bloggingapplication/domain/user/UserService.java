package com.server.bloggingapplication.domain.user;

import java.util.Optional;

import com.server.bloggingapplication.application.user.CreateUserRequestDTO;

public interface UserService {
    
    Integer createUser(CreateUserRequestDTO user);

    boolean followUser(String followeeUserName);

    boolean unfollowUser(String followeeUserName);

    Optional<UserProfile> getProfile(String username);
}
