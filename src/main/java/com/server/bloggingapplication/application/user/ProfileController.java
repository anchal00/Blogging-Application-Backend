package com.server.bloggingapplication.application.user;

import com.server.bloggingapplication.domain.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogapp/profiles")
public class ProfileController {

    @Autowired
    private UserService userService;

    @PostMapping("/{userName}/follow")
    public ResponseEntity<Boolean> followUser(@PathVariable("userName") String followeeUserName) {

        boolean isFollowed = userService.followUser(followeeUserName);
        if (isFollowed) {
            return ResponseEntity.status(HttpStatus.OK).body(isFollowed);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }


    @PostMapping("/{userName}/unfollow")
    public ResponseEntity<Boolean> unfollowUser(@PathVariable("userName") String followeeUserName) {

        boolean isUnFollowed = userService.unfollowUser(followeeUserName);
        if (isUnFollowed) {
            return ResponseEntity.status(HttpStatus.OK).body(isUnFollowed);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }
}
