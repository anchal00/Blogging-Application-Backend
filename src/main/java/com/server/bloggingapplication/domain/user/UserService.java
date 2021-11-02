package com.server.bloggingapplication.domain.user;

import com.server.bloggingapplication.application.user.CreateUserRequestDTO;

public interface UserService {
    
    Integer createUser(CreateUserRequestDTO user);
}
