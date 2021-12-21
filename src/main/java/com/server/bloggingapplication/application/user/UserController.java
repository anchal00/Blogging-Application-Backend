package com.server.bloggingapplication.application.user;

import javax.validation.Valid;

import com.server.bloggingapplication.application.security.AuthenticationFilter;
import com.server.bloggingapplication.domain.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogapp/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequest) {
        
        Integer userId = userService.createUser(createUserRequest);
        if (userId == null || userId == -1 ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with same username or email is already Registered ");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Success!");
    }

    /**
     * Placeholder method for login endpoint, to make Swagger API doc work.
     * Won't actually handle the login request, as that will be overridden by AuthenticationFilter
     * @see AuthenticationFilter
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginUserRequestDTO loginRequest){
        return ResponseEntity.status(HttpStatus.OK).body("TOKEN ___");
    }
}
