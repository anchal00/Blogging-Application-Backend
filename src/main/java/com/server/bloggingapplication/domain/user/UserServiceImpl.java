package com.server.bloggingapplication.domain.user;

import com.server.bloggingapplication.application.user.CreateUserRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDAO userDAO; 

    @Override
    public Integer createUser(CreateUserRequestDTO user) {

        return userDAO.saveUser(user);
        
    }
}
