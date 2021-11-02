package com.server.bloggingapplication.application.security;

import java.util.Collections;
import java.util.Optional;

import com.server.bloggingapplication.domain.user.User;
import com.server.bloggingapplication.domain.user.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userDAO.findByUserName(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User doesn't exist");
        }
        User user = optionalUser.get();
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
                Collections.emptyList());
    }

}
