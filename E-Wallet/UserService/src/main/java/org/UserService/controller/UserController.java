package org.UserService.controller;

import org.UserService.model.User;
import org.UserService.request.CreateUserRequest;
import org.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-onboarding")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create/user")
    public User createUser(@RequestBody(required = false) CreateUserRequest createUserRequest){
        return userService.onboardNewUser(createUserRequest);
    }

    @GetMapping("/get/user")
    public String getUserInformation(@RequestParam("username") String username){
        return userService.fetchUserBYUsername(username);
    }
}
