package com.server.bloggingapplication.application.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("deprecation")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        boolean userExists = checkIfUserExists(username);
        if (!userExists){
            throw new UsernameNotFoundException("User doesn't exist");
        }

        return new org.springframework.security.core.userdetails.User(username, getUserPassword(username),
                Collections.emptyList());
    }

    private boolean checkIfUserExists(String username) {

        return (jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE username = ? ", new Object[] { username },
                Integer.class)) == 1;
    }

    private String getUserPassword(String username) {
        return jdbcTemplate.queryForObject("SELECT passwd FROM users WHERE username = ?", new Object[] { username },
                String.class);
    }

}
