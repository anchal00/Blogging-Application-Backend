package com.server.bloggingapplication.domain.user;

import com.server.bloggingapplication.application.user.CreateUserRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public Integer createUser(CreateUserRequestDTO user) {

        return userDAO.saveUser(user);

    }

    @Override
    public boolean unfollowUser(String followeeUserName) {
        String followerUserName = getCurrentUserInfo();
        if (followerUserName == null) {
            return false;
        }
        if (followeeUserName.equals(followerUserName)) {
            return false;
        }

        return userDAO.unfollowUser(followerUserName, followeeUserName);
    }

    @Override
    public boolean followUser(String followeeUserName) {
        String followerUserName = getCurrentUserInfo();
        if (followerUserName == null) {
            return false;
        }
        if (followeeUserName.equals(followerUserName)) {
            return false;
        }
        return userDAO.followUser(followerUserName, followeeUserName);

    }

    private String getCurrentUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated())
            return null;
        String userName = auth.getPrincipal().toString();
        return userName;
    }

}
