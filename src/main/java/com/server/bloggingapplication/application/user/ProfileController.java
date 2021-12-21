package com.server.bloggingapplication.application.user;

import java.util.Optional;

import com.server.bloggingapplication.domain.user.UserProfile;
import com.server.bloggingapplication.domain.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogapp/profiles")
public class ProfileController {

    @Autowired
    private UserService userService;

    @PostMapping("/{userName}/follow")
    public ResponseEntity<Boolean> followUser(@PathVariable("userName") String followeeUserName,
            @RequestHeader("Authorization") String authorization) {

        boolean isFollowed = userService.followUser(followeeUserName);
        if (isFollowed) {
            return ResponseEntity.status(HttpStatus.OK).body(isFollowed);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @PostMapping("/{userName}/unfollow")
    public ResponseEntity<Boolean> unfollowUser(@PathVariable("userName") String followeeUserName,
            @RequestHeader("Authorization") String authorization) {

        boolean isUnFollowed = userService.unfollowUser(followeeUserName);
        if (isUnFollowed) {
            return ResponseEntity.status(HttpStatus.OK).body(isUnFollowed);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable("userName") String username,
            @RequestHeader("Authorization") String token) {

        Optional<UserProfile> userProfile = userService.getProfile(username);
        if (!userProfile.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userProfile.get());

    }
}
